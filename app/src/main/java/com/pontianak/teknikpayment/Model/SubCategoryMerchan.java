package com.pontianak.teknikpayment.Model;

import com.google.gson.annotations.SerializedName;

public class SubCategoryMerchan {
    @SerializedName("id")
    public int id;
    @SerializedName("cat_id")
    public int cat_id;
    @SerializedName("name")
    public String name;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
    @SerializedName("category")
    public CategoryMerchan category;
    @SerializedName("logo")
    public String logo;
}
