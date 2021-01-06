package com.example.faketagram_app.interfaces;

import com.example.faketagram_app.CommentRequest;
import com.example.faketagram_app.FavouriteResponse;
import com.example.faketagram_app.FollowResponse;
import com.example.faketagram_app.LoginResponse;
import com.example.faketagram_app.StatusRequest;
import com.example.faketagram_app.model.Comments;
import com.example.faketagram_app.model.Photographs;
import com.example.faketagram_app.model.Users;
import com.example.faketagram_app.LoginRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

 @POST("signUp")
 @FormUrlEncoded
 Call<Users> signUpUser(
         @Field("name") String name,
         @Field("lastname") String lastname,
         @Field("username") String username,
         @Field("email") String email,
         @Field("password") String password
 );

 @POST("login")
 Call<LoginResponse> login(@Body LoginRequest user);

 @GET("user")
 Call<Users> getLoggedUser(@Header("Authorization") String authToken);

 @GET("logout")
 Call<ResponseBody> logout(@Header("Authorization") String authToken);

 @GET("{name}/getName")
 Call<List<Users>> getSearchUsers(@Header("Authorization") String authToken, @Path("name") String name);

 @POST("uploadProfilePhoto")
 @Multipart
 Call<ResponseBody> uploadProfilePhoto(@Header("Authorization") String authToken, @Part MultipartBody.Part file);

 @POST("uploadFeedPhoto")
 @Multipart
 Call<ResponseBody> uploadFeedPhoto(@Header("Authorization") String authToken, @Part MultipartBody.Part file);

 @POST("updateStatus")
 Call<ResponseBody> updateStatus(@Header("Authorization") String authToken, @Body StatusRequest status);

 @POST("{user_followed_id}/followUser")
 Call<ResponseBody> followUser(@Header("Authorization") String authToken, @Path("user_followed_id") int user_followed_id);

 @DELETE("{user_followed_id}/unfollowUser")
 Call<ResponseBody> unfollowUser(@Header("Authorization") String authToken, @Path("user_followed_id") int user_followed_id);

 @GET("following")
 @Headers({
         "Accept: identity"})
 Call<List<FollowResponse>> getFollowings(@Header("Authorization") String authToken);

 @GET("followers")
 @Headers({
         "Accept: identity"})
 Call<List<FollowResponse>> getFollowers(@Header("Authorization") String authToken);

 @GET("{user_id}/getUser")
 @Headers({
         "Accept: identity"})
 Call<Users> getUserById(@Header("Authorization") String authToken, @Path("user_id") int user_id);

 @GET("{user_id}/getUserPhotographs")
 Call<List<Photographs>> getPhotographsByUserId(@Header("Authorization") String authToken, @Path("user_id") int user_id);

 @GET("{photograph_id}/getPhotographById")
 Call<Photographs> getPhotographById(@Header("Authorization") String authToken, @Path("photograph_id") int user_id);

 @POST("{photograph_id}/addToFavourites")
 Call<ResponseBody> addToFavorites(@Header("Authorization") String authToken, @Path("photograph_id") int photograph_id);

 @DELETE("{photograph_id}/deleteFromFavourites")
 Call<ResponseBody> deleteFromFavourites(@Header("Authorization") String authToken, @Path("photograph_id") int photograph_id);

 @GET("favourites")
 Call<List<FavouriteResponse>> getFavourites(@Header("Authorization") String authToken);

 @POST("{photograph_id}/addLikeToPhoto")
 Call<ResponseBody> like(@Header("Authorization") String authToken, @Path("photograph_id") int photograph_id);

 @DELETE("{photograph_id}/deleteLikeFromPhoto")
 Call<ResponseBody> dislike(@Header("Authorization") String authToken, @Path("photograph_id") int photograph_id);

 @GET("{user_id}/getLikesByUserId")
 Call<List<FavouriteResponse>> getLikesByUserId(@Header("Authorization") String authToken, @Path("user_id") int user_id);

 @GET("{photograph_id}/getLikesByPhotographId")
 Call<String> getLikesByPhotographId(@Header("Authorization") String authToken, @Path("photograph_id") int photograph_id);

 @POST("{photograph_id}/addCommentToPhotograph")
 Call<ResponseBody> commentPhotograph(@Header("Authorization") String authToken, @Body CommentRequest comment, @Path("photograph_id") int photograph_id);

 @GET("{photograph_id}/getPhotographComments")
 @Headers({
         "Accept: identity"})
 Call<List<Comments>> getCommentsByPhotographId(@Header("Authorization") String authToken, @Path("photograph_id") int photograph_id);

 @GET("getFeedPhotographs")
 Call<List<Photographs>> getFeedPhotographs(@Header("Authorization") String authToken);
}

