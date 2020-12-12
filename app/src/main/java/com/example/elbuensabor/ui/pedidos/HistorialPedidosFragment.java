package com.example.elbuensabor.ui.pedidos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;
import com.example.elbuensabor.adapters.CarritoAdapter;
import com.example.elbuensabor.adapters.CartaAdapter;
import com.example.elbuensabor.adapters.HistorialPedidoAdapter;
import com.example.elbuensabor.database.Carrito;
import com.example.elbuensabor.entities.HistorialPedido;
import com.example.elbuensabor.entities.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HistorialPedidosFragment extends Fragment {

    RecyclerView rv;
    HistorialPedidoAdapter historialPedidoAdapter;
    List<HistorialPedido> historialPedidoList;
    RequestQueue requestQueue ;
    String URL = "http://192.168.1.96:8080/apppedido/api/persona/listarpedidocliente/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_historial_pedidos, container, false);
        rv = view.findViewById(R.id.recyclerHistorialPedido);
        historialPedidoList=new ArrayList<>();
        listarHistorialPedido();
        return view;
    }

    private void listarHistorialPedido() {
        SharedPreferences preferences=this.getActivity().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        Integer idusuario=Integer.parseInt(preferences.getString("idusuario",""));


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL+idusuario.toString(),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("data");
                    for(int i=0;i<array.length();i++) {
                        JSONObject jsonObject=array.getJSONObject(i);
                        HistorialPedido historialPedido = new HistorialPedido();
                        historialPedido.setIdpedido(jsonObject.getString("idpedido"));
                        historialPedido.setDireccion(jsonObject.getString("direccion"));
                        historialPedido.setFec_registro(jsonObject.getString("fecpedido"));
                        historialPedido.setEstado(jsonObject.getString("estado"));
                        historialPedido.setMedioPago(jsonObject.getString("mediopago"));
                        historialPedido.setObservacion(jsonObject.getString("obs"));
                        historialPedido.setEfectivo(jsonObject.getString("efectivo"));
                        historialPedido.setCalificacion(jsonObject.getString("calificacion"));
                        historialPedidoList.add(historialPedido);
                    }
                    setupRecyclerView(historialPedidoList);
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

    private void setupRecyclerView(List<HistorialPedido> historialPedido) {
        historialPedidoAdapter = new HistorialPedidoAdapter(historialPedido,getContext()) ;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(historialPedidoAdapter);
    }
}