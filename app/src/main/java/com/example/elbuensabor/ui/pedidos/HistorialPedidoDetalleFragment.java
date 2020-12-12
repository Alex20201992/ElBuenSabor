package com.example.elbuensabor.ui.pedidos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.elbuensabor.MainActivity;
import com.example.elbuensabor.R;
import com.example.elbuensabor.activities.LoginActivity;
import com.example.elbuensabor.adapters.PedidosClientesDetalleAdapter;
import com.example.elbuensabor.entities.PedidoClienteDetalle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class HistorialPedidoDetalleFragment extends Fragment {
    RecyclerView rv;
    PedidosClientesDetalleAdapter pedidoClienteAdapter;
    List<PedidoClienteDetalle> pedidoList;
    RequestQueue requestQueue ;
    TextView tvPedidoDetalleTotal,tvObservacion,tvMedioPago;
    CardView cvCalificacionEntrega;
    Button btnEstrella1,btnEstrella2,btnEstrella3,btnEstrella4,btnEstrella5,btnGuardarCalificacion;
    String URL = "http://192.168.1.96:8080/apppedido/api/pedido/detallepedido/";
    String URL_CAL = "http://192.168.1.96:8080/apppedido/api/pedido/registrarcalificacion";
    int calificacionEstrella=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_historial_pedido_detalle, container, false);
        rv = view.findViewById(R.id.recyclerHistorialPedidosDetalle);
        tvPedidoDetalleTotal=view.findViewById(R.id.tvPedidoDetalleTotal);
        cvCalificacionEntrega=view.findViewById(R.id.cvCalificacionEntrega);
        tvMedioPago=view.findViewById(R.id.tvMedioPago);
        tvObservacion=view.findViewById(R.id.tvObservacion);
        btnEstrella1=view.findViewById(R.id.btnEstrella1);
        btnEstrella2=view.findViewById(R.id.btnEstrella2);
        btnEstrella3=view.findViewById(R.id.btnEstrella3);
        btnEstrella4=view.findViewById(R.id.btnEstrella4);
        btnEstrella5=view.findViewById(R.id.btnEstrella5);
        btnGuardarCalificacion=view.findViewById(R.id.btnGuardarCalificacion);

        if(!getArguments().getString("estado").equals("ENTREGADO")) {
            cvCalificacionEntrega.setVisibility(View.GONE);
        }

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

        calificarPedido(Integer.parseInt(getArguments().getString("calificacion")));

        btnEstrella1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calificacionEstrella=1;
                calificarPedido(calificacionEstrella);
            }
        });
        btnEstrella2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calificacionEstrella=2;
                calificarPedido(calificacionEstrella);
            }
        });
        btnEstrella3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calificacionEstrella=3;
                calificarPedido(calificacionEstrella);
            }
        });
        btnEstrella4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calificacionEstrella=4;
                calificarPedido(calificacionEstrella);
            }
        });
        btnEstrella5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calificacionEstrella=5;
                calificarPedido(calificacionEstrella);
            }
        });

        btnGuardarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calificacionPedido(calificacionEstrella);
            }
        });
        return view;
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

    public void calificacionPedido(int calificacion) {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Guardando Cambios..", "Espere por favor");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        SharedPreferences preferences=this.getActivity().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        int idusuario=Integer.parseInt(preferences.getString("idusuario",""));


        JSONObject postData = new JSONObject();
        try {
            postData.put("idpedido", getArguments().getString("idpedido"));
            postData.put("calificacion", calificacion);
            postData.put("idusuario", idusuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL_CAL,postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(getContext(), response.getString("text"), Toast.LENGTH_LONG).show();
                    }
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
        requestQueue.add(stringRequest);
    }

    public void calificarPedido(int i){
        switch (i){
            case 0:
                btnEstrella1.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella2.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella3.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella4.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella5.setBackgroundResource(R.drawable.ic_star_gray);
                break;
            case 1:
                btnEstrella1.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella2.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella3.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella4.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella5.setBackgroundResource(R.drawable.ic_star_gray);
                break;
            case 2:
                btnEstrella1.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella2.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella3.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella4.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella5.setBackgroundResource(R.drawable.ic_star_gray);
                break;
            case 3:
                btnEstrella1.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella2.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella3.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella4.setBackgroundResource(R.drawable.ic_star_gray);
                btnEstrella5.setBackgroundResource(R.drawable.ic_star_gray);
                break;
            case 4:
                btnEstrella1.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella2.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella3.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella4.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella5.setBackgroundResource(R.drawable.ic_star_gray);
                break;
            case 5:
                btnEstrella1.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella2.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella3.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella4.setBackgroundResource(R.drawable.ic_star_gold);
                btnEstrella5.setBackgroundResource(R.drawable.ic_star_gold);
                break;
        }
    }
}