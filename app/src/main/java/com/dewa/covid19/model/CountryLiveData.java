package com.dewa.covid19.model;

public class CountryLiveData {
    String Country;
            String CountryCode;
            String Province;
            String City;
            String CityCode;
            String Lat;
            String Lon;
            String Cases;
            String Status;
            String Date;

        public String getCountry() {
                return Country;
        }

        public void setCountry(String country) {
                Country = country;
        }

        public String getCountryCode() {
                return CountryCode;
        }

        public void setCountryCode(String countryCode) {
                CountryCode = countryCode;
        }

        public String getProvince() {
                return Province;
        }

        public void setProvince(String province) {
                Province = province;
        }

        public String getCity() {
                return City;
        }

        public void setCity(String city) {
                City = city;
        }

        public String getCityCode() {
                return CityCode;
        }

        public void setCityCode(String cityCode) {
                CityCode = cityCode;
        }

        public String getLat() {
                return Lat;
        }

        public void setLat(String lat) {
                Lat = lat;
        }

        public String getLon() {
                return Lon;
        }

        public void setLon(String lon) {
                Lon = lon;
        }

        public String getCases() {
                return Cases;
        }

        public void setCases(String cases) {
                Cases = cases;
        }

        public String getStatus() {
                return Status;
        }

        public void setStatus(String status) {
                Status = status;
        }

        public String getDate() {
                return Date;
        }

        public void setDate(String date) {
                Date = date;
        }
}
