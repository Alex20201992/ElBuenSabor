package com.example.elbuensabor.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;
import com.example.elbuensabor.entities.Producto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ProductoCategoriaAdapter extends RecyclerView.Adapter<ProductoCategoriaAdapter.ViewHolder>{

    Context context;
    private List<Producto> listaProducto ;
    String categoria;

    public ProductoCategoriaAdapter(Context context, List<Producto> listaProducto,String categoria) {
        this.context = context;
        this.listaProducto = listaProducto;
        this.categoria=categoria;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView codigoProducto,nombreProducto,precio;
        LinearLayout view_container;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            codigoProducto=itemView.findViewById(R.id.tvCodigoProducto);
            nombreProducto=itemView.findViewById(R.id.tvNombreProducto);
            //precio=itemView.findViewById(R.id.tvPrecio);
            view_container=itemView.findViewById(R.id.contenedor);
            cardView=itemView.findViewById(R.id.cardviewCarta);
        }
    }

    @NonNull
    @Override
    public ProductoCategoriaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.fragment_agregar_carta_item,parent,false) ;
        final ViewHolder viewHolder = new ViewHolder(view) ;
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo=listaProducto.get(viewHolder.getAdapterPosition()).getId_producto().toString();
                //Bundle bundle = new Bundle();
                //bundle.putString("id_producto", listaProducto.get(viewHolder.getAdapterPosition()).getId_producto().toString());
                //bundle.putString("nombre_producto", listaProducto.get(viewHolder.getAdapterPosition()).getNombre());
                //Navigation.findNavController(view).navigate(R.id.nav_editarProducto,bundle);
                actualizarProducto(codigo,categoria);
                viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorGray));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoCategoriaAdapter.ViewHolder holder, int position) {
        holder.codigoProducto.setText("COD."+listaProducto.get(position).getId_producto());
        holder.nombreProducto.setText(listaProducto.get(position).getNombre());
        //holder.precio.setText("S/. "+listaProducto.get(position).getPrecio().toString());
    }

    @Override
    public int getItemCount() {
        return listaProducto.size();
    }


    public void actualizarProducto(final String id_producto, final String categoria) {
        String EDIT_URL="http://192.168.1.96:8080/apppedido/api/producto/anexarcarta";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final ProgressDialog loading = ProgressDialog.show(context, "Agregando...", "Espere por favor");
        JSONObject postData = new JSONObject();
        try {
            postData.put("idproducto", id_producto);
            postData.put("idcarta", categoria);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, EDIT_URL,postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    Toast.makeText(context, response.getString("text"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}
