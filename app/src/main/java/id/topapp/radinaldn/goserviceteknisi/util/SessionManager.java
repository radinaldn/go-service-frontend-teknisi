package id.topapp.radinaldn.goserviceteknisi.util;

/**
 * Created by radinaldn on 22/12/18.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import java.util.HashMap;

import id.topapp.radinaldn.goserviceteknisi.model.Dashboard;
import id.topapp.radinaldn.goserviceteknisi.model.Teknisi;

/**
 * Created by radinaldn on 03/07/18.
 */

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context _context;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String ID_TEKNISI = "id_teknisi";
    public static final String EMAIL = "email";
    public static final String NAMA_TOKO= "nama_toko";
    public static final String NAMA_PEMILIK = "nama_pemilik";
    public static final String NIK_PEMILIK = "nik_pemilik";
    public static final String LAYANAN = "layanan";
    public static final String ALAMAT = "alamat";
    public static final String NO_HP = "no_hp";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String SIU = "siu";
    public static final String FOTO = "foto";
    public static final String STATUS_AKUN = "status_akun";
    public static final String SALDO = "saldo";
    public static final String TOTAL_RATING = "total_rating";
    public static final String JUMLAH_PEMESANAN = "jumlah_pemesanan";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";


    public static final String TOTAL_DIPROSES = "total_diproses";
    public static final String TOTAL_SELSESAI = "total_selesai";
    public static final String TOTAL_DIBAYAR = "total_dibayar";

    public static final String HAS_LAST_LOCATION = "hasLastLocation";
    public static final String LAST_LOCATED = "lastLocated";

    public static final String SHARE_LOC_IS_ON = "shareLocIsOn";

    public Context get_context(){
        return _context;
    }

    // constructor
    public SessionManager(Context context){
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(Teknisi teknisi){

        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(ID_TEKNISI, teknisi.getIdTeknisi());
        editor.putString(EMAIL, teknisi.getEmail());
        editor.putString(NAMA_TOKO, teknisi.getNamaToko());
        editor.putString(NAMA_PEMILIK, teknisi.getNamaPemilik());
        editor.putString(NIK_PEMILIK, teknisi.getNikPemilik());
        editor.putString(LAYANAN, teknisi.getLayanan());
        editor.putString(ALAMAT, teknisi.getAlamat());
        editor.putString(NO_HP, teknisi.getNoHp());
        editor.putString(LAT, teknisi.getLat());
        editor.putString(LNG, teknisi.getLng());
        editor.putString(FOTO, teknisi.getFoto());
        editor.putString(NO_HP, teknisi.getNoHp());
        editor.putString(SALDO, teknisi.getSaldo());
        editor.commit();

    }

    public void createDashboardSession(Dashboard dashboard){
        editor.putString(TOTAL_DIPROSES, dashboard.getDiproses());
        editor.putString(TOTAL_SELSESAI, dashboard.getSelesai());
        editor.putString(TOTAL_DIBAYAR, dashboard.getDibayar());
        editor.putString(SALDO, dashboard.getSaldo());
        editor.putString(TOTAL_RATING, dashboard.getTotal_rating());
        editor.putString(JUMLAH_PEMESANAN, dashboard.getJumlah_pemesanan());
        editor.commit();

    }
//
//    public void setStatusSwitchShareLoc(boolean status){
//        editor.putBoolean(SHARE_LOC_IS_ON, status);
//        editor.commit();
//    }
//
//    public boolean getStatusSwitchShareLoc(){
//        return sharedPreferences.getBoolean(SHARE_LOC_IS_ON, false);
//    }

    public HashMap<String, String> getTeknisiDetail(){
        HashMap<String,String> teknisi = new HashMap<>();
        teknisi.put(ID_TEKNISI, sharedPreferences.getString(ID_TEKNISI,null));
        teknisi.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        teknisi.put(NAMA_TOKO, sharedPreferences.getString(NAMA_TOKO,null));
        teknisi.put(NAMA_PEMILIK, sharedPreferences.getString(NAMA_PEMILIK,null));
        teknisi.put(NIK_PEMILIK, sharedPreferences.getString(NIK_PEMILIK,null));
        teknisi.put(LAYANAN, sharedPreferences.getString(LAYANAN,null));
        teknisi.put(ALAMAT, sharedPreferences.getString(ALAMAT,null));
        teknisi.put(NO_HP, sharedPreferences.getString(NO_HP,null));
        teknisi.put(LAT, sharedPreferences.getString(LAT,null));
        teknisi.put(LNG, sharedPreferences.getString(LNG,null));
        teknisi.put(SIU, sharedPreferences.getString(SIU,null));
        teknisi.put(FOTO, sharedPreferences.getString(FOTO,null));
        teknisi.put(STATUS_AKUN, sharedPreferences.getString(STATUS_AKUN,null));
        teknisi.put(SALDO, sharedPreferences.getString(SALDO,null));
        teknisi.put(CREATED_AT, sharedPreferences.getString(CREATED_AT,null));
        teknisi.put(UPDATED_AT, sharedPreferences.getString(UPDATED_AT,null));

        return teknisi;
    }

    public HashMap<String, String> getDashboardDetail(){
        HashMap<String, String> dashboard = new HashMap<>();
        dashboard.put(TOTAL_DIPROSES, sharedPreferences.getString(TOTAL_DIPROSES, null));
        dashboard.put(TOTAL_SELSESAI, sharedPreferences.getString(TOTAL_SELSESAI, null));
        dashboard.put(TOTAL_DIBAYAR, sharedPreferences.getString(TOTAL_DIBAYAR, null));
        dashboard.put(SALDO, sharedPreferences.getString(SALDO, null));
        dashboard.put(TOTAL_RATING, sharedPreferences.getString(TOTAL_RATING, null));
        dashboard.put(JUMLAH_PEMESANAN, sharedPreferences.getString(JUMLAH_PEMESANAN, null));

        return dashboard;
    }

    public void logoutTeknisi(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public boolean hasLastLocation(){
        return sharedPreferences.getBoolean(HAS_LAST_LOCATION, false);
    }
}
