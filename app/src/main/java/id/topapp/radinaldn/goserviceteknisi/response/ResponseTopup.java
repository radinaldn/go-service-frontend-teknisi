package id.topapp.radinaldn.goserviceteknisi.response;

/**
 * Created by radinaldn on 30/12/18.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.topapp.radinaldn.goserviceteknisi.model.Saldo;

public class ResponseTopup {

    @SerializedName("master")
    @Expose
    private List<Saldo> saldos = null;

    public List<Saldo> getSaldos() {
        return saldos;
    }

    public void setSaldos(List<Saldo> saldos) {
        this.saldos = saldos;
    }

}