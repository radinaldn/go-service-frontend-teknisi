package id.topapp.radinaldn.goserviceteknisi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import id.topapp.radinaldn.goserviceteknisi.R;
import id.topapp.radinaldn.goserviceteknisi.activity.DetailPemesananActivity;
import id.topapp.radinaldn.goserviceteknisi.activity.OrderankuActivity;
import id.topapp.radinaldn.goserviceteknisi.config.ServerConfig;
import id.topapp.radinaldn.goserviceteknisi.fragment.OrderankuFragment;
import id.topapp.radinaldn.goserviceteknisi.model.Pemesanan;
import id.topapp.radinaldn.goserviceteknisi.response.ResponseBayarDenganSaldo;
import id.topapp.radinaldn.goserviceteknisi.response.ResponsePemesanan;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiClient;
import id.topapp.radinaldn.goserviceteknisi.rest.ApiInterface;

import id.topapp.radinaldn.goserviceteknisi.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by radinaldn on 29/12/18.
 */

public class OrderankuAdapter extends RecyclerView.Adapter<OrderankuAdapter.OrderankuViewHolder> {

    private static final String TAG_ID_PEMESANAN = "id_pemesanan";
    private Context context;

    private ArrayList<Pemesanan> dataList;
    private static final String TAG = OrderankuAdapter.class.getSimpleName();

    ApiInterface apiService;
    SessionManager sessionManager;
    private static String ID_TEKNISI;
    private OrderankuFragment fragment;
    private String proses;

    public OrderankuAdapter(Context context, ArrayList<Pemesanan> dataList, OrderankuFragment fragment, String proses) {
        this.context = context;
        this.dataList = dataList;
        this.fragment = fragment;
        this.proses = proses;
    }

    @NonNull
    @Override
    public OrderankuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.orderanku_item, parent, false);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(context);
        ID_TEKNISI = sessionManager.getTeknisiDetail().get(SessionManager.ID_TEKNISI);

        return new OrderankuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderankuViewHolder holder, final int position) {

        // convert saldo
        // convert format saldo into rupiah
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String biayaInRP = formatRupiah.format(Double.parseDouble(dataList.get(position).getBiaya()));

        holder.tv_id_pemesanan.setText(dataList.get(position).getIdPemesanan());

        switch (dataList.get(position).getProses()){
            case "Diproses":
                Picasso.with(context)
                        .load(ServerConfig.FOTO_PEMESANAN_PATH+dataList.get(position).getFotoSebelum())
                        .placeholder(R.drawable.dummy_ava)
                        .error(R.drawable.dummy_ava)
                        .centerCrop()
                        .fit()
                        .into(holder.iv_foto);
                holder.tv_nama_toko.setText(dataList.get(position).getNama());
                holder.bt_update.setVisibility(View.VISIBLE);
                break;
            case "Selesai":
                Picasso.with(context)
                        .load(ServerConfig.FOTO_PEMESANAN_PATH+dataList.get(position).getFotoSesudah())
                        .placeholder(R.drawable.dummy_ava)
                        .error(R.drawable.dummy_ava)
                        .centerCrop()
                        .fit()
                        .into(holder.iv_foto);
                holder.tv_nama_toko.setText(dataList.get(position).getNama());
                holder.bt_update.setVisibility(View.GONE);

                if (dataList.get(position).getKategoriBayar().equalsIgnoreCase("Cash")){
                    holder.bt_dibayar.setVisibility(View.VISIBLE);
                    holder.bt_dibayar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            actionDibayar(dataList.get(position).getIdPemesanan().toString());
                        }
                    });
                }

                break;
            case "Dibayar":
                Picasso.with(context)
                        .load(ServerConfig.FOTO_PEMESANAN_PATH+dataList.get(position).getFotoSesudah())
                        .placeholder(R.drawable.dummy_ava)
                        .error(R.drawable.dummy_ava)
                        .centerCrop()
                        .fit()
                        .into(holder.iv_foto);
                holder.tv_nama_toko.setText(dataList.get(position).getNama());
                holder.bt_update.setVisibility(View.GONE);
                break;
        }

        switch (dataList.get(position).getJenisServis().toLowerCase()){
            case "tv":
                Picasso.with(context)
                        .load(R.drawable.tv)
                        .into(holder.iv_kategori);
                break;
            case "kulkas":
                Picasso.with(context)
                        .load(R.drawable.refrigerator)
                        .into(holder.iv_kategori);
                break;
            case "ac":
                Picasso.with(context)
                        .load(R.drawable.air_conditioner)
                        .into(holder.iv_kategori);
                break;
            case "mesin cuci":
                Picasso.with(context)
                        .load(R.drawable.washing_machine)
                        .into(holder.iv_kategori);
                break;
        }



        holder.tv_kategori_bayar.setText(dataList.get(position).getKategoriBayar());
        holder.tv_keluhan.setText(dataList.get(position).getKeluhan());
        switch (dataList.get(position).getKategoriBayar()){
            case "Cash":

                holder.tv_kategori_bayar.setBackgroundColor(context.getResources().getColor(R.color.colorMintDark));

                if (dataList.get(position).getProses().equalsIgnoreCase("Selesai")){
                    holder.tv_kategori_bayar.setText("(Cash) "+biayaInRP);
                }
                break;
            case "Saldo":

                holder.tv_kategori_bayar.setBackgroundColor(context.getResources().getColor(R.color.colorBlueJeansDark));

                if (dataList.get(position).getProses().equalsIgnoreCase("Selesai")){
                    holder.tv_kategori_bayar.setText("(Saldo) "+biayaInRP);
                }



                break;

        }

        holder.tv_created_at.setText(dataList.get(position).getCreatedAt());
        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+dataList.get(position).getNoHp()));
                context.startActivity(intent);
            }
        });

        holder.bt_lihat_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+dataList.get(position).getLat()+","+dataList.get(position).getLng()+"&z=17"));
                context.startActivity(intent);
            }
        });

        holder.bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof OrderankuActivity){
                    ((OrderankuActivity)context).actionUpdatePemesananByTeknisi(dataList.get(position).getIdPemesanan(), dataList.get(position).getNama(), dataList.get(position).getKeluhan());
                }
            }
        });

    }

    private void actionDibayar(String id_pemesanan) {
        apiService.pemesananUpdateProses(id_pemesanan, "Dibayar").enqueue(new Callback<ResponsePemesanan>() {
            @Override
            public void onResponse(Call<ResponsePemesanan> call, Response<ResponsePemesanan> response) {
                if (response.isSuccessful()){
                    if (response.body().getCode() == 200){
                        Intent i = new Intent(context, DetailPemesananActivity.class);
                        i.putExtra(TAG_ID_PEMESANAN, response.body().getPemesanan().getIdPemesanan());
                        ((Activity)context).finish();
                        context.startActivity(i);
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "response unsuccessful : "+response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePemesanan> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class OrderankuViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id_pemesanan, tv_nama_toko, tv_keluhan, tv_kategori_bayar, tv_created_at;
        ImageView iv_foto, iv_kategori, iv_call;
        Button bt_lihat_lokasi, bt_update, bt_dibayar;

        public OrderankuViewHolder(final View itemView) {
            super(itemView);
            tv_id_pemesanan = itemView.findViewById(R.id.tv_id_pemesanan);
            tv_nama_toko = itemView.findViewById(R.id.tv_nama_masyarakat);
            tv_keluhan = itemView.findViewById(R.id.tv_keluhan);
            tv_created_at = itemView.findViewById(R.id.tv_created_at);
            iv_foto = itemView.findViewById(R.id.iv_foto);
            iv_kategori = itemView.findViewById(R.id.iv_kategori);
            tv_kategori_bayar = itemView.findViewById(R.id.tv_kategori_bayar);
            iv_call = itemView.findViewById(R.id.iv_call);
            bt_lihat_lokasi = itemView.findViewById(R.id.btLihatLokasi);
            bt_update = itemView.findViewById(R.id.btUpdate);
            bt_dibayar = itemView.findViewById(R.id.btDibayar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), DetailPemesananActivity.class);
                    i.putExtra(TAG_ID_PEMESANAN, tv_id_pemesanan.getText());

                    // Aktifkan untuk mode debugging
                    //Toast.makeText(itemView.getContext(), "ID_MENGAJAR : "+tv_idmengajar.getText(), Toast.LENGTH_SHORT).show();

                    itemView.getContext().startActivity(i);
                }
            });

        }
    }


}
