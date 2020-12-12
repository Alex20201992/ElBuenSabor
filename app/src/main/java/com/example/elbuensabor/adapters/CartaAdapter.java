package com.example.elbuensabor.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.elbuensabor.R;
import com.example.elbuensabor.entities.Producto;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class CartaAdapter extends RecyclerView.Adapter<CartaAdapter.ViewHolder>{

    Context context;
    private List<Producto> listaProducto ;

    public CartaAdapter(Context context, List<Producto> listaProducto) {
        this.context = context;
        this.listaProducto = listaProducto;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView codigoProducto,nombreProducto,descripcion,precio;
        LinearLayout view_container;
        ImageView imgProducto;

        public ViewHolder(View itemView) {
            super(itemView);
            codigoProducto=itemView.findViewById(R.id.tvCodigoProducto);
            nombreProducto=itemView.findViewById(R.id.tvNombreProducto);
            descripcion=itemView.findViewById(R.id.tvDescripcion);
            precio=itemView.findViewById(R.id.tvPrecio);
            view_container=itemView.findViewById(R.id.contenedor);
            imgProducto=(ImageView) itemView.findViewById(R.id.imgProducto);
        }
    }

    @NonNull
    @Override
    public CartaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.fragment_productos_item,parent,false) ;
        final ViewHolder viewHolder = new ViewHolder(view) ;
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id_producto", listaProducto.get(viewHolder.getAdapterPosition()).getId_producto().toString());
                //bundle.putString("nombre_producto", listaProducto.get(viewHolder.getAdapterPosition()).getNombre());
                Navigation.findNavController(view).navigate(R.id.nav_detalle_carta,bundle);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartaAdapter.ViewHolder holder, int position) {
        holder.codigoProducto.setText("COD."+listaProducto.get(position).getId_producto());
        holder.nombreProducto.setText(listaProducto.get(position).getNombre());
        holder.descripcion.setText(listaProducto.get(position).getDescripcion());
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        holder.precio.setText("S/. "+formato.format(listaProducto.get(position).getPrecio()));
        Glide.with(context).load(listaProducto.get(position).getImg_url()).signature(new StringSignature(UUID.randomUUID().toString())).centerCrop().into(holder.imgProducto);
    }

    @Override
    public int getItemCount() {
        return listaProducto.size();
    }
}
