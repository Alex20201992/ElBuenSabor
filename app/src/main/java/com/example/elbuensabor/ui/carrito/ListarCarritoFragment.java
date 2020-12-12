package com.example.elbuensabor.ui.carrito;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.elbuensabor.adapters.CarritoAdapter;
import com.example.elbuensabor.database.Carrito;
import com.example.elbuensabor.entities.DetallePedido;
import com.example.elbuensabor.entities.Pedido;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListarCarritoFragment extends Fragment {
    RecyclerView rv;
    CarritoAdapter cartProductAdapter;
    TextView tvPrecioTotal;
    List<Carrito> carts;
    TextView tvcount;
    Button btnProcesarCompra;
    EditText edtDireccionEnvio,edtObservacion,edtEfectivo;
    RadioButton rbtVisa,rbtMastercard,rbtEfectivo;
    RadioGroup rbtGrupoMedioPago;
    DecimalFormat formato = new DecimalFormat("#,##0.00");
    String URL = "http://192.168.1.96:8080/apppedido/api/pedido/registrar";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_listar_carrito, container, false);
        rv=view.findViewById(R.id.recyclerCarrito);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        tvPrecioTotal=view.findViewById(R.id.tvPrecioTotalCarrito);
        btnProcesarCompra=view.findViewById(R.id.btnProcesarCompra);
        edtDireccionEnvio=view.findViewById(R.id.edtDireccionEnvio);
        edtEfectivo=view.findViewById(R.id.edtEfectivo);
        edtObservacion=view.findViewById(R.id.edtObservacion);
        rbtEfectivo=view.findViewById(R.id.rbtEfectivo);
        rbtVisa=view.findViewById(R.id.rbtVisa);
        rbtMastercard=view.findViewById(R.id.rbtMastercard);
        rbtGrupoMedioPago=view.findViewById(R.id.rbtGrupoMedioPago);
        recuperarPreferencias();

        final Double precioTotal=MainActivity.myDatabase.carritoDao().getprecioTotal();
        tvPrecioTotal.setText("S/"+formato.format(precioTotal));
        getCartData();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mensaje,new IntentFilter("mensaje"));

        btnProcesarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtDireccionEnvio.getText().toString().isEmpty() && !edtEfectivo.getText().toString().isEmpty()){
                    if(!rbtEfectivo.isChecked()){
                        agregarPedido();
                    }else{
                        if(Double.parseDouble(edtEfectivo.getText().toString())>=precioTotal){
                           agregarPedido();
                        }else{
                            Toast.makeText(getContext(), "La cantidad especificada no cubre el monto total a pagar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "Agrege los campos solicitados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rbtGrupoMedioPago.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbtVisa:
                    case R.id.rbtMastercard:
                        edtEfectivo.setVisibility(View.GONE);
                        edtEfectivo.setText("0");
                        break;
                    case R.id.rbtEfectivo:
                        edtEfectivo.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


        return view;
    }

    private void getCartData() {
        carts= MainActivity.myDatabase.carritoDao().getData();
        cartProductAdapter=new CarritoAdapter(carts,getContext());
        rv.setAdapter(cartProductAdapter);
    }

    private Pedido objetoPedido(){
        Pedido pedido=new Pedido();
        List<DetallePedido> detallePedido=new ArrayList<>();

        SharedPreferences preferences=this.getActivity().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        int idusuario=Integer.parseInt(preferences.getString("idusuario",""));

        pedido.setIdcliente(idusuario);
        pedido.setDireccion(edtDireccionEnvio.getText().toString());

        if (rbtVisa.isChecked())
            pedido.setMediopago("23");
            pedido.setEfectivo("0");
        if (rbtMastercard.isChecked())
            pedido.setMediopago("24");
            pedido.setEfectivo("0");
        if (rbtEfectivo.isChecked())
            pedido.setMediopago("25");
            pedido.setEfectivo(edtEfectivo.getText().toString());

        pedido.setObs(edtObservacion.getText().toString());
        for (Carrito detalle:carts) {
            detallePedido.add(new DetallePedido(detalle.getId(),detalle.precio,detalle.cantidad));
        }
        pedido.setDetalle(detallePedido);
        return pedido;
    }

    private void guardarPreferencias(){
        SharedPreferences preferences=this.getActivity().getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("direccion",edtDireccionEnvio.getText().toString());
        editor.commit();
    }

    private void recuperarPreferencias(){
        SharedPreferences preferences=this.getActivity().getSharedPreferences("preferenciasUsuario",Context.MODE_PRIVATE);
        edtDireccionEnvio.setText(preferences.getString("direccion","TU DIRECCIÓN DE ENVÍO"));
    }

    public void agregarPedido() {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Procesando..", "Espere por favor");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Objeto JSON anidado generado con Gson
        Gson gson=new Gson();
        String jsonPedido=gson.toJson(objetoPedido());
        JSONObject postData=null;
        try {
            postData = new JSONObject(jsonPedido);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Fin

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if(response.getBoolean("success")) {
                        guardarPreferencias();
                        MainActivity.myDatabase.carritoDao().deleteCart();
                        Bundle bundle = new Bundle();
                        if(response.getBoolean("success")){
                            bundle.putString("carrito_mensaje", "SU PEDIDO SE HA ENVIADO EXITOSAMENTE");
                            bundle.putString("carrito_detalle", "Hola, hemos recibido tu pedido con éxito. Ingresa al historial del pedidos para revisar su progeso de entrega");
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_carrito_mensaje, bundle);
                        }
                        //Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_carrito_mensaje, bundle);
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_carrito_mensaje, bundle, new NavOptions.Builder()
                                .setPopUpTo(R.id.nav_listar_carta, true)
                                .build());
                    }else{
                        Toast.makeText(getContext(), "Error de Conexón", Toast.LENGTH_LONG).show();
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


    public BroadcastReceiver mensaje=new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            getCartData();
            Double precioTotal=MainActivity.myDatabase.carritoDao().getprecioTotal();
            tvPrecioTotal.setText("S/"+formato.format(precioTotal));
        }
    };
}