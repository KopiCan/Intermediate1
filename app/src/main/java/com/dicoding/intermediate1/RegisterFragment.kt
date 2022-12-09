package com.dicoding.intermediate1

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate1.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var viewModel: AuthenticationViewModel? = null
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.btnRegister.setOnClickListener {
            val name = binding.fieldNameRegister.text.toString()
            val email = binding.fieldEmailRegister.text.toString()
            val password = binding.fieldPasswordRegister.text.toString()
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
                    viewModel?.register(name, email, password)
                }
            }
        }
    }
}
