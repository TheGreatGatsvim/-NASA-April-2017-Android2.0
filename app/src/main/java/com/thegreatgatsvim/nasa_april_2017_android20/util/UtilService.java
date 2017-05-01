package com.thegreatgatsvim.nasa_april_2017_android20.util;

import android.graphics.Picture;

import com.thegreatgatsvim.nasa_april_2017_android20.models.Recycle;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface UtilService {
    @GET("picture/")
    Call<List<Recycle>> getPictures();

    @Multipart
    @POST("picture/")
    Call<Recycle> postImage(@Part MultipartBody.Part file);
}