package com.bulgogi.flag.model;

public class Flag {
    private String country;
    private String uri;

    private Flag(String country, String uri) {
        this.country = country;
        this.uri = uri;
    }

    public static Flag valueOf(String country, String uri) {
        return new Flag(country, uri);
    }

    public String getCountry() {
        return country;
    }

    public String getUri() {
        return uri;
    }
}
