package com.dgsl.utility_package.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://freecurrencyapi.net/api/v3/";//TODO: REPLACE WITH SERVER ADDRESS php/nodejs backend
    private static Retrofit retrofit = null;
    public static String apiKey = "QlQJmCpvK73IBz1gT5NANy2AVC0WdL9THDqNUyQN";
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
