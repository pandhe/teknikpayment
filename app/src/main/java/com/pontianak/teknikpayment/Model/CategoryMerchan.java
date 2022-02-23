package com.pontianak.teknikpayment.Model;

import com.google.gson.annotations.SerializedName;

public class CategoryMerchan {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("tipe")
    public String tipe;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
}
