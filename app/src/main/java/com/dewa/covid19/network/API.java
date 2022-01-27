package com.dewa.covid19.network;



import com.dewa.covid19.model.CountryLiveData;
import com.dewa.covid19.model.CountryStats;
import com.dewa.covid19.network.response.SummaryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface API {
    @GET("/summary")
    Call<SummaryResponse> GetSummary(
    );


    @GET
    Call<List<CountryLiveData>> getCountryLiveData(
            @Url String url
        );

    @GET
    Call<List<CountryStats>> getCountryStats(
            @Url String url
    );





}
