package id.topapp.radinaldn.goserviceteknisi.response;

/**
 * Created by radinaldn on 31/12/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.topapp.radinaldn.goserviceteknisi.model.Topup;

public class ResponseTopupAdd {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Topup data;

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

    public Topup getTopup() {
        return data;
    }

    public void setTopup(Topup data) {
        this.data = data;
    }

}