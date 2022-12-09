package com.dicoding.intermediate1

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate1.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var viewModel: AuthenticationViewModel? = null
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences =
            SettingPreferences.getInstance((activity as AuthenticationActivity).dataStore)
        val viewModelSetting = ViewModelProvider(this, ViewModelSettingFactory(preferences))[ViewModelSetting::class.java]
        viewModel = ViewModelProvider(this, ViewModelFactory((activity as AuthenticationActivity)))[AuthenticationViewModel::class.java]
        viewModel?.let { state ->
            state.loginState.observe(viewLifecycleOwner) { login ->
                viewModelSetting.setProfilePreferences(
                    login.loginResult.token,
                    login.loginResult.userId,
                    login.loginResult.name,
                    viewModel!!.temporaryEmail.value ?: Constanst.defaultValue
                )
            }
            state.error.observe(viewLifecycleOwner) {error ->
                if (error.isNotEmpty()) {
                    Helper.showInteraction(requireContext(), error)
                }
            }
            state.loader.observe(viewLifecycleOwner) {status ->
                binding.loadingAnim.root.visibility = status
            }
        }
        viewModelSetting.getProfilePreferences(Constanst.ProfilePreferences.ProfileToken.name)
            .observe(viewLifecycleOwner) { token ->
                if (token != Constanst.defaultValue) (activity as AuthenticationActivity).moveToMainActivity()
            }
        binding.btnLogin.setOnClickListener {
            val email = binding.fieldEmailLogin.text.toString()
            val password = binding.fieldPasswordLogin.text.toString()
            when {
                email.isEmpty() or password.isEmpty() -> {
                    Helper.showInteraction(
                        requireContext(),
                        getString(R.string.mandatory_field)
                    )
                }
                !email.matches(Constanst.emailDefaultPattern) -> {
                    Helper.showInteraction(
                        requireContext(),
                        getString(R.string.error_400)
                    )
                }
                password.length <= 6 -> {
                    Helper.showInteraction(
                        requireContext(),
                        getString(R.string.error_password_minimum)
                    )
                }
                else -> {
                    viewModel?.login(email, password)
                }
            }
        }
        binding.tvIntoRegister.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, RegisterFragment(), RegisterFragment::class.java.simpleName)
                addSharedElement(binding.tvLoginTitle, "auth")
                addSharedElement(binding.fieldEmailLogin, "email")
                addSharedElement(binding.fieldPasswordLogin, "password")
                addSharedElement(binding.bottomContainer, "bot_container")
                commit()
            }
        }
    }

    companion object {
        fun instance() = LoginFragment()
    }

}