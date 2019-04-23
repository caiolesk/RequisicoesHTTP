package com.whatsapp.caiocavallari.requisicoeshttp.api;

import com.whatsapp.caiocavallari.requisicoeshttp.model.Foto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {

    @GET("/photos")
    Call<List<Foto>> recuperarFotos();

    @GET("/posts")
    Call<List<Foto>> recuperarPostagens();
}
