package com.example.elbuensabor.ui.carrito;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.elbuensabor.MainActivity;
import com.example.elbuensabor.R;
public class MensajeFragment extends Fragment {

    TextView tvMensajeTitulo, tvMensajeDescripcion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mensaje, container, false);
        tvMensajeTitulo=view.findViewById(R.id.tvMensajeTitulo);
        tvMensajeDescripcion=view.findViewById(R.id.tvMensajeDescripcion);
        tvMensajeTitulo.setText(getArguments().getString("carrito_mensaje"));
        tvMensajeDescripcion.setText(getArguments().getString("carrito_detalle"));
        return view;
    }
}