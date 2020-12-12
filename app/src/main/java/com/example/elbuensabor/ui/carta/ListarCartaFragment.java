package com.example.elbuensabor.ui.carta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;
import com.example.elbuensabor.adapters.CartaAdapter;
import com.example.elbuensabor.adapters.ProductoAdapter;
import com.example.elbuensabor.entities.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListarCartaFragment extends Fragment {
    TextView tvTituloCategoria;
    private final String JSON_URL = "http://192.168.1.96:8080/apppedido/api/producto/listarxcarta/";
    private JsonArrayRequest request ;
    private RequestQueue requestQueue ;
    private List<Producto> productList;
    //private ProductoAdapter productoAdapter;
    private CartaAdapter cartaAdapter;
    private RecyclerView recyclerView ;
    private Button btnAgregarCarta,btnMostrarCarrito;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_listar_carta, container, false);
        tvTituloCategoria=view.findViewById(R.id.tvTitutoCategoria);
        tvTituloCategoria.setText(getArguments().getString("categoria"));
        btnAgregarCarta = view.findViewById(R.id.btnAgregarCarta);
        recyclerView = view.findViewById(R.id.recyclerProducto);
        productList = new ArrayList<>() ;

        SharedPreferences preferences=this.getActivity().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        String usuariotipo=preferences.getString("nomrol","");
        if(usuariotipo.equals("CLIENTE")){
            btnAgregarCarta.setVisibility(View.GONE);
        }
        listarCarta();
        btnAgregarCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id_carta", getArguments().getString("id_carta"));
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_agregar_carta,bundle);
            }
        });
        return view;
    }

    private void listarCarta() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL+getArguments().getString("id_carta"),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("data");
                    for(int i=0;i<array.length();i++) {
                        JSONObject jsonObject=array.getJSONObject(i);
                        Producto producto = new Producto();
                        producto.setId_producto(jsonObject.getInt("idproducto"));
                        producto.setNombre(jsonObject.getString("nombre"));
                        producto.setDescripcion(jsonObject.getString("descripcion"));
                        producto.setPrecio(jsonObject.getDouble("precio"));
                        producto.setImg_url(jsonObject.getString("imagen"));
                        productList.add(producto);
                    }
                    setupRecyclerView(productList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request) ;

    }

    private void setupRecyclerView(List<Producto> productList) {
        cartaAdapter = new CartaAdapter(getContext(),productList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartaAdapter);
    }
}