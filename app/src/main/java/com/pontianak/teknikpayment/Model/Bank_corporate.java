package com.pontianak.teknikpayment.Model;

import com.google.gson.annotations.SerializedName;

public class Bank_corporate {
    @SerializedName("id")
    public int id;
    @SerializedName("bank_name")
    public String bank_name;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;
}
