package com.thegreatgatsvim.nasa_april_2017_android20.util;

import android.graphics.Picture;

import com.thegreatgatsvim.nasa_april_2017_android20.models.Recycle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface UtilService {
    @GET("picture")
    Call<List<Recycle>> getPictures();

    @POST("picture")
    Call<Recycle> postImage(@Body Picture user);
}