package id.topapp.radinaldn.goserviceteknisi.activity;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import id.topapp.radinaldn.goserviceteknisi.R;
import id.topapp.radinaldn.goserviceteknisi.model.Masyarakat;
import id.topapp.radinaldn.goserviceteknisi.model.Teknisi;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseLogin;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiClient;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiInterface;
import id.topapp.radinaldn.goserviceteknisi.util.AbsRuntimePermission;
import id.topapp.radinaldn.goserviceteknisi.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AbsRuntimePermission {

    private static final int REQUEST_PERMISSION = 10;
    Button btLogin, btRegister;
    TextInputEditText etEmail, etPassword;
    ApiInterface apiService;
    SessionManager sessionManager;
    public static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //request permission here
        requestAppPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE},
                R.string.msg,REQUEST_PERMISSION);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        etEmail = findViewById(R.id.etemail);
        etPassword = findViewById(R.id.etpassword);

        if(sessionManager.isLoggedIn()){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            finish();
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString()!=null && etPassword.getText().toString()!=null){
                    actionLogin(etEmail.getText().toString(), etPassword.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Kolom NIK dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
            }
        });

    }

    @Override
    public void onPermissionGranted(int requestcode) {
        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
    }

    private void actionLogin(String nik, String password) {

        Log.d(TAG, "actionLogin: nik : "+nik+", password : "+password);
        
        apiService.login(nik, password).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful()){
                    if (response.body().getCode() == 200){
                        Log.d(TAG, "onResponse: Dapat terhubung ke server");
                        Log.d(TAG, "onResponse: code : " +response.body().getCode()+", message : "+response.body().getMessage());

                        Teknisi teknisi = response.body().getTeknisi();
                        sessionManager.createLoginSession(teknisi);

                        Log.d(TAG, "onResponse: dapat data masyarakat");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        finish();
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Selamat datang "+teknisi.getNamaPemilik(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal konek ke server", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: "+ t.getLocalizedMessage());
            }
        });
    }
}
