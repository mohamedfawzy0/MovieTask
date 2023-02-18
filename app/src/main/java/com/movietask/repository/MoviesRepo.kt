package com.movietask.repository

import android.util.Log
import com.movietask.di.ApiInterface
import com.movietask.model.MoviesResponse
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class MoviesRepo @Inject constructor(private val service: ApiInterface) {

    suspend fun getMovies(): Flow<ApiResult<List<MoviesResponse.Movie>>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = service.getMovies()
            emit(ApiResult.Success(response.items))
        } catch (e: IOException) {
            emit(ApiResult.Error(e.localizedMessage.orEmpty()))
        } catch (e: HttpException) {
            emit(ApiResult.Error(e.localizedMessage.orEmpty()))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.localizedMessage.orEmpty()))
        }
    }

}

sealed class ApiResult<out T> {
    object Loading : ApiResult<Nothing>()
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}