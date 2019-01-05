package id.topapp.radinaldn.goserviceteknisi.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.topapp.radinaldn.goserviceteknisi.R;
import id.topapp.radinaldn.goserviceteknisi.adapter.OrderankuViewPagerAdapter;
import id.topapp.radinaldn.goserviceteknisi.config.ServerConfig;
import id.topapp.radinaldn.goserviceteknisi.fragment.OrderankuFragment;
import id.topapp.radinaldn.goserviceteknisi.response.ResponsePemesanan;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseTopupAdd;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiClient;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiInterface;
import id.topapp.radinaldn.goserviceteknisi.util.ConnectionDetector;
import id.topapp.radinaldn.goserviceteknisi.util.HttpFileUpload;
import id.topapp.radinaldn.goserviceteknisi.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderankuActivity extends AppCompatActivity {

    private static final String TAG_ID_PEMESANAN = "id_pemesanan";
    private static final String TAG = OrderankuActivity.class.getSimpleName();
    private static String ID_PEMESANAN, NAMA_MASYARAKAT, KELUHAN;
    private TextView mTextMessage;
    ViewPager viewPager;
    TabLayout tabLayout;
    ApiInterface apiService;
    View parentLayout;

    AlertDialog.Builder alertDialogBuilder;
    LayoutInflater inflater;
    View dialogView;
    private ProgressDialog pDialog;
    ImageView ivFoto;

    File destFile;
    File file;
    private SimpleDateFormat dateFormatter;
    private Uri imageCaptureUri;
    Bitmap bmp;
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String IMAGE_DIRECTORY = "Go-Service";
    private Boolean upflag = false;
    String fname, finalPhotoName;
    ConnectionDetector cd;
    SessionManager sessionManager;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_orderanku:



                    return true;
                case R.id.navigation_dashboard:

                    Intent intent2 = new Intent(OrderankuActivity.this, MainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent2);
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.navigation_profil:

                    Intent intent3 = new Intent(OrderankuActivity.this, ProfilActivity.class);
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
        setContentView(R.layout.activity_orderanku);
        cd = new ConnectionDetector(this);
        dateFormatter = new SimpleDateFormat(
                DATE_FORMAT, Locale.US);

        file = new File(Environment.getExternalStorageDirectory()
                + "/" + IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }

        sessionManager = new SessionManager(this);
        parentLayout = findViewById(android.R.id.content);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_orderanku);

        apiService = ApiClient.getClient().create(ApiInterface.class);

//        Toolbar toolbar = findViewById(R.id.toolbar_kehadiran_dosen);
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToMainActivity();
//            }
//        });
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager_kehadiran_dosen);
        tabLayout = findViewById(R.id.tabs_kehadiran_dosen);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        OrderankuViewPagerAdapter adapter = new OrderankuViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(OrderankuFragment.newInstance("Diproses"), "Diproses");
        adapter.addFragment(OrderankuFragment.newInstance("Selesai"), "Selesai");
        adapter.addFragment(OrderankuFragment.newInstance("Dibayar"), "Dibayar");
        viewPager.setAdapter(adapter);
    }



    private void goToMainActivity(){
        Intent intent = new Intent(OrderankuActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    public void showSnackbarSaldo(){
        Snackbar.make(parentLayout, "Saldo anda tidak mencukupi!", Snackbar.LENGTH_LONG).setAction("Isi Saldo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderankuActivity.this, SaldoActivity.class);
                startActivity(i);
            }
        }).show();
    }

    public void actionUpdatePemesananByTeknisi(String id_pemesanan, String nama_masyarakat, String keluhan) {
        Toast.makeText(getApplicationContext(), "Ambil foto bukti pekerjaan telah selesai", Toast.LENGTH_SHORT).show();
        System.out.println("Nyalakan kamera");

        finalPhotoName = "Post_"+sessionManager.getTeknisiDetail().get(SessionManager.ID_TEKNISI)+"_" +dateFormatter.format(new Date()) + ".jpg";

        destFile = new File(file, finalPhotoName);
        imageCaptureUri = Uri.fromFile(destFile);

        ID_PEMESANAN = id_pemesanan;
        NAMA_MASYARAKAT = nama_masyarakat;
        KELUHAN = keluhan;
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        setResult(RESULT_OK, intentCamera); // tambahkan ini agar bisa put extra pada intent camera
        startActivityForResult(intentCamera, 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode){
                case 101:
                    if (resultCode == RESULT_OK){
                        upflag = true;
                        Log.d(TAG +". Pick camera image ", "Selected image uri path : "+imageCaptureUri);

                        bmp = decodeFile(destFile, finalPhotoName);

                        String id_pemesanan = ID_PEMESANAN;
                        String nama_masyarakat = NAMA_MASYARAKAT;
                        String keluhan = KELUHAN;
                        showFormUpdatePemesanan(bmp, id_pemesanan, nama_masyarakat, keluhan);


                    }
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFormUpdatePemesanan(Bitmap bmpFotoBukti, final String idPemesanan, String namaMasyarakat, String keluhan) {
        System.out.println("Tampilkan form update pemesanan");
        alertDialogBuilder = new AlertDialog.Builder(OrderankuActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_update_pemesanan, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setIcon(R.drawable.ic_mode_edit_black);
        alertDialogBuilder.setTitle(R.string.update_pemesanan);

        final EditText etKet = dialogView.findViewById(R.id.etKet);
        final EditText etBiaya = dialogView.findViewById(R.id.etBiaya);
        final TextView tvNama = dialogView.findViewById(R.id.tvNamaMasyarakat);

        tvNama.setText(namaMasyarakat);
        final TextView tvKeluhan = dialogView.findViewById(R.id.tvKeluhan);
        tvKeluhan.setText(keluhan);

        ivFoto = dialogView.findViewById(R.id.ivFoto);
        ivFoto.setImageBitmap(bmpFotoBukti);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        System.out.println("Klik simpan");
                        // save foto to lcoal storage and to server
                        if (destFile!=null){
                            saveFile(destFile);
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.belum_mengambil_gambar), Toast.LENGTH_SHORT).show();
                        }


                        final String id_pemesanan = idPemesanan;
                        String ket = etKet.getText().toString();
                        String biaya = etBiaya.getText().toString();
                        String foto = finalPhotoName;
                        String proses = "Selesai";
                        System.out.println("id_pemesanan : "+id_pemesanan);
                        System.out.println("ket : "+ket);
                        System.out.println("biaya : "+biaya);
                        System.out.println("foto : "+foto);
                        System.out.println("proses : "+proses);
                        apiService.pemesananUpdateByTeknisi(id_pemesanan, ket, biaya, proses, foto).enqueue(new Callback<ResponsePemesanan>() {
                            @Override
                            public void onResponse(Call<ResponsePemesanan> call, Response<ResponsePemesanan> response) {
                                Log.d(TAG, "onResponse: "+response.toString());
                                if (response.isSuccessful()){
                                    if (response.body().getCode() == 200){
                                        // Log.w("UpdatePemesanan", new Gson().toJson(response));
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();


                                        Intent i = new Intent(OrderankuActivity.this, DetailPemesananActivity.class);
                                        i.putExtra(TAG_ID_PEMESANAN, id_pemesanan);
                                        finish();
                                        startActivity(i);


/*You've to create a frame layout or any other layout with id inside your activity layout and then use that id in java*/



                                    } else {
                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Response unsuccessful : "+response.toString(), Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePemesanan> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }})



                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private Bitmap decodeFile(File f, String final_photo_name) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        fname = final_photo_name;
        destFile = new File(file, fname);

        return b;



    }

    // for saving image to local storage, method ini master untuk upload foto
    private void saveFile(File destination){
        if(destination.exists()) destination.delete();

        try{
            FileOutputStream out = new FileOutputStream(destFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();

            if (cd.isConnectingToInternet()){
                new UploadFoto().execute();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    class UploadFoto extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(OrderankuActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Mohon menunggu, sedang mengupload gambar..");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                // set your file path here
                FileInputStream fstrm = new FileInputStream(destFile);
                // set your server page url (and the title/description)
                HttpFileUpload hfu = new HttpFileUpload(ServerConfig.UPLOAD_FOTO_ENDPOINT, "ftitle", "fdescription", finalPhotoName);
                upflag = hfu.Send_Now(fstrm);

            } catch (FileNotFoundException e){
                // file not found
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (upflag){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_gambar_berhasil), Toast.LENGTH_SHORT).show();
                // selesaikan activity
//                finish();
//                restartActivity();
                pDialog.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_gambar_gagal), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }

        private void restartActivity(){
            Intent i =getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

//    `

}
