package com.example.elbuensabor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrarEmpleadoActivity extends AppCompatActivity {

    EditText edtRegNombre, edtRegApellido1,edtRegApellido2,edtRegDNI,edtRegTelefono,edtRegEmail,edtRegPassword;
    Button btnRegistrarEmpleado;
    CardView cvRolEmpleado;
    Spinner spinnerRol;
    String URL = "http://192.168.1.96:8080/apppedido/api/persona/registrar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_empleado);
        edtRegNombre=findViewById(R.id.edtRegNombres);
        edtRegApellido1=findViewById(R.id.edtRegApellido1);
        edtRegApellido2=findViewById(R.id.edtRegApellido2);
        edtRegDNI=findViewById(R.id.edtRegDNI);
        edtRegTelefono=findViewById(R.id.edtRegTelefono);
        edtRegEmail=findViewById(R.id.edtRegEmail);
        edtRegPassword=findViewById(R.id.edtRegPassword);
        btnRegistrarEmpleado=findViewById(R.id.btnRegistarEmpleado);
        cvRolEmpleado=findViewById(R.id.cvRolEmpleado);
        spinnerRol=findViewById(R.id.spinnerRol);

        btnRegistrarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtRegNombre.getText().toString().isEmpty()&& !edtRegApellido1.getText().toString().isEmpty()&& !edtRegApellido2.getText().toString().isEmpty()&&
                        !edtRegDNI.getText().toString().isEmpty() && !edtRegTelefono.getText().toString().isEmpty() &&!edtRegEmail.getText().toString().isEmpty() &&
                        !edtRegPassword.getText().toString().isEmpty()
                ){
                    registrarUsuario();
                }else{
                    Toast.makeText(RegistrarEmpleadoActivity.this, "No se permiten campos vacíos", Toast.LENGTH_SHORT).show();
                }
                //alertView("hola");
            }
        });

        llenarSpinner();
    }

    public void llenarSpinner(){
        String[] array = {"ADMINISTRADOR", "REPARTIDOR"};
        //Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(spinnerArrayAdapter);
    }

    public void registrarUsuario() {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo..", "Espere por favor");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("nrodocumento", edtRegDNI.getText().toString());
            postData.put("apepaterno", edtRegApellido1.getText().toString());
            postData.put("apematerno", edtRegApellido2.getText().toString());
            postData.put("nombre", edtRegNombre.getText().toString());
            postData.put("sexo", "0");
            postData.put("telefono", edtRegTelefono.getText().toString());
            postData.put("email", edtRegEmail.getText().toString());
            postData.put("password", edtRegPassword.getText().toString());
            String rol=spinnerRol.getSelectedItem().toString().equals("ADMINISTRADOR")?"5":"6";
            postData.put("rol", rol);
            postData.put("idusuario", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();


                try {
                    if(response.getBoolean("success")){
                     /* AlertDialog alertDialog = new AlertDialog.Builder(getApplication()).create();
                      alertDialog.setTitle("MENSAJE");
                      alertDialog.setMessage(response.getString("text"));
                      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                              new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int which) {
                                      dialog.dismiss();
                                  }
                              });
                      alertDialog.show();*/
                        alertView(response.getString("text"));
                    }else{
                        Toast.makeText(RegistrarEmpleadoActivity.this, response.getString("text"), Toast.LENGTH_SHORT).show();
                    }


                    //Toast.makeText(getApplication(), response.getString("text"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplication(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void alertView( String message ) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle( "MENSAJE" )
                //.setIcon(R.drawable.icon_email_)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        dialoginterface.cancel();
                    }
                })
                /*.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})*/
                .show();
    }
}