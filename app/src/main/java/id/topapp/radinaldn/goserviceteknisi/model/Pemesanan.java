package id.topapp.radinaldn.goserviceteknisi.model;

/**
 * Created by radinaldn on 26/12/18.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pemesanan {

    @SerializedName("id_pemesanan")
    @Expose
    private String idPemesanan;
    @SerializedName("nik")
    @Expose
    private String nik;
    @SerializedName("id_teknisi")
    @Expose
    private String idTeknisi;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("keluhan")
    @Expose
    private String keluhan;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("jenis_servis")
    @Expose
    private String jenisServis;
    @SerializedName("biaya")
    @Expose
    private String biaya;
    @SerializedName("proses")
    @Expose
    private String proses;
    @SerializedName("kategori_bayar")
    @Expose
    private String kategoriBayar;
    @SerializedName("foto_sebelum")
    @Expose
    private String fotoSebelum;
    @SerializedName("foto_sesudah")
    @Expose
    private String fotoSesudah;
    @SerializedName("ket")
    @Expose
    private String ket;

    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("komentar_rating")
    @Expose
    private String komentar_rating;

    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("tempat_lahir")
    @Expose
    private String tempatLahir;
    @SerializedName("tanggal_lahir")
    @Expose
    private String tanggalLahir;
    @SerializedName("jk")
    @Expose
    private String jk;
    @SerializedName("agama")
    @Expose
    private String agama;
    @SerializedName("status_kawin")
    @Expose
    private String statusKawin;
    @SerializedName("pekerjaan")
    @Expose
    private String pekerjaan;
    @SerializedName("kewarganegaraan")
    @Expose
    private String kewarganegaraan;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("no_hp")
    @Expose
    private String noHp;
    @SerializedName("saldo")
    @Expose
    private String saldo;
    @SerializedName("total_rating")
    @Expose
    private int total_rating;
    @SerializedName("jumlah_pemesanan")
    @Expose
    private int jumlah_pemesanan;

    public String getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(String idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getIdTeknisi() {
        return idTeknisi;
    }

    public void setIdTeknisi(String idTeknisi) {
        this.idTeknisi = idTeknisi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getJenisServis() {
        return jenisServis;
    }

    public void setJenisServis(String jenisServis) {
        this.jenisServis = jenisServis;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getProses() {
        return proses;
    }

    public void setProses(String proses) {
        this.proses = proses;
    }

    public String getKategoriBayar() {
        return kategoriBayar;
    }

    public void setKategoriBayar(String kategoriBayar) {
        this.kategoriBayar = kategoriBayar;
    }

    public String getFotoSebelum() {
        return fotoSebelum;
    }

    public void setFotoSebelum(String fotoSebelum) {
        this.fotoSebelum = fotoSebelum;
    }

    public String getFotoSesudah() {
        return fotoSesudah;
    }

    public void setFotoSesudah(String fotoSesudah) {
        this.fotoSesudah = fotoSesudah;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }

    public String getStatusKawin() {
        return statusKawin;
    }

    public void setStatusKawin(String statusKawin) {
        this.statusKawin = statusKawin;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getKewarganegaraan() {
        return kewarganegaraan;
    }

    public void setKewarganegaraan(String kewarganegaraan) {
        this.kewarganegaraan = kewarganegaraan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getKomentar_rating() {
        return komentar_rating;
    }

    public void setKomentar_rating(String komentar_rating) {
        this.komentar_rating = komentar_rating;
    }

    public int getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(int total_rating) {
        this.total_rating = total_rating;
    }

    public int getJumlah_pemesanan() {
        return jumlah_pemesanan;
    }

    public void setJumlah_pemesanan(int jumlah_pemesanan) {
        this.jumlah_pemesanan = jumlah_pemesanan;
    }
}