package com.example.elbuensabor.ui.carta;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.elbuensabor.MainActivity;
import com.example.elbuensabor.R;
import com.example.elbuensabor.database.Carrito;
import com.example.elbuensabor.database.CarritoDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.UUID;

public class DetalleCartaProductoFragment extends Fragment {

    ImageView imageView;
    EditText edtCantidadDetalle;
    TextView tvProductoNombre,tvProductoPrecio, tvDescripcion;
    int codigo;
    String img_url;
    String nombre;
    Double precio;
    int cantidad;
    Double preciototal;
    Button btnAgregarCarrito;



    String URL_BUSCAR = "http://192.168.1.96:8080/apppedido/api/producto/recuperar/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_carta_producto, container, false);
        //btnEditarProducto = view.findViewById(R.id.btnGuardarCambios);
        tvProductoNombre = view.findViewById(R.id.tvNombreDetalle);
        tvProductoPrecio = view.findViewById(R.id.tvPrecioDetalle);
        tvDescripcion = view.findViewById(R.id.tvDescripcionDetalle);
        edtCantidadDetalle = view.findViewById(R.id.edtCantidadDetalle);
        imageView=view.findViewById(R.id.imgProductoDetalle);
        buscarProducto(URL_BUSCAR +getArguments().getString("id_producto"));

        btnAgregarCarrito=view.findViewById(R.id.btnAgregarCarrito);
        btnAgregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtCantidadDetalle.getText().toString().isEmpty()) {
                    Carrito cart = new Carrito();
                    cart.setId(codigo);
                    cart.setImageid(img_url);
                    cart.setNombre(nombre);
                    cantidad = Integer.parseInt(edtCantidadDetalle.getText().toString());
                    preciototal = precio * cantidad;
                    cart.setCantidad(cantidad);
                    cart.setPrecio(precio);
                    cart.setTotal(preciototal);
                    if (MainActivity.myDatabase.carritoDao().isAddToCart(codigo) != 1) {
                        MainActivity.myDatabase.carritoDao().addToCart(cart);
                        Toast.makeText(getContext(), "El producto se ha agregado al Carrito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "¡Ya está agregado al carrito!", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(getContext(), "Ingrese una cantidad!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void buscarProducto(String URL){
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject=response.getJSONObject("data");
                    codigo=Integer.parseInt(jsonObject.getString("idproducto"));
                    img_url=jsonObject.getString("imagen");
                    nombre=jsonObject.getString("nombre");
                    precio= Double.parseDouble(jsonObject.getString("precio"));
                    tvProductoNombre.setText(jsonObject.getString("nombre"));
                    tvDescripcion.setText(jsonObject.getString("descripcion"));
                    DecimalFormat formato = new DecimalFormat("#,##0.00");
                    Double precio=Double.parseDouble(jsonObject.getString("precio"));
                    tvProductoPrecio.setText("S/"+formato.format(precio));
                    Glide.with(getContext()).load(jsonObject.getString("imagen")).signature(new StringSignature(UUID.randomUUID().toString())).centerCrop().into(imageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Verifique su conexión", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
}