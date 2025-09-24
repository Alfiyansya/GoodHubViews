package com.alfiansyah.goodhubviews.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alfiansyah.goodhubviews.core.data.Resource
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser
import com.alfiansyah.goodhubviews.core.utils.avoidEdgeToEdge
import com.alfiansyah.goodhubviews.core.utils.imageTarget
import com.alfiansyah.goodhubviews.core.utils.loadAndShowImage
import com.alfiansyah.goodhubviews.core.utils.parcelableExtra
import com.alfiansyah.goodhubviews.databinding.ActivityDetailGithubUserBinding
import com.alfiansyah.goodhubviews.image_preview.PreviewImageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailGithubUserActivity : AppCompatActivity() {

    val viewModel : DetailGithubUserViewModel by viewModels()
    private lateinit var binding: ActivityDetailGithubUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGithubUserBinding.inflate(layoutInflater)
        avoidEdgeToEdge(binding) // fungsi untuk android 15
        setContentView(binding.root)
        val data = intent.parcelableExtra<GithubUser>(EXTRA_DATA)
        Log.d("DetailGithubUserActivity", "onCreate: $data")
        initUi(data as GithubUser)
    }
    private fun initUi(data: GithubUser){
        viewModel.getDetailGithubUser(data.login.toString())
        viewModel.detailGithubUser.observe(this@DetailGithubUserActivity){ detailGithubUser ->
            when(detailGithubUser){
                is Resource.Error -> {
                    Log.e("DetailGithubUserActivity", "initUi: ${detailGithubUser.message}", )
                    binding.animDetailLoader.visibility = View.GONE
                    binding.layoutDetailData.visibility = View.VISIBLE
                    binding.apply {
                        tvDetailUsername.text = data.name
                        tvDetailName.text = data.login
                        tvDetailBio.text = if(data.bio.toString() == "null" ) "-" else data.bio.toString()
                        tvDetailCompany.text = if(data.company.toString() == "null") "-" else data.company.toString()
                        tvDetailLocation.text = if(data.location.toString() == "null") "-" else data.location.toString()
                        tvDetailBlog.text = if(data.blog.toString() == "null") "-" else data.blog.toString()
                        ivUserDetailImage.setOnClickListener {
                            startActivity(Intent(this@DetailGithubUserActivity, PreviewImageActivity::class.java).putExtra(PreviewImageActivity.DATA_NAME, data.avatarUrl))
                        }

                    }
                }
                is Resource.Loading -> binding.animDetailLoader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.animDetailLoader.visibility = View.GONE
                    binding.animeDetailFailedDataLoad.visibility = View.GONE
                    binding.layoutDetailData.visibility = View.VISIBLE

                    binding.apply {
                        val evidenceBeforeTarget = imageTarget(this@DetailGithubUserActivity) {
                            binding.ivUserDetailImage.setImageDrawable(it)
                        }
                        loadAndShowImage(
                            this@DetailGithubUserActivity,
                            detailGithubUser.data?.avatarUrl ,
                            evidenceBeforeTarget
                        )
                        tvDetailUsername.text = detailGithubUser.data?.name
                        tvDetailName.text = detailGithubUser.data?.login
                        tvDetailBio.text = if(detailGithubUser.data?.bio.toString() == "null" ) "-" else detailGithubUser.data?.bio.toString()
                        tvDetailCompany.text = if(detailGithubUser.data?.company.toString() == "null") "-" else detailGithubUser.data?.company.toString()
                        tvDetailLocation.text = if(detailGithubUser.data?.location.toString() == "null") "-" else detailGithubUser.data?.location
                        tvDetailBlog.text = detailGithubUser.data?.blog
                        tvDetailFollowingValue.text = detailGithubUser.data?.following.toString()
                        tvDetailFollowersValue.text = detailGithubUser.data?.followers.toString()
                        tvDetailRepoValue.text = detailGithubUser.data?.publicRepos.toString()
                    }
                }
                else -> {
                    Log.e("DetailGithubUserActivity", "initUi: Unknown State $detailGithubUser")
                }
            }
        }
    }
    companion object{
        const val EXTRA_DATA = "extra_data"
    }
}