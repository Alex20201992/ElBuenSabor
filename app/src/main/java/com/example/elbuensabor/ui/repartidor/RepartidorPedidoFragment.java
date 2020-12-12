package com.example.elbuensabor.ui.repartidor;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;
import com.example.elbuensabor.adapters.PedidosClientesAdapter;
import com.example.elbuensabor.adapters.PedidosRepartidorAdapter;
import com.example.elbuensabor.entities.HistorialPedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RepartidorPedidoFragment extends Fragment {

    RecyclerView rv;
    PedidosRepartidorAdapter pedidoRepartidorAdapter;
    List<HistorialPedido> pedidoList;
    RequestQueue requestQueue ;
    String URL = "http://192.168.1.96:8080/apppedido/api/pedido/listarrepartidor/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_repartidor_pedido, container, false);
        rv = view.findViewById(R.id.recyclerPedidosClientes);
        pedidoList=new ArrayList<>();
        listarPedidos();
        return view;
    }

    private void listarPedidos() {
        SharedPreferences preferences=this.getActivity().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        String idusuario=preferences.getString("idusuario","");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL+idusuario,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("data");
                    for(int i=0;i<array.length();i++) {
                        JSONObject jsonObject=array.getJSONObject(i);
                        HistorialPedido historialPedido = new HistorialPedido();
                        historialPedido.setIdpedido(jsonObject.getString("idpedido"));
                        historialPedido.setCliente(jsonObject.getString("nomcliente"));
                        historialPedido.setDireccion(jsonObject.getString("direccion"));
                        historialPedido.setFec_registro(jsonObject.getString("fecpedido"));
                        historialPedido.setEstado(jsonObject.getString("estado"));
                        historialPedido.setMedioPago(jsonObject.getString("mediopago"));
                        historialPedido.setObservacion(jsonObject.getString("obs"));
                        historialPedido.setEfectivo(jsonObject.getString("efectivo"));
                        pedidoList.add(historialPedido);
                    }
                    setupRecyclerView(pedidoList);
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

    private void setupRecyclerView(List<HistorialPedido> pedidosCliente) {
        pedidoRepartidorAdapter = new PedidosRepartidorAdapter(pedidosCliente,getContext()) ;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(pedidoRepartidorAdapter);
    }
}