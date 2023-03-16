package com.example.unsplash.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.api.UnsplashApi
import com.example.unsplash.data.auth.TokenStorage
import com.example.unsplash.data.models.CollectionsPhoto
import retrofit2.HttpException
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1
class CollectionsPagingSource (
    private val unsplashApi: UnsplashApi,
    private val query: String,
    val repository: UnsplashRepository
) : PagingSource<Int, CollectionsPhoto>() {

    private val token = "Bearer ${TokenStorage.accessToken}"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionsPhoto> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = repository.collectionsResponse(query, position, params.loadSize)
            val collections = response.results
            LoadResult.Page(
                data = collections,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (collections.isEmpty()) null else position + 1

            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CollectionsPhoto>): Int? {
        TODO("Not yet implemented")
    }
}