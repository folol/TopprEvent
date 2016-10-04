package com.topprevents.android;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by devansh on 9/24/16.
 */
public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl("https://hackerearth.0x10.info/api/")
                    .addConverterFactory(GsonConverterFactory.create());





    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();





    public static <S> S createService(Class<S> serviceClass) {


        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().clear();
        httpClient.addInterceptor(logging);

//        httpClient.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request original = chain.request();
//
//                Request.Builder requestBuilder = original.newBuilder()
//                        .method(original.method(), original.body());
//
//
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });

        httpClient.retryOnConnectionFailure(true);
        httpClient.connectTimeout(20, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

}
