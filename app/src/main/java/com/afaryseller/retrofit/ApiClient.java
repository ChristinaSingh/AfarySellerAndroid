package com.afaryseller.retrofit;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static AfarySeller apiInterface = null;

    public static Retrofit getClient(Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(10, TimeUnit.SECONDS) // write timeout
                .readTimeout(10, TimeUnit.SECONDS); // read timeout

        retrofit = new Retrofit.Builder()
               // .baseUrl(Constant.BASE_URL)
                .baseUrl(Constant.BASE_URL_TEST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


    public static Retrofit getClient1(Context context) {
         Retrofit retrofit = null;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(10, TimeUnit.SECONDS) // write timeout
                .readTimeout(10, TimeUnit.SECONDS); // read timeout

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL_OTHER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


    public static AfarySeller loadInterface(Context context) {
        if (apiInterface == null) {
            apiInterface = ApiClient.getClient(context).create(AfarySeller.class);
        }
        return apiInterface;
    }
}