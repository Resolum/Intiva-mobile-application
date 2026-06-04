package com.resolum.intiva.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.resolum.intiva.BuildConfig
import com.resolum.intiva.core.data.local.datastore.TokenDataStore
import com.resolum.intiva.core.network.api.ApiClientFactory
import com.resolum.intiva.core.network.interceptor.AuthInterceptor
import com.resolum.intiva.core.network.interceptor.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/** DataStore extension — one instance per app. */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

/**
 * Dagger Hilt module providing network-related dependencies such as OkHttpClient, Retrofit,
 * and DataStore. All dependencies are scoped to the SingletonComponent, ensuring a single
 * instance throughout the app's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of DataStore<Preferences> for the entire app. This allows
     * for centralized management of key-value pairs, such as authentication tokens or user settings.
     */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    /**
     * Provides a singleton instance of AuthInterceptor, which is responsible for adding
     * authentication headers to outgoing network requests. The token provider is currently
     * a placeholder and should be replaced with a real implementation that retrieves the
     * token from a secure source, such as DataStore or a repository.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenDataStore: TokenDataStore): AuthInterceptor =
        AuthInterceptor(tokenProvider = {
            runBlocking { tokenDataStore.authToken.firstOrNull() }
        })

    /**
     * Provides a singleton instance of LoggingInterceptor, which logs network requests and
     * responses for debugging purposes. This interceptor can be configured to log at different
     * levels (e.g., BASIC, BODY) depending on the needs of the app.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): LoggingInterceptor = LoggingInterceptor()

    /**
     * Provides a singleton instance of OkHttpClient, configured with the provided AuthInterceptor
     * and LoggingInterceptor. This client will be used for all network requests, ensuring that
     * authentication and logging are consistently applied across the app.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: LoggingInterceptor,
    ): OkHttpClient =
        ApiClientFactory.buildOkHttpClient(authInterceptor, loggingInterceptor)

    /**
     * Provides a singleton instance of Retrofit, configured with the base URL from BuildConfig
     * and the provided OkHttpClient. This Retrofit instance will be used to create API service
     * interfaces for making network requests.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        ApiClientFactory.buildRetrofit(BuildConfig.BASE_URL, okHttpClient)
}
