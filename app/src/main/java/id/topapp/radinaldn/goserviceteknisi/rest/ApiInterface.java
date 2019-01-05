package id.topapp.radinaldn.goserviceteknisi.rest;

import id.topapp.radinaldn.goserviceteknisi.response.ResponseBayarDenganSaldo;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseFindTeknisi;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseLogin;
import id.topapp.radinaldn.goserviceteknisi.response.ResponsePemesanan;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseRegister;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseTopup;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseTopupAdd;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseViewDataDashboard;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseViewPemesanan;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseViewSaldo;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by radinaldn on 22/12/18.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("teknisi/login")
    Call<ResponseLogin> login(
            @Field("nik") String nik,
            @Field("password") String password
    );

//    @FormUrlEncoded
//    @POST("masyarakat/register")
//    Call<ResponseRegister> register(
//            @Field("nik") String nik,
//            @Field("password_1") String password_1,
//            @Field("password_2") String password_2,
//            @Field("nama") String nama,
//            @Field("tempat_lahir") String tempat_lahir,
//            @Field("tanggal_lahir") String tanggal_lahir,
//            @Field("jk") String jk,
//            @Field("alamat") String alamat,
//            @Field("agama") String agama,
//            @Field("status_kawin") String status_kawin,
//            @Field("pekerjaan") String pekerjaan,
//            @Field("kewarganegaraan") String kewarganegaraan,
//            @Field("foto") String foto,
//            @Field("no_hp") String no_hp
//    );

    @FormUrlEncoded
    @POST("pemesanan/update-by-teknisi")
    Call<ResponsePemesanan> pemesananUpdateByTeknisi(
            @Field("id_pemesanan") String id_pemesanan,
            @Field("ket") String ket,
            @Field("biaya") String biaya,
            @Field("proses") String proses,
            @Field("foto_sesudah") String foto_sesudah
    );

    @GET("teknisi/find-nearby")
    Call<ResponseFindTeknisi> teknisiFindNearby(
            @Query("myLat") double myLat,
            @Query("myLng") double myLng,
            @Query("jarak") double jarak
    );

    @GET("pemesanan/view-all-by-id-teknisi-and-proses")
    Call<ResponseViewPemesanan> pemesananViewAllByIdTeknisiAndProses(
            @Query("id_teknisi") String id_teknisi,
            @Query("proses") String proses
    );

    @GET("pemesanan/view-by-teknisi")
    Call<ResponseViewPemesanan> pemesananView(
            @Query("id_pemesanan") String id_pemesanan
    );


    @GET("topup/view-all-by-nik")
    Call<ResponseTopup> topupViewAllByNik(
            @Query("nik") String nik
    );

    @FormUrlEncoded
    @POST("topup/add")
    Call<ResponseTopupAdd> topupAdd(
            @Field("nik") String nik,
            @Field("nominal") String nominal,
            @Field("foto") String foto
    );

    @GET("teknisi/view-saldo")
    Call<ResponseViewSaldo> teknisiViewSaldo(
            @Query("id_teknisi") String id_teknisi
    );

    @FormUrlEncoded
    @POST("pemesanan/update-proses")
    Call<ResponsePemesanan> pemesananUpdateProses(
            @Field("id_pemesanan") String id_pemesanan,
            @Field("proses") String proses
    );

    @GET("pemesanan/view-dashboard-data-by-id-teknisi")
    Call<ResponseViewDataDashboard> pemesananViewDashboardData(
            @Query("id_teknisi") String id_teknisi
    );

}
