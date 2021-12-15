package com.crazy.instalogin_graphapi.network


import com.crazy.instalogin_graphapi.model.MediaResponse
import retrofit2.Call
import retrofit2.http.*


/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

interface NetworkService {

    @FormUrlEncoded
    @POST("oauth/access_token")
    fun getAccessToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("code") code: String,
        @Field("grant_type") scope: String = "authorization_code"
    ): Call<String>

    @GET("me?fields=id,username")
    fun me(
        @Query("access_token") access_token: String
    ): Call<String>

    @GET("/v11.0/{id}/media")
    fun media(
        @Path("id") userid: String,
        @Query("access_token") access_token: String,
        @Query("fields") fields: String
    ): Call<MediaResponse>
}
