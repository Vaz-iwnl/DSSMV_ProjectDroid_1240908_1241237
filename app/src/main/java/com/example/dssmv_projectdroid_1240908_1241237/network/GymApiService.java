package com.example.dssmv_projectdroid_1240908_1241237.network;

import com.example.dssmv_projectdroid_1240908_1241237.data.model.Desporto;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Utilizador;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GymApiService {

    // ==================== DESPORTOS ====================
    @GET("desportos")
    Call<List<Desporto>> getDesportos();

    @GET("desportos/{id}")
    Call<Desporto> getDesporto(@Path("id") String id);

    @POST("desportos")
    Call<Desporto> criarDesporto(@Body Desporto desporto);

    @PUT("desportos/{id}")
    Call<Desporto> atualizarDesporto(@Path("id") String id, @Body Desporto desporto);

    @DELETE("desportos/{id}")
    Call<Void> apagarDesporto(@Path("id") String id);


    // ==================== UTILIZADORES ====================
    @GET("users")
    Call<List<Utilizador>> getUtilizadores();

    @GET("users")
    Call<List<Utilizador>> fazerLogin(@Query("q") String query);

    @GET("users/{id}")
    Call<Utilizador> getUtilizador(@Path("id") String id);

    @POST("users")
    Call<Utilizador> criarUtilizador(@Body Utilizador utilizador);

    @PUT("users/{id}")
    Call<Utilizador> atualizarUtilizador(@Path("id") String id, @Body Utilizador utilizador);

    @DELETE("users/{id}")
    Call<Void> apagarUtilizador(@Path("id") String id);
}