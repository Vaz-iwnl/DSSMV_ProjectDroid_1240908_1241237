package com.example.dssmv_projectdroid_1240908_1241237.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://isepgym-24b2.restdb.io/rest/";
    private static final String API_KEY = "40f3a2b81ba8c91c64173112fc8405605d967";

    private static Retrofit retrofit = null;

    public static GymApiService getApiService() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); //regista o pedidos da api
            Interceptor apiKeyInterceptor = new Interceptor() { // Interceptor que injeta o cabeçalho 'x-apikey' obrigatório pelo RestDB
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request requestWithHeaders = originalRequest.newBuilder()
                            .header("x-apikey", API_KEY)
                            .header("Content-Type", "application/json")
                            .header("cache-control", "no-cache")
                            .build();
                    return chain.proceed(requestWithHeaders);
                }
            };
            // Cria o cliente HTTP com a autenticação
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(apiKeyInterceptor)
                    .addInterceptor(logging)
                    .build();

            // Criar a instância do Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(GymApiService.class);
    }
}