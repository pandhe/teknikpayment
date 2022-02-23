package com.pontianak.teknikpayment.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class History implements Parcelable {
    @SerializedName("refid")
    public String refid;
    @SerializedName("status")
    public String status;
    @SerializedName("tgl")
    public String tgl;
    @SerializedName("tgl_human")
    public String tgl_human;
    @SerializedName("jenis")
    public String jenis;
    @SerializedName("nominal")
    public int nominal;
    @SerializedName("nominal_text")
    public String nominal_text;
    @SerializedName("biaya")
    public int biaya;
    @SerializedName("biaya_text")
    public String biaya_text;
    @SerializedName("saldo_saat_transaksi")
    public int saldo_saat_transaksi;
    @SerializedName("saldo_saat_transaksi_text")
    public String saldo_saat_transaksi_text;
    @SerializedName("keterangan")
    public String keterangan;
    @SerializedName("detail_dflash")
    public String detail_dflash;

    public History(String refid, String status, String tgl, String tgl_human, String jenis, int nominal, String nominal_text, int biaya, String biaya_text, int saldo_saat_transaksi, String saldo_saat_transaksi_text, String keterangan, String detail_dflash) {
        this.refid = refid;
        this.status = status;
        this.tgl = tgl;
        this.tgl_human = tgl_human;
        this.jenis = jenis;
        this.nominal = nominal;
        this.nominal_text = nominal_text;
        this.biaya = biaya;
        this.biaya_text = biaya_text;
        this.saldo_saat_transaksi = saldo_saat_transaksi;
        this.saldo_saat_transaksi_text = saldo_saat_transaksi_text;
        this.keterangan = keterangan;
        this.detail_dflash = detail_dflash;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.refid);
        dest.writeString(this.status);
        dest.writeString(this.tgl);
        dest.writeString(this.tgl_human);
        dest.writeString(this.jenis);
        dest.writeInt(this.nominal);
        dest.writeString(this.nominal_text);
        dest.writeInt(this.biaya);
        dest.writeString(this.biaya_text);
        dest.writeInt(this.saldo_saat_transaksi);
        dest.writeString(this.saldo_saat_transaksi_text);
        dest.writeString(this.keterangan);
        dest.writeString(this.detail_dflash);
    }

    public History() {
    }

    protected History(Parcel in) {
        this.refid = in.readString();
        this.status = in.readString();
        this.tgl = in.readString();
        this.tgl_human = in.readString();
        this.jenis = in.readString();
        this.nominal = in.readInt();
        this.nominal_text = in.readString();
        this.biaya = in.readInt();
        this.biaya_text = in.readString();
        this.saldo_saat_transaksi = in.readInt();
        this.saldo_saat_transaksi_text = in.readString();
        this.keterangan = in.readString();
        this.detail_dflash = in.readString();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
}
