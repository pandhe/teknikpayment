package com.pontianak.teknikpayment.Model;

import com.google.gson.annotations.SerializedName;

public class DetailTagihan {
    @SerializedName("periode")
    public String periode;
    @SerializedName("nilai_tagihan")
    public String nilai_tagihan;
    @SerializedName("admin")
    public String admin;
    @SerializedName("denda")
    public String denda;
}
