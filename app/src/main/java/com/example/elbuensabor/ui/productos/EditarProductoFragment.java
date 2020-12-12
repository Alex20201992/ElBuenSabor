package com.example.elbuensabor.ui.productos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.elbuensabor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class EditarProductoFragment extends Fragment {
    Button btnEditarProducto;
    ImageView imageView;
    EditText edtProductoNombre,edtProductoPrecio, edtDescripcion;
    Switch  switchEstado;

    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String URL_BUSCAR = "http://192.168.1.96:8080/apppedido/api/producto/recuperar/";
    String EDIT_URL = "http://192.168.1.96:8080/apppedido/api/producto/modificar";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_producto, container, false);
        btnEditarProducto = view.findViewById(R.id.btnGuardarCambios);
        edtProductoNombre = view.findViewById(R.id.edtProductoNombreEdit);
        edtProductoPrecio = view.findViewById(R.id.edtPrecioProductoEdit);
        edtDescripcion = view.findViewById(R.id.edtDescripcionEdit);
        imageView=view.findViewById(R.id.imgProductoEditar);
        switchEstado =view.findViewById(R.id.switchDisponibilidad);

        buscarProducto(URL_BUSCAR +getArguments().getString("id_producto"));
        btnEditarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtProductoNombre.getText().toString().isEmpty() && !edtProductoPrecio.getText().toString().isEmpty()){
                    actualizarProducto();
                }else{
                    Toast.makeText(getContext(), "No se permiten campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        return view;
    }

    public void buscarProducto(String URL){
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                        JSONObject jsonObject=response.getJSONObject("data");
                        edtProductoNombre.setText(jsonObject.getString("nombre"));
                        edtDescripcion.setText(jsonObject.getString("descripcion"));
                        edtProductoPrecio.setText(jsonObject.getString("precio"));
                        Glide.with(getContext()).load(jsonObject.getString("imagen")).signature(new StringSignature(UUID.randomUUID().toString())).centerCrop().into(imageView);
                        String estado=jsonObject.getString("estado");
                        if (estado.equals("1")){
                            switchEstado.setChecked(true);
                        }else{
                            switchEstado.setChecked(false);
                        }
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

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void actualizarProducto() {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Modificando...", "Espere por favor");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject postData = new JSONObject();
        try {
            postData.put("idproducto", getArguments().getString("id_producto"));
            postData.put("nombre", edtProductoNombre.getText().toString());
            postData.put("precio", edtProductoPrecio.getText().toString());
            postData.put("descripcion", edtDescripcion.getText().toString());
            if (bitmap!=null){
                postData.put("imagen", getStringImagen(bitmap).replace("\n", ""));
            }else{
                postData.put("imagen", "");
            }
            String estado;
            if (switchEstado.isChecked())
                estado= "1";
            else
                estado= "0";
            postData.put("estado", estado);
            postData.put("idmodifica", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, EDIT_URL,postData, new Response.Listener<JSONObject>() {
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
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}