package id.topapp.radinaldn.goserviceteknisi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.topapp.radinaldn.goserviceteknisi.R;
import id.topapp.radinaldn.goserviceteknisi.config.ServerConfig;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseViewSaldo;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiClient;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiInterface;
import id.topapp.radinaldn.goserviceteknisi.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {


    TextView tvNik, tvNama, tvToko, tvAlamat, tvLayanan, tvNoHp, tvSaldo;
    CircleImageView ivFoto;
    SessionManager sessionManager;
    ApiInterface apiService;
    ImageView star1, star2, star3, star4, star5;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_orderanku:


                    Intent intent = new Intent(ProfilActivity.this, OrderankuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.navigation_dashboard:

                    Intent intent2 = new Intent(ProfilActivity.this, MainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent2);
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.navigation_profil:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_profil);

        sessionManager = new SessionManager(this);
        tvNik = findViewById(R.id.tvNik);
        tvNama = findViewById(R.id.tvNama);
        tvToko = findViewById(R.id.tvToko);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvNoHp = findViewById(R.id.tvNoHp);
        tvSaldo = findViewById(R.id.tvSaldo);
        ivFoto = findViewById(R.id.ivFoto);
        tvLayanan = findViewById(R.id.tvLayanan);
        tvNoHp = findViewById(R.id.tvNoHp);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        

        tvNik.setText(sessionManager.getTeknisiDetail().get(SessionManager.NIK_PEMILIK));
        tvNama.setText(sessionManager.getTeknisiDetail().get(SessionManager.NAMA_PEMILIK));
        tvToko.setText(sessionManager.getTeknisiDetail().get(SessionManager.NAMA_TOKO));
        tvAlamat.setText(sessionManager.getTeknisiDetail().get(SessionManager.ALAMAT));
        tvNoHp.setText(sessionManager.getTeknisiDetail().get(SessionManager.NO_HP));
        tvSaldo.setText(sessionManager.getTeknisiDetail().get(SessionManager.SALDO));
        tvLayanan.setText(sessionManager.getTeknisiDetail().get(SessionManager.LAYANAN));
        tvNoHp.setText(sessionManager.getTeknisiDetail().get(SessionManager.NO_HP));

        // get user saldo
        getSaldoRealtime();
        // convert format saldo into rupiah


        Log.d("TAG", "foto: "+ServerConfig.TEKNISI_PROFIL_PATH+sessionManager.getTeknisiDetail().get(SessionManager.FOTO));
        Picasso.with(this)
                .load(ServerConfig.TEKNISI_PROFIL_PATH+sessionManager.getTeknisiDetail().get(SessionManager.FOTO))
                .error(R.drawable.dummy_ava)
                .centerCrop()
                .fit()
                .into(ivFoto);


        Double dblRating = Math.round(Double.parseDouble(sessionManager.getDashboardDetail().get(SessionManager.TOTAL_RATING)))/(Double.parseDouble(sessionManager.getDashboardDetail().get(SessionManager.JUMLAH_PEMESANAN)));
        int rating = dblRating.intValue();

        System.out.println("rating : "+rating);

        switch (rating){
            case 1:
                star1.setVisibility(View.VISIBLE);
                break;
            case 2:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                break;
            case 3:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                break;
            case 4:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                break;
            case 5 :
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                break;
            default:
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star5.setVisibility(View.GONE);
                break;
        }
    }

    private void getSaldoRealtime() {

        apiService.teknisiViewSaldo(sessionManager.getTeknisiDetail().get(SessionManager.ID_TEKNISI)).enqueue(new Callback<ResponseViewSaldo>() {
            @Override
            public void onResponse(Call<ResponseViewSaldo> call, Response<ResponseViewSaldo> response) {
                if (response.isSuccessful()){
                    System.out.println(response.toString());
                    System.out.println(response.body().toString());

                    if (response.body().getCode() == 200){
                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        String saldoFormatted = formatRupiah.format(Double.parseDouble(response.body().getSaldo().toString()));
                        Log.d("TAG", "saldo: "+saldoFormatted);

                        tvSaldo.setText("Saldo Anda:\n"+saldoFormatted);
                        Log.d("TAG", "tvSaldo: "+sessionManager.getTeknisiDetail().get("Saldo Anda:\n"+saldoFormatted));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseViewSaldo> call, Throwable t) {
t.printStackTrace();
            }
        });


    }

    private void goToMainActivity(){
        Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToMainActivity();
    }

    public void actionLogout(View view) {
        sessionManager.logoutTeknisi();
        Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}
