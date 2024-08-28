package com.example.gymmanagesystemtrainer.di

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.navigation.NavHostController
import com.example.gymmanagesystemtrainer.network.AttendanceApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.gymmanagesystemtrainer.network.AuthApiService
import com.example.gymmanagesystemtrainer.network.CategoryApiService
import com.example.gymmanagesystemtrainer.network.ClassApiService
import com.example.gymmanagesystemtrainer.network.CommunicationApiService
import com.example.gymmanagesystemtrainer.network.CourseApiService
import com.example.gymmanagesystemtrainer.network.EquipmentApiService
import com.example.gymmanagesystemtrainer.network.FeedbackApiService
import com.example.gymmanagesystemtrainer.network.LessonApiService
import com.example.gymmanagesystemtrainer.network.UserApiService
import com.example.gymmanagesystemtrainer.utils.RequiresAuth
import com.example.gymmanagesystemtrainer.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val trustAllCerts: Array<TrustManager> = arrayOf(@SuppressLint("CustomX509TrustManager")
    object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    private val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, java.security.SecureRandom())
    }

    private val sslSocketFactory = sslContext.socketFactory

    class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val annotations = request.tag(Invocation::class.java)?.method()?.annotations

            val requiresAuth = annotations?.any { it is RequiresAuth } ?: false

            return if (requiresAuth) {
                val token = sessionManager.getToken()
                if (token.isNullOrEmpty()) {
                    Log.e("AuthInterceptor", "Token is null or empty")
                    throw IOException("Token is null or empty")
                }

                val authenticatedRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(authenticatedRequest)
            } else {
                chain.proceed(request)
            }
        }
    }

    private fun okHttpClient(sessionManager: SessionManager) : OkHttpClient {
        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .addInterceptor (AuthInterceptor(sessionManager))
            .build()

    }

    @Singleton
    @Provides
    fun provideRetrofit(sessionManager: SessionManager): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://gym.evericks.com/api/")
            .client(okHttpClient(sessionManager))
//            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }

    @Singleton
    @Provides
    fun provideCourseApiService(retrofit: Retrofit): CourseApiService {
        return retrofit.create(CourseApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideClassApiService(retrofit: Retrofit): ClassApiService {
        return retrofit.create(ClassApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideCategoryApiService(retrofit: Retrofit): CategoryApiService {
        return retrofit.create(CategoryApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideLessonApiService(retrofit: Retrofit): LessonApiService {
        return retrofit.create(LessonApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideCommunicationApiService(retrofit: Retrofit): CommunicationApiService {
        return retrofit.create(CommunicationApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAttendanceApiService(retrofit: Retrofit): AttendanceApiService {
        return retrofit.create(AttendanceApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideFeedbackApiService(retrofit: Retrofit): FeedbackApiService {
        return retrofit.create(FeedbackApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideEquipmentApiService(retrofit: Retrofit): EquipmentApiService {
        return retrofit.create(EquipmentApiService::class.java)
    }
}