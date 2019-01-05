package id.topapp.radinaldn.goserviceteknisi.response;

/**
 * Created by radinaldn on 29/12/18.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.topapp.radinaldn.goserviceteknisi.model.Pemesanan;

public class ResponseViewPemesanan {

    @SerializedName("master")
    @Expose
    private List<Pemesanan> pemesanans = null;

    public List<Pemesanan> getPemesanans() {
        return pemesanans;
    }

    public void setMaster(List<Pemesanan> pemesanans) {
        this.pemesanans = pemesanans;
    }

}