package com.pontianak.teknikpayment.Model;

import com.google.gson.annotations.SerializedName;

public class Berita {
    @SerializedName("title")
    public String title;
    @SerializedName("link")
    public String link;

    public Berita(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
