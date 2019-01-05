package id.topapp.radinaldn.goserviceteknisi.response;

/**
 * Created by radinaldn on 28/12/18.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.topapp.radinaldn.goserviceteknisi.model.Teknisi;

public class ResponseFindTeknisi {

    @SerializedName("master")
    @Expose
    private List<Teknisi> teknisi = null;

    public List<Teknisi> getTeknisi() {
        return teknisi;
    }

    public void setTeknisi(List<Teknisi> teknisi) {
        this.teknisi = teknisi;
    }

}