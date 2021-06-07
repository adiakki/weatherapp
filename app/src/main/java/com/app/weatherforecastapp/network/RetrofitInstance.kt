package com.app.weatherforecastapp.network

import android.content.Context
import com.app.weatherforecastapp.utils.AppUtils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * This is the singleton class
 * used to create the retrofit builder instance
 * being used in calling server apis
 */
object RetrofitInstance {

    private var retrofit: Retrofit? = null

    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

    fun create(application: Context) : Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(createOkHttpClient(application)).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit!!
    }

    /**
     * This is the method that creates the okHttpClient
     * instance
     */
    private fun createOkHttpClient(application: Context): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply { level =  HttpLoggingInterceptor.Level.BODY}

        val httpCacheDirectory = File(application.cacheDir, "offlineCache")
        //10 MB
        val cache = Cache(httpCacheDirectory, 10 * 1024 * 1024)

       return OkHttpClient.Builder()
            .addInterceptor(provideOfflineCacheInterceptor(application))
            .addNetworkInterceptor(provideCacheInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .cache(cache)
            .build()
    }

    /**
     * This is the method that helps in creating the
     * caching on network apis
     *
     * we define the maxage 2 hour that means data will be fetched from cache
     * for 2 hours.
     */
    private fun provideCacheInterceptor() = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 2*60*60
         response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }


    /**
     * This method is used to create the interceptor for offline
     * caching for 2 hours.
     */
    private fun provideOfflineCacheInterceptor(application: Context) = Interceptor { chain ->
        var request = chain.request()
        if (!AppUtils.isInternetAvailable(application)) {
            val maxStale = 2*60*60 // offline caching for 2 hours
            val params = "public, only-if-cached, max-stale="
            request = request.newBuilder()
                .header("Cache-Control",  params + maxStale)
                .removeHeader("Pragma")
                .build()
        }
        chain.proceed(request)
    }
}