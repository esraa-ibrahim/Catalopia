package com.me.catalopia.connection;

import com.me.catalopia.models.Store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Esraa on 1/13/2018.
 */

public interface CatalopiaService {
    @GET("getStores")
    Call<List<Store>> getStoresList();
}
