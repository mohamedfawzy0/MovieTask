package com.movietask.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movietask.model.MoviesResponse
import com.movietask.repository.ApiResult
import com.movietask.repository.MoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repo: MoviesRepo) : ViewModel() {

    private var _isLoadingLivData: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoadingLivData

    private val _movieLiveData: MutableLiveData<List<MoviesResponse.Movie>> = MutableLiveData()
    val movieLiveData: LiveData<List<MoviesResponse.Movie>> = _movieLiveData

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage

    fun getMovies() {
        viewModelScope.launch {
            repo.getMovies()
                .flowOn(Dispatchers.IO)
                .onEach {
                    when (it) {
                        is ApiResult.Error -> {
                            _isLoadingLivData.value = false
                            _errorMessage.value = it.message
                        }
                        ApiResult.Loading -> {
                            _isLoadingLivData.value = true
                        }
                        is ApiResult.Success -> {
                            _isLoadingLivData.value = false
                            _movieLiveData.value = it.data!!
                        }
                    }
                }.collect()
        }
    }
}
