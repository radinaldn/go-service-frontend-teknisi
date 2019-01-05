package id.topapp.radinaldn.goserviceteknisi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuhart.stepview.StepView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.topapp.radinaldn.goserviceteknisi.R;
import id.topapp.radinaldn.goserviceteknisi.config.ServerConfig;
import id.topapp.radinaldn.goserviceteknisi.model.Pemesanan;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseViewPemesanan;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiClient;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiInterface;
import id.topapp.radinaldn.goserviceteknisi.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemesananActivity extends AppCompatActivity {


    private static final String TAG_ID_PEMESANAN = "id_pemesanan";
    private static final String TAG = DetailPemesananActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private ImageView iv_foto;
    public static final String TAG_NIP = "nip";
    public static final String TAG_IMEI = "imei";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_FOTO = "foto";
    public static final String TAG_JK = "jk";
    private LinearLayout llKet;
    private String id_pemesanan, kategori, keluhan, proses, kategoriBayar, tanggal, fotoPemesanan, fotoTeknisi, namaTeknisi, alamatTeknisi, hpTeknisi, biaya, ket;
    private int intProses =0;
    private TextView tvKategori, tvKeluhan, tvKategoriBayar, tvTanggal, tvDeskripsiTeknisi, tvBiaya, tvKet;
    private CircleImageView civTeknisi;
    AlertDialog.Builder alertDialogBuilder;
    LayoutInflater inflater;
    View dialogView;
    ApiInterface apiService;
    StepView svProses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llKet = findViewById(R.id.llKet);
        tvKet = findViewById(R.id.tvKet);
        tvBiaya=  findViewById(R.id.tvBiaya);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        id_pemesanan = getIntent().getStringExtra(TAG_ID_PEMESANAN);
        Log.d(TAG, "onCreate: id_pemesanan = "+id_pemesanan);
        tvKategori = findViewById(R.id.tvKategori);
        tvKeluhan = findViewById(R.id.tvKeluhan);
        svProses = findViewById(R.id.svProses);
        tvKategoriBayar = findViewById(R.id.tvKategoriBayar);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvDeskripsiTeknisi = findViewById(R.id.tvDeskripsiTeknisi);
        civTeknisi = findViewById(R.id.civFotoTeknisi);
        iv_foto = findViewById(R.id.iv_foto);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOrderankuActivity();
            }
        });

        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);



        apiService.pemesananView(id_pemesanan).enqueue(new Callback<ResponseViewPemesanan>() {
            @Override
            public void onResponse(Call<ResponseViewPemesanan> call, Response<ResponseViewPemesanan> response) {
                System.out.println("masuk ke onResponse");
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: "+response.body().toString());
                    if (response.body().getPemesanans().size()>0){
                        Pemesanan pemesanan = response.body().getPemesanans().get(0);
                        kategori = pemesanan.getJenisServis();
                        keluhan = pemesanan.getKeluhan();
                        proses = pemesanan.getProses();
                        kategoriBayar = pemesanan.getKategoriBayar();
                        tanggal = pemesanan.getCreatedAt();
                        fotoTeknisi = pemesanan.getFoto();
                        namaTeknisi = pemesanan.getNama();
                        alamatTeknisi = pemesanan.getAlamat();
                        hpTeknisi = pemesanan.getNoHp();
                        ket = pemesanan.getKet();
                         // convert format saldo into rupiah

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        biaya = formatRupiah.format(Double.parseDouble(pemesanan.getBiaya()));


                        fotoTeknisi = pemesanan.getFoto();
                        collapsingToolbar.setTitle(namaTeknisi);
                        tvKategoriBayar.setText(kategoriBayar);

                        switch (proses){
                            case "Diproses":
                                intProses = 0;
                                fotoPemesanan = pemesanan.getFotoSebelum();
                                llKet.setVisibility(View.GONE);
                                break;
                            case "Selesai":
                                intProses = 2;
                                fotoPemesanan = pemesanan.getFotoSesudah();
                                tvKategoriBayar.setText(kategoriBayar+" senilai "+biaya);
                                llKet.setVisibility(View.VISIBLE);
                                tvKet.setText(ket);

                                break;
                            case "Dibayar":
                                intProses = 3;
                                fotoPemesanan = pemesanan.getFotoSesudah();
                                tvKategoriBayar.setText(kategoriBayar+" senilai "+biaya);
                                llKet.setVisibility(View.VISIBLE);
                                tvKet.setText(ket);
                                break;
                        }

                        tvKategori.setText(kategori);
                        tvKeluhan.setText(keluhan);

                        tvTanggal.setText(tanggal);
                        tvDeskripsiTeknisi.setText(namaTeknisi+"\n"+alamatTeknisi+"\n"+hpTeknisi);

                        Log.d(TAG, "intProses : "+intProses);
                        svProses.getState()
                                .selectedTextColor(ContextCompat.getColor(DetailPemesananActivity.this, R.color.colorAccent))
                                .animationType(StepView.ANIMATION_CIRCLE)
                                .selectedCircleColor(ContextCompat.getColor(DetailPemesananActivity.this, R.color.colorAccent))
                                .selectedCircleRadius(getResources().getDimensionPixelSize(R.dimen.dp14))
                                .selectedStepNumberColor(ContextCompat.getColor(DetailPemesananActivity.this, R.color.colorWhite))
                                // You should specify only stepsNumber or steps array of strings.
                                // In case you specify both steps array is chosen.
                                .steps(new ArrayList<String>() {{
                                    add("Diproses");
                                    add("Selesai");
                                    add("Dibayar");
                                }})
                                // You should specify only steps number or steps array of strings.
                                // In case you specify both steps array is chosen.
                                .stepsNumber(0)
                                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                                .stepLineWidth(getResources().getDimensionPixelSize(R.dimen.dp1))
                                .textSize(getResources().getDimensionPixelSize(R.dimen.sp14))
                                .stepNumberTextSize(getResources().getDimensionPixelSize(R.dimen.sp16))

                                // other state methods are equal to the corresponding xml attributes
                                .commit();

                        svProses.go(intProses-1, true);
                        svProses.done(false);

                        Log.d(TAG, "Load foto : "+ServerConfig.FOTO_PEMESANAN_PATH+fotoPemesanan);
                        Picasso.with(DetailPemesananActivity.this)
                                .load(ServerConfig.FOTO_PEMESANAN_PATH+fotoPemesanan)
                                .into(iv_foto);

                        Log.d(TAG, "Load foto : "+ServerConfig.FOTO_PEMESANAN_PATH+fotoTeknisi);
                        Picasso.with(DetailPemesananActivity.this)
                                .load(ServerConfig.MASYARAKAT_PROFIL_PATH+fotoTeknisi)
                                .into(civTeknisi);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseViewPemesanan> call, Throwable t) {
                t.printStackTrace();
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void goToOrderankuActivity(){
        Intent intent = new Intent(DetailPemesananActivity.this, OrderankuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}
