package id.topapp.radinaldn.goserviceteknisi.response;

/**
 * Created by radinaldn on 01/01/19.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.topapp.radinaldn.goserviceteknisi.model.Masyarakat;
import id.topapp.radinaldn.goserviceteknisi.model.Pemesanan;
import id.topapp.radinaldn.goserviceteknisi.model.Teknisi;

public class ResponseBayarDenganSaldo {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("masyarakat")
    @Expose
    private Masyarakat masyarakat;
    @SerializedName("teknisi")
    @Expose
    private Teknisi teknisi;
    @SerializedName("pemesanan")
    @Expose
    private Pemesanan pemesanan;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Masyarakat getMasyarakat() {
        return masyarakat;
    }

    public void setMasyarakat(Masyarakat masyarakat) {
        this.masyarakat = masyarakat;
    }

    public Teknisi getTeknisi() {
        return teknisi;
    }

    public void setTeknisi(Teknisi teknisi) {
        this.teknisi = teknisi;
    }

    public Pemesanan getPemesanan() {
        return pemesanan;
    }

    public void setPemesanan(Pemesanan pemesanan) {
        this.pemesanan = pemesanan;
    }

}