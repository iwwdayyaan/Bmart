package com.bart.api.interfaces;


import com.bart.api.Responce;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by think360 on 18/04/17.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.UserSignUp> userSignUp(@Field("action") String action, @Field("name") String name,  @Field("phoneno") String phoneno,@Field("email") String email,
                                 @Field("password") String password,@Field("otp") String otp, @Field("paasport") String paasport, @Field("deviceid") String deviceid
                               );
    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.LoginUser> userLogin(@Field("action") String action, @Field("email") String email, @Field("password") String password, @Field("deviceid") String deviceid);

    @Multipart
    @POST("jason/jason.php")
    Call<Responce.Status> addArt(@Part("action") RequestBody action, @Part("userid") RequestBody userid, @Part  MultipartBody.Part artpic, @Part("qr_code") RequestBody qr_code,
                                 @Part("painter_name") RequestBody painter_name, @Part("owner_name") RequestBody owner_name, @Part("art_gallery_name") RequestBody art_gallery_name,
                                 @Part("dop") RequestBody dop, @Part("value") RequestBody value, @Part("art_type") RequestBody art_type);

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.Status> wallet(@Field("action") String action, @Field("purchaseid") String purchaseid, @Field("walletid") String walletid, @Field("name") String name,
                            @Field("phoneno") String phoneno,  @Field("purchase_value") String purchase_value);

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.History> history(@Field("action") String action, @Field("userid") String userid);

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.FeatchArt> featchArt(@Field("action") String action, @Field("userid") String userid);

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.FeatProfile> featProfile(@Field("action") String action, @Field("userid") String userid);

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.Status> updateUserProfile(@Field("action") String action, @Field("name") String name,  @Field("phoneno") String phoneno,@Field("email") String email,
                                         @Field("password") String password,@Field("otp") String otp, @Field("paasport") String paasport, @Field("userid") String userid
    );

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.Status> addOtp(@Field("action") String action, @Field("phoneno") String phoneno);

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.Status> confirmOtp(@Field("action") String action, @Field("phoneno") String phoneno, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("jason/jason.php")
    Call<Responce.Status> forgotPassword(@Field("action") String action, @Field("email") String email, @Field("phoneno") String phoneno);
   /* @GET("/data/2.5{movie_id}getDetails")
    Call <Responce.Status> getMovieDatils(@Path(value = "movie_id", encoded = true) String movieID);*/
}
