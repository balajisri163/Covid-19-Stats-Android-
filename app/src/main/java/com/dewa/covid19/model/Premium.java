package com.dewa.covid19.model;

import java.util.List;

public class Premium {

    CountryStats CountryStats;

    public com.dewa.covid19.model.CountryStats getCountryStats() {
        return CountryStats;
    }

    public void setCountryStats(com.dewa.covid19.model.CountryStats countryStats) {
        CountryStats = countryStats;
    }

    @Override
    public String toString() {
        return "Premium{" +
                "CountryStats=" + CountryStats.getContinent() +
                '}';
    }
}
