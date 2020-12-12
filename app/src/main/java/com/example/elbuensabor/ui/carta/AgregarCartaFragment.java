package com.example.elbuensabor.ui.carta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;
import com.example.elbuensabor.adapters.ProductoCategoriaAdapter;
import com.example.elbuensabor.entities.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgregarCartaFragment extends Fragment {

    private final String JSON_URL = "http://192.168.1.96:8080/apppedido/api/producto/listar";
    private JsonArrayRequest request ;
    private RequestQueue requestQueue ;
    private List<Producto> productList;
    private ProductoCategoriaAdapter productoCategoriaAdapter;
    private RecyclerView recyclerView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_agregar_carta, container, false);
        productList = new ArrayList<>() ;
        recyclerView = view.findViewById(R.id.recyclerProductoCategoria);
        listarProductosCarta();
        return view;
    }



    private void listarProductosCarta() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("data");
                    for(int i=0;i<array.length();i++) {
                        JSONObject jsonObject=array.getJSONObject(i);
                        Producto producto = new Producto();
                        producto.setId_producto(jsonObject.getInt("idproducto"));
                        producto.setNombre(jsonObject.getString("nombre"));
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
        productoCategoriaAdapter= new ProductoCategoriaAdapter(getContext(),productList,getArguments().getString("id_carta")) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productoCategoriaAdapter);
    }
}