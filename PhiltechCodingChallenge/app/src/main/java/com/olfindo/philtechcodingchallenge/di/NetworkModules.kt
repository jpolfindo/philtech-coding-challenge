package com.olfindo.philtechcodingchallenge.di

import com.olfindo.philtechcodingchallenge.data.repository.UserRepoManager
import com.olfindo.philtechcodingchallenge.data.repository.UserRepoManagerImpl
import com.olfindo.philtechcodingchallenge.data.source.remote.GetRandomUserService
import com.olfindo.philtechcodingchallenge.data.source.remote.RemoteDataSource
import com.olfindo.philtechcodingchallenge.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * A Dagger Hilt module that provides network-related dependencies,
 * such as Retrofit, OkHttpClient, and service interfaces for remote data operations.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of [Retrofit] configured with the base URL,
     * Gson converter, and an OkHttpClient instance.
     *
     * @param httpClient The OkHttpClient instance for handling HTTP requests and responses.
     * @return A configured [Retrofit] instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/") // Base URL for the API
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin objects
            .client(httpClient) // Custom HTTP client
            .build()
    }

    /**
     * Provides a singleton instance of [OkHttpClient], configured with logging
     * and timeout settings.
     *
     * @return A configured [OkHttpClient] instance.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs HTTP request and response bodies
        }

        return OkHttpClient.Builder()
            .connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS) // Timeout for connections
            .readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS) // Timeout for reading data
            .addInterceptor(loggingInterceptor) // Adds logging to HTTP requests
            .build()
    }

    /**
     * Provides a singleton instance of [GetRandomUserService],
     * which is used for API calls to fetch random user data.
     *
     * @param retrofit The [Retrofit] instance used to create the service interface.
     * @return An implementation of [GetRandomUserService].
     */
    @Provides
    @Singleton
    fun provideRandomUserService(retrofit: Retrofit): GetRandomUserService =
        retrofit.create(GetRandomUserService::class.java)

    /**
     * Provides a singleton instance of [UserRepoManager],
     * which serves as the repository layer for user data.
     *
     * @param remoteDataSource The [RemoteDataSource] instance used to fetch remote data.
     * @return An implementation of [UserRepoManager].
     */
    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: RemoteDataSource): UserRepoManager =
        UserRepoManagerImpl(remoteDataSource)
}
