package com.example.elbuensabor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elbuensabor.R;
import com.example.elbuensabor.entities.HistorialPedido;

import java.util.List;

public class PedidosRepartidorAdapter extends RecyclerView.Adapter<PedidosRepartidorAdapter.ViewHolder>{
    private List<HistorialPedido> pedidoList;
    private Context context;

    public PedidosRepartidorAdapter(List<HistorialPedido> pedidoList, Context context) {
        this.pedidoList = pedidoList;
        this.context = context;
    }

    @NonNull
    @Override
    public PedidosRepartidorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view= inflater.inflate(R.layout.fragment_pedidos_clientes_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view) ;
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("idpedido", pedidoList.get(viewHolder.getAdapterPosition()).getIdpedido().toString());
                bundle.putString("observacion", pedidoList.get(viewHolder.getAdapterPosition()).getObservacion().toString());
                bundle.putString("mediopago", pedidoList.get(viewHolder.getAdapterPosition()).getMedioPago().toString());
                bundle.putString("efectivo", pedidoList.get(viewHolder.getAdapterPosition()).getEfectivo().toString());
                Navigation.findNavController(view).navigate(R.id.nav_pedido_repartidor_detalle,bundle);

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosRepartidorAdapter.ViewHolder holder, int position) {
        holder.tvCodigoPedidoCliente.setText("N° PEDIDO: "+pedidoList.get(position).getIdpedido());
        holder.tvDireccionEnvioCliente.setText("DIRECCION: "+pedidoList.get(position).getDireccion());
        holder.tvFechaPedidoCliente.setText("FECHA: "+pedidoList.get(position).getFec_registro());
        holder.tvNombreCliente.setText(pedidoList.get(position).getCliente());
        String estado=pedidoList.get(position).getEstado();
        if(estado.equals("PROCESADO")){
            holder.tvEstadoCliente.setBackgroundColor(Color.parseColor("#ff1744"));
            holder.tvEstadoCliente.setText(pedidoList.get(position).getEstado());
        }else if(estado.equals("EN CAMINO")){
            holder.tvEstadoCliente.setBackgroundColor(Color.parseColor("#ff8f00"));
            holder.tvEstadoCliente.setText(pedidoList.get(position).getEstado());
        }else if(estado.equals("ENTREGADO")){
            holder.tvEstadoCliente.setBackgroundColor(Color.parseColor("#64dd17"));
            holder.tvEstadoCliente.setText(pedidoList.get(position).getEstado());
        }
    }

    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCodigoPedidoCliente,tvDireccionEnvioCliente,tvFechaPedidoCliente,tvEstadoCliente,tvNombreCliente;
        private LinearLayout view_container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigoPedidoCliente=itemView.findViewById(R.id.tvCodigoPedidoCliente);
            tvDireccionEnvioCliente=itemView.findViewById(R.id.tvDireccionEntregaCliente);
            tvFechaPedidoCliente=itemView.findViewById(R.id.tvFechaRegistroCliente);
            tvEstadoCliente=itemView.findViewById(R.id.tvEstadoCliente);
            tvNombreCliente=itemView.findViewById(R.id.tvNombreCliente);
            view_container=itemView.findViewById(R.id.contenedorPedido);
        }
    }
}
