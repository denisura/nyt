package com.github.denisura.nytseacher.data.network;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewYorkTimesService {
    private NewYorkTimesService() {
    }

    public static NewYorkTimesApi createNewYorkTimesService() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .baseUrl(NewYorkTimesApi.BASE_URL)
                .build()
                .create(NewYorkTimesApi.class);
    }
}
