package id.topapp.radinaldn.goserviceteknisi.model;

/**
 * Created by radinaldn on 06/01/19.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard {

    @SerializedName("diproses")
    @Expose
    private String diproses;
    @SerializedName("selesai")
    @Expose
    private String selesai;
    @SerializedName("dibayar")
    @Expose
    private String dibayar;
    @SerializedName("saldo")
    @Expose
    private String saldo;

    public String getDiproses() {
        return diproses;
    }

    public void setDiproses(String diproses) {
        this.diproses = diproses;
    }

    public String getSelesai() {
        return selesai;
    }

    public void setSelesai(String selesai) {
        this.selesai = selesai;
    }

    public String getDibayar() {
        return dibayar;
    }

    public void setDibayar(String dibayar) {
        this.dibayar = dibayar;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

}