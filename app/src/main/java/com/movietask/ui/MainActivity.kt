package com.movietask.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.movietask.R
import com.movietask.adapter.MovieAdapter
import com.movietask.databinding.ActivityMainBinding
import com.movietask.model.MoviesResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MoviesViewModel>()
    private var movieList = mutableListOf<MoviesResponse.Movie>()
    private var sortedMovieList = mutableListOf<MoviesResponse.Movie>()
    private val adapter: MovieAdapter by lazy { MovieAdapter(movieList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.getMovies()
        initView()
        setUpObservers()
        setUpListeners()
    }

    private fun initView() {
        setSupportActionBar(binding.toolBar)
        binding.recViewMovies.layoutManager = LinearLayoutManager(this)
        binding.recViewMovies.adapter = adapter
    }

    private fun setUpObservers() {
        viewModel.isLoading.observe(this) {
            binding.refresh.isRefreshing = it
        }
        viewModel.errorMessage.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE).show()
        }
        viewModel.movieLiveData.observe(this) {
            it?.let {
                binding.cardNoData.isVisible = it.isEmpty()
                binding.recViewMovies.isVisible = it.isNotEmpty()
                if (it.isNotEmpty()) {
                    movieList.clear()
                    movieList.addAll(it)
                    movieList.sortByDescending { it.year }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.refresh.setOnRefreshListener {
            if (binding.refresh.isRefreshing) {
                focusView(binding.tvYear)
                binding.tvYear.setTextColor(resources.getColor(R.color.white))
                binding.tvRate.setTextColor(resources.getColor(R.color.black))
                viewModel.getMovies()
            }
        }
        binding.tvYear.setOnClickListener(this)
        binding.tvRate.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.tvYear -> {
                    focusView(binding.tvYear)
                    binding.tvYear.setTextColor(resources.getColor(R.color.white))
                    binding.tvRate.setTextColor(resources.getColor(R.color.black))
                    if (movieList.isNotEmpty()) {
                        movieList.sortByDescending { it.year }
                        sortedMovieList.clear()
                        sortedMovieList.addAll(movieList)
                        adapter.notifyDataSetChanged()
                    }
                }
                R.id.tvRate -> {
                    focusView(binding.tvRate)
                    binding.tvRate.setTextColor(resources.getColor(R.color.white))
                    binding.tvYear.setTextColor(resources.getColor(R.color.black))
                    if (movieList.isNotEmpty()) {
                        movieList.sortByDescending { it.imDbRating }
                        sortedMovieList.clear()
                        sortedMovieList.addAll(movieList)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun focusView(view: View) {
        binding.tvYear.background =
            ResourcesCompat.getDrawable(resources, R.color.transparent, null)
        binding.tvRate.background =
            ResourcesCompat.getDrawable(resources, R.color.transparent, null)

        view.background =
            ResourcesCompat.getDrawable(resources, R.drawable.rounded_primary, null)
    }
}