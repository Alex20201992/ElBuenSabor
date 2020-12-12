package com.example.elbuensabor.ui.repartidor;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;
import com.example.elbuensabor.adapters.PedidosClientesDetalleAdapter;
import com.example.elbuensabor.entities.PedidoClienteDetalle;
import com.example.elbuensabor.entities.Repartidor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RepartidorPedidoDetalleFragment extends Fragment {
    RecyclerView rv;
    PedidosClientesDetalleAdapter pedidoClienteAdapter;
    List<PedidoClienteDetalle> pedidoList;
    RequestQueue requestQueue ;
    TextView tvPedidoDetalleTotal,tvObservacion,tvMedioPago;
    Spinner spinnerRepartidor;
    Button btnConfirmarEntrega;
    int idrepartidor;
    String URL = "http://192.168.1.96:8080/apppedido/api/pedido/detallepedido/";
    String URL_CONFIRMAR = "http://192.168.1.96:8080/apppedido/api/pedido/actualizarestado";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_repartidor_pedido_detalle, container, false);
        rv = view.findViewById(R.id.recyclerPedidosClientesDetalle);
        tvPedidoDetalleTotal=view.findViewById(R.id.tvPedidoDetalleTotal);
        btnConfirmarEntrega=view.findViewById(R.id.btnConfirmarEntrega);
        tvMedioPago=view.findViewById(R.id.tvMedioPago);
        tvObservacion=view.findViewById(R.id.tvObservacion);
        pedidoList=new ArrayList<>();
        listarPedidos();

        if(getArguments().getString("efectivo").equals("0.0")){
            tvMedioPago.setText(getArguments().getString("mediopago"));
        }else{
            tvMedioPago.setText(getArguments().getString("mediopago")+" CON CAMBIO DE S/."+getArguments().getString("efectivo"));
        }
        if(getArguments().getString("observacion").isEmpty()){
            tvObservacion.setText("NO HAY OBSERVACIONES DE ENVÍO");
        }else{
            tvObservacion.setText(getArguments().getString("observacion").toUpperCase());}

        btnConfirmarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarEntrega();
            }
        });

        return view;
    }

    public void confirmarEntrega() {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Subiendo..", "Espere por favor");

        JSONObject postData = new JSONObject();
        try {
            postData.put("idpedido",getArguments().getString("idpedido"));
            postData.put("estado", "21");
            postData.put("idregistro", "5");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, URL_CONFIRMAR,postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    Toast.makeText(getContext(), response.getString("text"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request) ;
    }

    private void listarPedidos() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL+getArguments().getString("idpedido"),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    DecimalFormat formato = new DecimalFormat("#,##0.00");
                    String total=response.getString("text");

                    tvPedidoDetalleTotal.setText("PAGO TOTAL S/ "+formato.format(Double.parseDouble(total)));
                    JSONArray array=response.getJSONArray("data");
                    for(int i=0;i<array.length();i++) {
                        JSONObject jsonObject=array.getJSONObject(i);
                        PedidoClienteDetalle pedido = new PedidoClienteDetalle();
                        pedido.setNombre(jsonObject.getString("nombre"));
                        pedido.setCantidad(jsonObject.getInt("cantidad"));
                        pedido.setPrecio(jsonObject.getDouble("precio"));
                        pedidoList.add(pedido);
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

    private void setupRecyclerView(List<PedidoClienteDetalle> pedidosCliente) {
        pedidoClienteAdapter = new PedidosClientesDetalleAdapter(pedidosCliente,getContext()) ;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(pedidoClienteAdapter);
    }
}