package id.topapp.radinaldn.goserviceteknisi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import id.topapp.radinaldn.goserviceteknisi.R;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseViewDataDashboard;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiClient;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiInterface;
import id.topapp.radinaldn.goserviceteknisi.util.ConnectionDetector;
import id.topapp.radinaldn.goserviceteknisi.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static final String TAG_JENIS_SERVIS = "jenis_servis";

    // init TAG servis
    private static final String TAG_TV = "tv";
    private static final String TAG_ID_TEKNISI = "id_teknisi";
    private static final String TAG_KULKAS = "kulkas";
    private static final String TAG_AC = "ac";
    private static final String TAG_MESIN_CUCI = "mesin cuci";
    TextView tvDiproses, tvSelesai, tvDibayar, tvSaldo;
    private ConnectionDetector cd;
    ApiInterface apiService;
    private SessionManager sessionManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_orderanku:


                    Intent intent = new Intent(MainActivity.this, OrderankuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_profil:

                    Intent intent3 = new Intent(MainActivity.this, ProfilActivity.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent3);
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init for push notification w/ pusher
        PushNotifications.start(getApplicationContext(), "825913f1-e596-46cd-a0b8-22d2b6535c3d");
        PushNotifications.subscribe(sessionManager.getTeknisiDetail().get(SessionManager.ID_TEKNISI));
        
        cd = new ConnectionDetector(this);
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        tvDiproses = findViewById(R.id.tvDiproses);
        tvSelesai = findViewById(R.id.tvSelesai);
        tvDibayar = findViewById(R.id.tvDibayar);
        tvSaldo = findViewById(R.id.tvSaldo);
        
        if (cd.isConnectingToInternet()){
            loadDataFromApi();
        } else {
            loadDataFromSession();
        }
    }

    private void loadDataFromSession() {
        System.out.println("Saldo from session : "+sessionManager.getDashboardDetail().get(SessionManager.TOTAL_DIPROSES));
        tvDiproses.setText(sessionManager.getDashboardDetail().get(SessionManager.TOTAL_DIPROSES));
        tvSelesai.setText(sessionManager.getDashboardDetail().get(SessionManager.TOTAL_SELSESAI));
        tvDibayar.setText(sessionManager.getDashboardDetail().get(SessionManager.TOTAL_DIBAYAR));
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String saldoFormatted = formatRupiah.format(Double.parseDouble(sessionManager.getDashboardDetail().get(SessionManager.SALDO)));
        Log.d("TAG", "saldo: "+saldoFormatted);

        tvSaldo.setText(saldoFormatted);
    }

    private void loadDataFromApi() {
        apiService.pemesananViewDashboardData(sessionManager.getTeknisiDetail().get(TAG_ID_TEKNISI)).enqueue(new Callback<ResponseViewDataDashboard>() {
            @Override
            public void onResponse(Call<ResponseViewDataDashboard> call, Response<ResponseViewDataDashboard> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 200) {
                        sessionManager.createDashboardSession(response.body().getData());

                        tvDiproses.setText(sessionManager.getDashboardDetail().get(SessionManager.TOTAL_DIPROSES));
                        tvSelesai.setText(sessionManager.getDashboardDetail().get(SessionManager.TOTAL_SELSESAI));
                        tvDibayar.setText(sessionManager.getDashboardDetail().get(SessionManager.TOTAL_DIBAYAR));

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        String saldoFormatted = formatRupiah.format(Double.parseDouble(sessionManager.getDashboardDetail().get(SessionManager.SALDO)));
                        Log.d("TAG", "saldo: "+saldoFormatted);

                        tvSaldo.setText(saldoFormatted);

                        System.out.println("Saldo from session : "+sessionManager.getDashboardDetail().get(SessionManager.TOTAL_DIPROSES));
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseViewDataDashboard> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }


    public void actionTarikSaldo(View view) {
        
    }

    @Override
    protected void onResume() {
        if (cd.isConnectingToInternet()){
            loadDataFromApi();
        } else {
            loadDataFromSession();
        }
        super.onResume();
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                Log.i("MainActivity", "A remote message was received while this activity is visible!");
            }
        });

    }
}
