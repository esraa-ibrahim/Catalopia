package com.me.catalopia.connection;

import com.me.catalopia.models.Store;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Esraa on 1/13/2018.
 */

public class CatalopiaApiManager {
    private Retrofit retrofit;
    private CatalopiaService service;
    public CatalopiaApiManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.42.113:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(CatalopiaService.class);
    }

    public Call<List<Store>> getStoresCall() {
        Call<List<Store>> storesListCall = service.getStoresList();
        return storesListCall;
    }
}
