package com.example.unsplash.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.api.UnsplashApi
import com.example.unsplash.data.auth.TokenStorage
import com.example.unsplash.data.models.GetCollectionsPhoto
import com.example.unsplash.data.models.MyLikedPhoto
import com.example.unsplash.data.models.UnsplashPhoto
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class MyLikedPhotosPagingSource(
    private val unsplashApi: UnsplashApi,
    private val username: String,
    val repository: UnsplashRepository
) : PagingSource<Int, MyLikedPhoto>() {

    private val token = "Bearer ${TokenStorage.accessToken}"
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyLikedPhoto> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = repository.getMyLikedPhotosResults(username, position, params.loadSize)
            val photos = response
            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1

            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MyLikedPhoto>): Int? {
        TODO("Not yet implemented")
    }
}