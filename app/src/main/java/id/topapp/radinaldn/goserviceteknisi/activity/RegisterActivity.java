//package id.topapp.radinaldn.goserviceteknisi.activity;
//
//import android.content.Intent;
//import android.support.design.widget.TextInputEditText;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import id.topapp.radinaldn.goserviceteknisi.R;
//import id.topapp.radinaldn.goserviceteknisi.response.ResponseRegister;
//import id.topapp.radinaldn.goserviceteknisi.rest.ApiClient;
//import id.topapp.radinaldn.goserviceteknisi.rest.ApiInterface;
//import id.topapp.radinaldn.goserviceteknisi.util.SessionManager;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    TextInputEditText etNik, etPassword1, etPassword2, etNama, etTempatLahir, etTanggalLahir, etAlamat, etPekerjaan, etNoHp;
//    RadioGroup rgJk, rgAgama, rgStatusKawin, rgKewarganegaraan;
//    Button btRegister;
//    ApiInterface apiService;
//    SessionManager sessionManager;
//    public static final String TAG = RegisterActivity.class.getSimpleName();
//
//    String [] jenis_kelamins = {"L", "P"};
//    String [] agamas = {"Islam", "Kristen", "Katholik", "Budha", "Hindu", "Lainnya"};
//    String [] status_kawins = {"Kawin", "Belum Kawin"};
//    String [] kewarganegaraans = {"WNI", "WNA"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        apiService = ApiClient.getClient().create(ApiInterface.class);
//
//        etNik = findViewById(R.id.etnik);
//        etPassword1 = findViewById(R.id.etpassword);
//        etPassword2 = findViewById(R.id.etpassword2);
//        etNama = findViewById(R.id.etnama);
//        etTempatLahir = findViewById(R.id.ettempatlahir);
//        etTanggalLahir = findViewById(R.id.ettgllahir);
//        etAlamat = findViewById(R.id.etalamat);
//        etPekerjaan = findViewById(R.id.etpekerjaan);
//        etNoHp = findViewById(R.id.ethp);
//        rgJk = findViewById(R.id.rgjk);
//        rgAgama = findViewById(R.id.rgagama);
//        rgStatusKawin = findViewById(R.id.rgstatuskawin);
//        rgKewarganegaraan = findViewById(R.id.rgkwn);
//
//        btRegister = findViewById(R.id.btRegister);
//
//        btRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actionRegister();
//            }
//        });
//    }
//
//    private void actionRegister() {
//        final String nik = etNik.getText().toString();
//        final String password_1 = etPassword1.getText().toString();
//        final String password_2 = etPassword2.getText().toString();
//        final String nama = etNama.getText().toString();
//        final String tempat_lahir = etTempatLahir.getText().toString();
//        final String tanggal_lahir = etTanggalLahir.getText().toString();
//        final String alamat = etAlamat.getText().toString();
//        final String pekerjaan = etPekerjaan.getText().toString();
//        final String no_hp = etNoHp.getText().toString();
//        final String jk = jenis_kelamins[rgJk.indexOfChild(rgJk.findViewById(rgJk.getCheckedRadioButtonId()))];
//        final String agama = agamas[rgAgama.indexOfChild(rgAgama.findViewById(rgAgama.getCheckedRadioButtonId()))];
//        final String status_kawin = status_kawins[rgStatusKawin.indexOfChild(rgStatusKawin.findViewById(rgStatusKawin.getCheckedRadioButtonId()))];
//        final String kewarganegaraan = kewarganegaraans[rgKewarganegaraan.indexOfChild(rgKewarganegaraan.findViewById(rgKewarganegaraan.getCheckedRadioButtonId()))];
//        final String foto = "Dummy.jpg";
//
//        apiService.register(nik, password_1, password_2, nama, tempat_lahir, tanggal_lahir, jk, alamat, agama, status_kawin, pekerjaan, kewarganegaraan, foto, no_hp).enqueue(new Callback<ResponseRegister>() {
//            @Override
//            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
//                Log.d(TAG, "onResponse: " +
//                        "\nnik : "+nik+
//                        "\npassword_1 : "+password_1+
//                        "\npassword_2 : "+password_2+
//                        "\nnama : "+nama+
//                        "\ntempat_lahir : "+tempat_lahir+
//                        "\ntanggal_lahir : "+tanggal_lahir+
//                        "\njk : "+jk+
//                        "\nalamat : "+alamat+
//                        "\nagama : "+agama+
//                        "\nstatus_kawin : "+status_kawin+
//                        "\npekerjaan : "+pekerjaan+
//                        "\nkewarganegaraan : "+kewarganegaraan+
//                        "\nfoto : "+foto+
//                        "\nno_hp : "+no_hp
//                );
//
//                if (response.isSuccessful()){
//                    Log.d(TAG, "onResponse: Dapat terhubung ke server");
//                    Log.d(TAG, "onResponse: " +response.body().getMessage());
//
//                    if (response.body().getCode() == 200){
//                        Toast.makeText(getApplicationContext(), response.body().getMessage()+"\nSIlahkan login menggunakan NIK dan Password yang sudah dibuat.", Toast.LENGTH_LONG).show();
//                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(i);
//                        finish();
//                    } else {
//                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Registrasi gagal", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseRegister> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//
//    }
//}
