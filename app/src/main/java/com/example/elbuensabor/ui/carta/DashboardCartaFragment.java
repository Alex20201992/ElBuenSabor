package com.example.elbuensabor.ui.carta;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elbuensabor.R;

public class DashboardCartaFragment extends Fragment {
    CardView crvComidaCriolla, crvComidaRapida, crvDesayunos, crvAlmuerzos, crvPostres, crvBebidas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_dashboard_carta, container, false);
        crvComidaCriolla=view.findViewById(R.id.crvComidaCriolla);
        crvComidaRapida=view.findViewById(R.id.crvComidaRapida);
        crvDesayunos=view.findViewById(R.id.crvDesayunos);
        crvAlmuerzos=view.findViewById(R.id.crvAlmuerzos);
        crvPostres=view.findViewById(R.id.crvPostres);
        crvBebidas=view.findViewById(R.id.crvBebidas);

        crvComidaCriolla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id_carta", "1");
                bundle.putString("categoria", "COMIDA CRIOLLA");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_listar_carta,bundle);
            }
        });
        crvComidaRapida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id_carta", "2");
                bundle.putString("categoria", "COMIDA RAPIDA");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_listar_carta,bundle);
            }
        });
        crvDesayunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id_carta", "3");
                bundle.putString("categoria", "DESAYUNOS");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_listar_carta,bundle);
            }
        });
        crvAlmuerzos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id_carta", "4");
                bundle.putString("categoria", "ALMUERZOS");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_listar_carta,bundle);
            }
        });
        crvPostres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id_carta", "5");
                bundle.putString("categoria", "POSTRES");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_listar_carta,bundle);
            }
        });
        crvBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id_carta", "6");
                bundle.putString("categoria", "BEBIDAS");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_listar_carta,bundle);
            }
        });

        return view;
    }
}