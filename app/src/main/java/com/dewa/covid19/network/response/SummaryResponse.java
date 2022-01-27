package com.dewa.covid19.network.response;

import com.dewa.covid19.model.Country;
import com.dewa.covid19.model.Global;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SummaryResponse {
    @SerializedName("ID")
    private String ID;

    @SerializedName("Message")
    private String Message;

    @SerializedName("Global")
    private Global Global;

    @SerializedName("Countries")
    private List<Country> Countries;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public com.dewa.covid19.model.Global getGlobal() {
        return Global;
    }

    public void setGlobal(com.dewa.covid19.model.Global global) {
        Global = global;
    }

    public List<Country> getCountries() {
        return Countries;
    }

    public void setCountries(List<Country> countries) {
        Countries = countries;
    }
}
