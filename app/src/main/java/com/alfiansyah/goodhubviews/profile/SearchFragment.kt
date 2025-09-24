package com.alfiansyah.goodhubviews.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.alfiansyah.goodhubviews.core.data.Resource
import com.alfiansyah.goodhubviews.core.databinding.ItemUserBinding
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser
import com.alfiansyah.goodhubviews.core.ui.adapter.ReusableAdapter
import com.alfiansyah.goodhubviews.core.utils.imageTarget
import com.alfiansyah.goodhubviews.core.utils.loadAndShowImage
import com.alfiansyah.goodhubviews.core.utils.showShortToast
import com.alfiansyah.goodhubviews.databinding.FragmentSearchBinding
import com.alfiansyah.goodhubviews.detail.DetailGithubUserActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchJob: Job? = null
    private lateinit var searchAdapter: ReusableAdapter<GithubUser>
    private val searchViewModel: SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            setupAdapter()
            searchViewModel.userBySearch.observe(viewLifecycleOwner) { userBySearch ->
                if (userBySearch != null) {
                    when (userBySearch) {
                        is Resource.Loading -> showProgressBar(true)
                        is Resource.Success -> {
                            showFailedLoadData(false)
                            showProgressBar(false)
                            binding.rvSearchGithubUser.visibility = View.VISIBLE
                            searchAdapter.setItems(userBySearch.data ?: emptyList())
                        }
                        is Resource.Error -> {
                            showProgressBar(false)
                            showFailedLoadData(true)
                            showShortToast(userBySearch.message)
                        }
                    }
                }
            }
            binding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null){
                        searchJob?.cancel() // Batalkan job pencarian sebelumnya
                        searchJob = lifecycleScope.launch {
                            delay(500L) // Tunggu 500 milidetik
                            searchViewModel.getUserBySearch(query.trim())
                        }
                        binding.svSearch.clearFocus()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()){
                        showNoData(true)
                        binding.rvSearchGithubUser.visibility = View.GONE
                        searchAdapter.setItems(emptyList())
                    }else{
                        showNoData(false)
                    }
                    return true
                }

            })
        }
    }
    //    Menyiapkan adapter untuk recyclerview (komponen untuk menampilkan list di android)
    private fun setupAdapter() {
        searchAdapter = ReusableAdapter(
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
//                Mengatur item di list ketika diklik
                itemBinding.cvItem.setOnClickListener {
                    startActivity(Intent(requireContext(), DetailGithubUserActivity::class.java).putExtra(DetailGithubUserActivity.EXTRA_DATA, data))
                }
            }
        )
        binding.rvSearchGithubUser.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.rvSearchGithubUser.adapter = searchAdapter
    }
    private fun showProgressBar(isLoading: Boolean) {
        binding.rvSearchGithubUser.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showFailedLoadData(isFailed: Boolean) {
        binding.animFailedDataLoad.visibility = if (isFailed) View.VISIBLE else View.GONE
        binding.tvFailed.visibility = if (isFailed) View.VISIBLE else View.GONE

    }
    private fun showNoData(isNoData: Boolean) {
        binding.noData.visibility = if (isNoData) View.VISIBLE else View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding == null
    }
}