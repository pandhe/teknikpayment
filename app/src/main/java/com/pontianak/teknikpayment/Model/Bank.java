package com.pontianak.teknikpayment.Model;

import com.google.gson.annotations.SerializedName;

public class Bank {
    @SerializedName("id")
    public int id;
    @SerializedName("slug_pengguna")
    public String slug_pengguna;
    @SerializedName("bank_name")
    public String bank_name;
    @SerializedName("bank_account")
    public String bank_account;
    @SerializedName("bank_placeholder")
    public String bank_placeholder;
    @SerializedName("tipe")
    public String tipe;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;

}
