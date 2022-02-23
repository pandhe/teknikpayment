package com.pontianak.teknikpayment.Model;

import com.google.gson.annotations.SerializedName;

public class PulsaOperator {
    @SerializedName("id")
    public float id;
    @SerializedName("subcat_id")
    public int subcat_id;
    @SerializedName("name")
    public String name;
    @SerializedName("price")
    public float price;
    @SerializedName("price_user")
    public float price_user;
    @SerializedName("code")
    public String code;
    @SerializedName("status")
    public String status;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
    @SerializedName("subcategory")
    public SubCategoryMerchan subcategory;



    public PulsaOperator(float id, int subcat_id, String name, String code) {
        this.id = id;
        this.subcat_id = subcat_id;
        this.name = name;
        this.code = code;
        this.price_user=0;
    }
    public PulsaOperator(float id, int subcat_id, String name, String code, String logo) {
        this.id = id;
        this.subcat_id = subcat_id;
        this.name = name;
        this.code = code;
        this.price_user=0;
    }
}

