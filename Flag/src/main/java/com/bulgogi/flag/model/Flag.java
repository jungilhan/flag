package com.bulgogi.flag.model;

public class Flag {
    private String country;
    private String thumbUri;
    private String flagUri;

    private Flag(String country, String thumbUri, String flagUri) {
        this.country = country;
        this.thumbUri = thumbUri;
        this.flagUri = flagUri;
    }

    public static Flag valueOf(String country, String uri, String flagUri) {
        return new Flag(country, uri, flagUri);
    }

    public String getCountry() {
        return country;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public String getFlagUri() {
        return flagUri;
    }
}
