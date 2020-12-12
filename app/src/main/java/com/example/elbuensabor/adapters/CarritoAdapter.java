package com.example.elbuensabor.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elbuensabor.MainActivity;
import com.example.elbuensabor.R;
import com.example.elbuensabor.database.Carrito;

import java.security.PrivateKey;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {
    private List<Carrito> carts;
    private Context context;
    private View view;

    public CarritoAdapter(List<Carrito> carts, Context context) {
        this.carts = carts;
        this.context = context;
    }

    @NonNull
    @Override
    public CarritoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_listar_carrito_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CarritoAdapter.ViewHolder holder, final int position) {
        final Carrito cart=carts.get(position);

        Glide.with(context).load(carts.get(position).getImageid()).into(holder.primage);
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        holder.precio.setText("S/"+formato.format(carts.get(position).getPrecio()));
        holder.cantidad.setText(""+carts.get(position).getCantidad());
        holder.nombre.setText(carts.get(position).getNombre());
        holder.tvSubtotalCarrito.setText("SUBTOTAL: S/"+formato.format(carts.get(position).getTotal()));
        holder.deletbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carts.remove(position);
                notifyDataSetChanged();
                MainActivity.myDatabase.carritoDao().deleteItem(cart.getId());
                //int cartcount= MainActivity.myDatabase.carritoDao().countCart();
                //double precioTotal= MainActivity.myDatabase.carritoDao().getprecioTotal();
                Intent intent=new Intent("mensaje");
                //intent.putExtra("cartcount",cartcount);
                //intent.putExtra("preciototal",precioTotal);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        holder.btnAumentarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //carts.set(position,carts);


                //notifyItemInserted(position);



                int cantidad=MainActivity.myDatabase.carritoDao().getCant(cart.getId());
                if(cantidad<10){
                    MainActivity.myDatabase.carritoDao().updateCantCart(cantidad+1,cart.getId());
                }

                //carts.clear();
                //addAll(carts);
                //updateUI(carts);

                //notifyDataSetChanged();


                //Intent intent=new Intent("mensaje");
                //intent.putExtra("cartcount",cartcount);
                //intent.putExtra("preciototal",precioTotal);
                //Toast.makeText(context, cantidad+"", Toast.LENGTH_SHORT).show();
                //Navigation.findNavController(view).navigate(R.id.nav_listar_carrito);
                //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                //NavHostFragment.findNavController((Activity) context).navigate(R.id.nav_detalle_carta);
                //Navigation.findNavController((Activity) context,R.id.nav_host_fragment).navigate(R.id.nav_listar_carrito);
                //NavHostFragment.findNavController().navigate(R.id.homeFragment, null, NavOptions.Builder().setPopUpTo(R.id.nav_listar_carrito, true).build());

                Intent intent=new Intent("mensaje");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        holder.btnDisminuirCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad=MainActivity.myDatabase.carritoDao().getCant(cart.getId());
                if(cantidad>1){
                    MainActivity.myDatabase.carritoDao().updateCantCart(cantidad-1,cart.getId());
                }
                Intent intent=new Intent("mensaje");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    public void addAll(List<Carrito> contactList) {
        for (Carrito contact : contactList) {
            // add(contact);
            contactList.add(0, contact);        }
    }

    private void updateUI(List<Carrito> users){
        // 3 - Stop refreshing and clear actual list of users
        //carts.clear();
        carts.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView primage,deletbtn;
        private TextView precio,nombre,cantidad,codigo,tvSubtotalCarrito,btnAumentarCantidad,btnDisminuirCantidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            primage=itemView.findViewById(R.id.imgProductoCarrito);
            deletbtn=itemView.findViewById(R.id.btnEliminarItem);
            precio=itemView.findViewById(R.id.tvPrecioCarrito);
            nombre=itemView.findViewById(R.id.tvNombreCarrito);
            cantidad=itemView.findViewById(R.id.tvCantidadCarrito);
            codigo=itemView.findViewById(R.id.tvCodigoProCarrito);
            tvSubtotalCarrito=itemView.findViewById(R.id.tvSubtotalCarrito);
            btnDisminuirCantidad=itemView.findViewById(R.id.btnDisminuirCantidad);
            btnAumentarCantidad=itemView.findViewById(R.id.btnAumentarCantidad);
    }
    }
}
