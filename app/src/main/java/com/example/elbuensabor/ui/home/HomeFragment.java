package com.example.elbuensabor.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {
    TextView tvDashboardEntregasCamino,tvDashboardClientesAtendidos,tvDashboardCantVentas,tvDashboardPedidosPendientes,tvDashboardTotal;
    String URL = "http://192.168.1.96:8080/apppedido/api/reporte/dashboard";
    RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        tvDashboardTotal=view.findViewById(R.id.tvDashboardTotal);
        tvDashboardCantVentas=view.findViewById(R.id.tvDashboardCantVentas);
        tvDashboardEntregasCamino=view.findViewById(R.id.tvDashboardEntregasCamino);
        tvDashboardClientesAtendidos=view.findViewById(R.id.tvDahboardClientesAtendidos);
        tvDashboardPedidosPendientes=view.findViewById(R.id.tvDashboardPedidosPendientes);
        mostrarDashboard(URL);
        return view;
    }

    public void mostrarDashboard(String URL){
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject=response.getJSONObject("data");
                    DecimalFormat formato = new DecimalFormat("#,##0.00");
                    tvDashboardTotal.setText("S/"+formato.format(Double.parseDouble(jsonObject.getString("mtoventa"))));
                    tvDashboardCantVentas.setText(jsonObject.getString("cantventa"));
                    tvDashboardPedidosPendientes.setText(jsonObject.getString("cantpendiente"));
                    tvDashboardEntregasCamino.setText(jsonObject.getString("cantreparto"));
                    tvDashboardClientesAtendidos.setText(jsonObject.getString("cantcliente"));
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

        requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
}