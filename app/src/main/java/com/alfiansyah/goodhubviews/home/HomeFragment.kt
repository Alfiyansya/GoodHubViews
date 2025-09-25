package com.alfiansyah.goodhubviews.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.alfiansyah.goodhubviews.core.data.Resource
import com.alfiansyah.goodhubviews.core.databinding.ItemUserBinding
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser
import com.alfiansyah.goodhubviews.core.ui.adapter.ReusableAdapter
import com.alfiansyah.goodhubviews.core.utils.imageTarget
import com.alfiansyah.goodhubviews.core.utils.loadAndShowImage
import com.alfiansyah.goodhubviews.databinding.FragmentHomeBinding
import com.alfiansyah.goodhubviews.detail.DetailGithubUserActivity
import com.alfiansyah.goodhubviews.image_preview.PreviewImageActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var homeAdapter: ReusableAdapter<GithubUser>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            setupAdapter()
            binding.swipe.setOnRefreshListener {
                showUserGithubData(false)
                // Menyembunyikan berbagai layout sebelum refresh
                showProgressBar(true)
                showFailedLoadData(false)
                homeViewModel.refreshGithubUsers()
                showUserGithubData(true)
            }
            observeUserState()
        }
    }
    private fun observeUserState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.githubUserState.collect{ githubUser ->
                    when (githubUser) {
                        is Resource.Loading -> showProgressBar(true)
                        is Resource.Success -> {
                            binding.swipe.isRefreshing = false
                            showFailedLoadData(false)
                            showProgressBar(false)
                            homeAdapter.setItems(githubUser.data ?: emptyList())
                            showUserGithubData(true)
                        }
                        is Resource.Error -> {
                            binding.swipe.isRefreshing = false
                            showProgressBar(false)
                            showUserGithubData(false)
                            showFailedLoadData(true)

                        }
                    }
                }
            }
        }
    }
    private fun setupAdapter() {
        homeAdapter = ReusableAdapter(
            context = requireContext(),
            layoutResId = com.alfiansyah.goodhubviews.core.R.layout.item_user,
            bindViewHolder = { view, data ->
                val itemBinding = ItemUserBinding.bind(view)
                itemBinding.tvName.text = data.login
                val evidenceBeforeTarget = imageTarget(requireContext()) {
                    itemBinding.ivItemImage.setImageDrawable(it)
                }
                loadAndShowImage(
                    requireContext(),
                    data.avatarUrl ,
                    evidenceBeforeTarget
                )
                itemBinding.cvItem.setOnClickListener {
                    startActivity(Intent(requireContext(), DetailGithubUserActivity::class.java).putExtra(DetailGithubUserActivity.EXTRA_DATA, data))
                }
                itemBinding.ivItemImage.setOnClickListener {
                    startActivity(Intent(requireContext(), PreviewImageActivity::class.java).putExtra(PreviewImageActivity.DATA_NAME, data.avatarUrl))
                }
            }
        )
        binding.githubUserRv.layoutManager = GridLayoutManager(requireContext(), 1,GridLayoutManager.VERTICAL,false)
        binding.githubUserRv.adapter = homeAdapter
    }
    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showFailedLoadData(isFailed: Boolean) {
        binding.animFailedDataLoad.visibility = if (isFailed) View.VISIBLE else View.GONE
        binding.tvFailed.visibility = if (isFailed) View.VISIBLE else View.GONE
    }
    private fun showUserGithubData(isDataReady : Boolean){
        binding.githubUserRv.visibility = if (isDataReady) View.VISIBLE else View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding == null
    }

}