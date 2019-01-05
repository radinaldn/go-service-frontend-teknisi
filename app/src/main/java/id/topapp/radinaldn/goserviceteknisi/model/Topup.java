package id.topapp.radinaldn.goserviceteknisi.model;

/**
 * Created by radinaldn on 31/12/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Topup {

    @SerializedName("nik")
    @Expose
    private String nik;
    @SerializedName("nominal")
    @Expose
    private String nominal;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("id_topup")
    @Expose
    private Integer idTopup;

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getIdTopup() {
        return idTopup;
    }

    public void setIdTopup(Integer idTopup) {
        this.idTopup = idTopup;
    }

}
