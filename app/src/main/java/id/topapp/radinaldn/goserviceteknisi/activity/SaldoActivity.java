package id.topapp.radinaldn.goserviceteknisi.activity;

import android.app.Activity;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.badoualy.datepicker.DatePickerTimeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import id.topapp.radinaldn.goserviceteknisi.R;
import id.topapp.radinaldn.goserviceteknisi.adapter.SaldoAdapter;
import id.topapp.radinaldn.goserviceteknisi.config.ServerConfig;
import id.topapp.radinaldn.goserviceteknisi.model.Saldo;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseTopup;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseTopupAdd;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiClient;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiInterface;
import id.topapp.radinaldn.goserviceteknisi.util.ConnectionDetector;
import id.topapp.radinaldn.goserviceteknisi.util.HttpFileUpload;
import id.topapp.radinaldn.goserviceteknisi.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaldoActivity extends AppCompatActivity {

    private static final String TAG = SaldoActivity.class.getSimpleName();
    AlertDialog.Builder alertDialogBuilder;
    LayoutInflater inflater;
    View dialogView;
    private ProgressDialog pDialog;


    SessionManager sessionManager;
    SwipeRefreshLayout swipeRefreshLayout;
    ApiInterface apiService;
    private SaldoAdapter adapter;

    DatePickerTimeline dp_timeline;
    RecyclerView rv_saldoku;
    ImageView ivFoto;
    private ArrayList<Saldo> saldoList = new ArrayList<>();

    private String ID_TEKNISI;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cd = new ConnectionDetector(this);
        setContentView(R.layout.activity_saldo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                actionAdd();
                Toast.makeText(getApplicationContext(), "Foto bukti transfer anda", Toast.LENGTH_SHORT).show();
            }
        });

        dateFormatter = new SimpleDateFormat(
                DATE_FORMAT, Locale.US);

        file = new File(Environment.getExternalStorageDirectory()
                + "/" + IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }

        sessionManager = new SessionManager(this);
        ID_TEKNISI = sessionManager.getTeknisiDetail().get(SessionManager.ID_TEKNISI);

        apiService = ApiClient.getClient().create(ApiInterface.class);


        rv_saldoku = findViewById(R.id.rv_saldo);
        swipeRefreshLayout = findViewById(R.id.swipe_kantor);

        toolbar.setTitle(R.string.histori_topup);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        // click button back pada title bar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // handling refresh recycler view
                refreshUI();
            }
        });

        refreshUI();
    }

    private void actionAdd() {
        finalPhotoName = "Topup_"+sessionManager.getTeknisiDetail().get(SessionManager.ID_TEKNISI)+"_" +dateFormatter.format(new Date()) + ".jpg";

        destFile = new File(file, finalPhotoName);
        imageCaptureUri = Uri.fromFile(destFile);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        startActivityForResult(intentCamera, 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode){
                case 101:
                    if (resultCode == Activity.RESULT_OK){
                        upflag = true;
                        Log.d(TAG +". Pick camera image ", "Selected image uri path : "+imageCaptureUri);

                        bmp = decodeFile(destFile, finalPhotoName);

                        showFormTopup(bmp);


                    }
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshUI() {
        System.out.println("refreshUI()");
        saldoList.clear();
        apiService.topupViewAllByNik(ID_TEKNISI).enqueue(new Callback<ResponseTopup>() {
            @Override
            public void onResponse(Call<ResponseTopup> call, Response<ResponseTopup> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()){
                    if (response.body().getSaldos().size()>0){
                        saldoList.addAll(response.body().getSaldos());

                        System.out.println(saldoList.get(0).getNominal());

                        adapter = new SaldoAdapter(saldoList, SaldoActivity.this);
                        RecyclerView.LayoutManager layoutManager =  new LinearLayoutManager(SaldoActivity.this);
                        rv_saldoku.setLayoutManager(layoutManager);
                        rv_saldoku.setAdapter(adapter);

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTopup> call, Throwable t) {
t.printStackTrace();
            }
        });


    }

    private void goToProfilActivity(){
        Intent intent = new Intent(SaldoActivity.this, ProfilActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToProfilActivity();
    }

    private void showFormTopup(Bitmap bmpFotoBukti) {
        alertDialogBuilder = new AlertDialog.Builder(SaldoActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_topup, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setIcon(R.drawable.ic_monetization_on_black);
        alertDialogBuilder.setTitle(R.string.update_pemesanan);

        final EditText etNominal = dialogView.findViewById(R.id.et_id_telegram);
        ivFoto = dialogView.findViewById(R.id.ivFoto);
        ivFoto.setImageBitmap(bmpFotoBukti);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        // save foto to lcoal storage and to server
                        if (destFile!=null){
                            saveFile(destFile);
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.belum_mengambil_gambar), Toast.LENGTH_SHORT).show();
                        }

                        final String nominal = etNominal.getText().toString();
                        String foto = finalPhotoName;
                        apiService.topupAdd(ID_TEKNISI, nominal, foto).enqueue(new Callback<ResponseTopupAdd>() {
                            @Override
                            public void onResponse(Call<ResponseTopupAdd> call, Response<ResponseTopupAdd> response) {
                                if (response.isSuccessful()){
                                    if (response.body().getCode() == 200){
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                        refreshUI();
                                    } else {
                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseTopupAdd> call, Throwable t) {
                                t.printStackTrace();
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
            pDialog = new ProgressDialog(SaldoActivity.this);
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
                HttpFileUpload hfu = new HttpFileUpload(ServerConfig.UPLOAD_FOTO_TOPUP_ENDPOINT, "ftitle", "fdescription", finalPhotoName);
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

}
