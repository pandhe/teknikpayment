package com.pontianak.teknikpayment.Model;

import com.google.gson.annotations.SerializedName;

public class Notifikasi {
    @SerializedName("id")
    public int id;
    @SerializedName("slug_pengguna")
    public String slug_pengguna;
    @SerializedName("token")
    public String token;
    @SerializedName("title")
    public String title;
    @SerializedName("message")
    public String message;
    @SerializedName("payload")
    public String payload;
    @SerializedName("response")
    public String response;
    @SerializedName("is_read")
    public int is_read;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
}
