package com.example.dummyapi.api

import com.example.dummyapi.model.delete.Delete
import com.example.dummyapi.model.main.Data
import com.example.dummyapi.model.main.PostData
import com.example.dummyapi.model.main.Posts
import retrofit2.Response
import retrofit2.http.*

// setting up all kinds of callouts

interface Api {
    @Headers("app-id: 625c4479c48cf904cfd6e354")
    @GET("post")

    suspend fun getPosts(
        @Query("page") pageNumber: Int,
        @Query("limit") postsNumber: Int
    ): Response<Posts>

    @Headers("app-id: 625c4479c48cf904cfd6e354")
    @GET("post/{post}")
    suspend fun getPost(
        @Path("post") postId: String
    ): Response<PostData>

    @Headers("app-id: 625c4479c48cf904cfd6e354")
    @GET("tag/{tag}/post")
    suspend fun getPostsByTag(
        @Path("tag") tag: String,
        @Query("limit") postsNumber: Int
    ): Response<Posts>

    @Headers("app-id: 625c4479c48cf904cfd6e354")
    @DELETE("post/{post}")
    suspend fun deletePost(
        @Path("post") postId: String
    ): Response<Delete>

    @FormUrlEncoded
    @Headers("app-id: 625c4479c48cf904cfd6e354")
    @POST("post/create")
    suspend fun createPost(
        @Field("text") text: String,
        @Field("image") image: String,
        @Field("likes") likes: Int,
        @Field("tags") tags: ArrayList<String>,
        @Field("owner") owner: String,
    ): Response<Data>

}