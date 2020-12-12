package com.example.elbuensabor.ui.pedidos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;
import com.example.elbuensabor.adapters.HistorialPedidoAdapter;
import com.example.elbuensabor.adapters.PedidosClientesAdapter;
import com.example.elbuensabor.entities.HistorialPedido;
import com.example.elbuensabor.entities.Repartidor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PedidosClientesFragment extends Fragment {

    RecyclerView rv;
    PedidosClientesAdapter pedidoClienteAdapter;
    List<HistorialPedido> pedidoList;
    RequestQueue requestQueue ;
    Spinner spinnerEstado;
    //String URL_1 = "http://192.168.1.96:8080/apppedido/api/pedido/listarrecepcion";
    String URL = "http://192.168.1.96:8080/apppedido/api/pedido/listarestado/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pedidos_clientes, container, false);
        rv = view.findViewById(R.id.recyclerPedidosClientes);
        spinnerEstado=view.findViewById(R.id.spinnerEstado);
        pedidoList=new ArrayList<>();
        llenarSpinner();


        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), spinnerEstado.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if(pedidoClienteAdapter!=null){
                    pedidoClienteAdapter.clear();
                }
                listarPedidos(spinnerEstado.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void llenarSpinner(){
        String[] array = {"PROCESADO", "EN CAMINO","ENTREGADO"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(spinnerArrayAdapter);
    }

    private void listarPedidos(String estado) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL+estado,null, new Response.Listener<JSONObject>() {
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
                        historialPedido.setCalificacion(jsonObject.getString("calificacion"));
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
        pedidoClienteAdapter = new PedidosClientesAdapter(pedidosCliente,getContext()) ;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(pedidoClienteAdapter);
    }
}