package com.example.elbuensabor.ui.productos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;
import com.example.elbuensabor.adapters.ProductoAdapter;
import com.example.elbuensabor.entities.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProductosFragment extends Fragment {
    private final String JSON_URL = "http://192.168.1.96:8080/apppedido/api/producto/listar";
    private JsonArrayRequest request ;
    private RequestQueue requestQueue ;
    private List<Producto> productList;
    private ProductoAdapter productoAdapter;
    private RecyclerView recyclerView ;
    private Button btnAgregarProducto;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_productos, container, false);
        productList = new ArrayList<>() ;
        recyclerView = view.findViewById(R.id.recyclerProducto);
        btnAgregarProducto=view.findViewById(R.id.btnAgregarProducto);
        listarProductos();

        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_agregarProducto);
            }
        });

        return view;
    }

    private void listarProductos() {
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
        productoAdapter = new ProductoAdapter(getContext(),productList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productoAdapter);
    }
}