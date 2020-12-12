package com.example.elbuensabor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elbuensabor.R;
import com.example.elbuensabor.entities.HistorialPedido;

import java.util.List;

public class HistorialPedidoAdapter extends RecyclerView.Adapter<HistorialPedidoAdapter.ViewHolder> {
    private List<HistorialPedido> historialPedido;
    private Context context;

    public HistorialPedidoAdapter(List<HistorialPedido> historialPedido, Context context) {
        this.historialPedido = historialPedido;
        this.context = context;
    }

    @NonNull
    @Override
    public HistorialPedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view= inflater.inflate(R.layout.fragment_historial_pedidos_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view) ;
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("idpedido", historialPedido.get(viewHolder.getAdapterPosition()).getIdpedido().toString());
                bundle.putString("observacion", historialPedido.get(viewHolder.getAdapterPosition()).getObservacion().toString());
                bundle.putString("mediopago", historialPedido.get(viewHolder.getAdapterPosition()).getMedioPago().toString());
                bundle.putString("efectivo", historialPedido.get(viewHolder.getAdapterPosition()).getEfectivo().toString());
                bundle.putString("estado", historialPedido.get(viewHolder.getAdapterPosition()).getEstado().toString());
                bundle.putString("calificacion", historialPedido.get(viewHolder.getAdapterPosition()).getCalificacion().toString());
                Navigation.findNavController(view).navigate(R.id.nav_historialPedidoDetalle,bundle);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialPedidoAdapter.ViewHolder holder, int position) {
        holder.tvCodigoPedido.setText("N° PEDIDO: "+historialPedido.get(position).getIdpedido());
        holder.tvDireccionEnvio.setText("DIRECCION: "+historialPedido.get(position).getDireccion());
        holder.tvFechaPedido.setText("FECHA: "+historialPedido.get(position).getFec_registro());
        String estado=historialPedido.get(position).getEstado();
        if(estado.equals("PROCESADO")){
            holder.tvEstado.setBackgroundColor(Color.parseColor("#ff1744"));
            holder.tvEstado.setText(historialPedido.get(position).getEstado());
        }else if(estado.equals("EN CAMINO")){
            holder.tvEstado.setBackgroundColor(Color.parseColor("#ff8f00"));
            holder.tvEstado.setText(historialPedido.get(position).getEstado());
        }else if(estado.equals("ENTREGADO")){
            holder.tvEstado.setBackgroundColor(Color.parseColor("#64dd17"));
            holder.tvEstado.setText(historialPedido.get(position).getEstado());
        }

    }

    @Override
    public int getItemCount() {
        return historialPedido.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView tvCodigoPedido,tvDireccionEnvio,tvFechaPedido,tvEstado;
        private LinearLayout view_container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigoPedido=itemView.findViewById(R.id.tvCodigoPedido);
            tvDireccionEnvio=itemView.findViewById(R.id.tvDireccionEntrega);
            tvFechaPedido=itemView.findViewById(R.id.tvFechaRegistro);
            tvEstado=itemView.findViewById(R.id.tvEstado);
            view_container=itemView.findViewById(R.id.contenedorHistorial);
        }
    }
}
