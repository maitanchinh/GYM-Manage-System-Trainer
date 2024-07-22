package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.Comment
import com.example.gymmanagesystemtrainer.model.Communication
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.utils.RequiresAuth
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CommunicationApiService {
    @POST("class-communications/filter")
    suspend fun getCommunications(@Body filterRequestBody: FilterRequestBody): Response<Communication>

    @RequiresAuth
    @Multipart
    @POST("class-communications/{classId}")
    suspend fun postCommunication(
        @Path("classId") classId: String,
        @Part imageUrl: MultipartBody.Part?,
        @Part("message") message: RequestBody
    ): Communication

    @RequiresAuth
    @POST("class-communication-comments/{communicationId}")
    suspend fun sendComment(
        @Path("communicationId") communicationId: String,
        @Body body: CommentRequestBody
    ): Comment
}

data class CommentRequestBody(
    val message: String
)