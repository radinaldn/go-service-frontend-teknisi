package id.topapp.radinaldn.goserviceteknisi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import id.topapp.radinaldn.goserviceteknisi.R;
import id.topapp.radinaldn.goserviceteknisi.config.ServerConfig;
import id.topapp.radinaldn.goserviceteknisi.model.Saldo;

/**
 * Created by radinaldn on 30/12/18.
 */

public class SaldoAdapter extends RecyclerView.Adapter<SaldoAdapter.SaldoViewHolder> {

    private ArrayList<Saldo> dataList;
    private static final String TAG = SaldoAdapter.class.getSimpleName();
    private Context context;

    public SaldoAdapter(ArrayList<Saldo> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public SaldoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.saldo_item, parent, false);
        return new SaldoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaldoViewHolder holder, int position) {

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String biayaInRP = formatRupiah.format(Double.parseDouble(dataList.get(position).getNominal()));

        holder.tv_nominal.setText(biayaInRP);
        System.out.println("memasukkan nominal "+dataList.get(position).getNominal());

        holder.tv_proses.setText(dataList.get(position).getProses());

        switch (dataList.get(position).getProses()){
            case "Diproses":
                holder.tv_proses.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                break;
            case "Diterima":
                holder.tv_proses.setBackgroundColor(context.getResources().getColor(R.color.GreenBootstrap));
                break;
            case "Ditolak":
                holder.tv_proses.setBackgroundColor(context.getResources().getColor(R.color.RedBootstrap));
                break;

        }

        holder.tv_created_at.setText(dataList.get(position).getCreatedAt());

        Picasso.with(context).load(ServerConfig.BUKTI_TOPUP_PATH+dataList.get(position).getFoto()).error(R.drawable.dummy_ava).placeholder(R.drawable.dummy_ava).centerCrop().fit().into(holder.iv_foto);

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class SaldoViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nominal, tv_proses, tv_created_at;
        ImageView iv_foto;

        public SaldoViewHolder(View itemView) {
            super(itemView);

            tv_nominal = itemView.findViewById(R.id.tv_nama_toko);
            tv_proses = itemView.findViewById(R.id.tv_kategori_bayar);
            tv_created_at = itemView.findViewById(R.id.tv_created_at);
            iv_foto = itemView.findViewById(R.id.iv_foto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // action click
                    Toast.makeText(context, "Anda menekan topup "+tv_nominal.getText().toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
