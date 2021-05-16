package com.dr.apigallery2.ApiPosts

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface JsonPlaceHolder {


//    @GET("")
//    fun getPosts(): Call<Post>

    @GET
    fun getPosts(@Url url: String?): Call<Post>

}

