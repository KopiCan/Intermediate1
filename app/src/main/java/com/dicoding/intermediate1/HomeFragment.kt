package com.dicoding.intermediate1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.intermediate1.databinding.FragmentHomeBinding
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var viewModel: HomeViewModel? = null
    private val rvAdapter = HomeAdapter()
    private lateinit var binding: FragmentHomeBinding
    private var temporaryToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        viewModel = ViewModelProvider(this, ViewModelFactory((activity as MainActivity)))[HomeViewModel::class.java]
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val preferences = SettingPreferences.getInstance((activity as MainActivity).dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelSettingFactory(preferences))[ViewModelSetting::class.java]
        settingViewModel.getProfilePreferences(Constanst.ProfilePreferences.ProfileToken.name)
            .observe(viewLifecycleOwner) {token ->
                temporaryToken = StringBuilder("Bearer ").append(token).toString()
                viewModel?.loadStory(temporaryToken)
            }

        binding.rvHome.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = rvAdapter
        }

        viewModel?.apply { loader.observe(viewLifecycleOwner) {binding.loadingAnim.root.visibility = it }
        error.observe(viewLifecycleOwner){if (it.isNotEmpty()) Helper.showInteraction(requireContext(),it)}
            dataList.observe(viewLifecycleOwner){
                rvAdapter.apply { initData(it)
                    notifyDataSetChanged()
                }
            }
        }
        return binding.root
    }

    override fun onRefresh() {
        binding.refreshLayout.isRefreshing = true
        viewModel?.loadStory(temporaryToken)
        Timer().schedule(2000) {
            binding.refreshLayout.isRefreshing = false
        }
    }


}
