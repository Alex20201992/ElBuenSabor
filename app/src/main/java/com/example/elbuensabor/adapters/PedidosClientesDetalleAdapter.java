package com.example.elbuensabor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elbuensabor.R;
import com.example.elbuensabor.entities.HistorialPedido;
import com.example.elbuensabor.entities.PedidoClienteDetalle;

import java.text.DecimalFormat;
import java.util.List;

public class PedidosClientesDetalleAdapter extends RecyclerView.Adapter<PedidosClientesDetalleAdapter.ViewHolder> {
    private List<PedidoClienteDetalle> pedidoList;
    private Context context;

    public PedidosClientesDetalleAdapter(List<PedidoClienteDetalle> pedidoList, Context context) {
        this.pedidoList = pedidoList;
        this.context = context;
    }

    @NonNull
    @Override
    public PedidosClientesDetalleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pedidos_clientes_detalle_item,parent,false);
        return new PedidosClientesDetalleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosClientesDetalleAdapter.ViewHolder holder, int position) {
        holder.tvNombrePedidoDetalle.setText(pedidoList.get(position).getNombre());
        int cantidad=pedidoList.get(position).getCantidad();
        double precio=pedidoList.get(position).getPrecio();
        double total=precio*cantidad;
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        holder.tvCantidadPedidoDetalle.setText(""+cantidad);
        holder.tvPrecioPedidoDetalle.setText("S/: "+formato.format(precio));
        holder.tvSubtotalPedidoDetalle.setText("TOTAL: "+formato.format(total));
    }

    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombrePedidoDetalle,tvCantidadPedidoDetalle,tvPrecioPedidoDetalle,tvSubtotalPedidoDetalle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombrePedidoDetalle=itemView.findViewById(R.id.tvNombrePedidoDetalle);
            tvCantidadPedidoDetalle=itemView.findViewById(R.id.tvCantidadPedidoDetalle);
            tvPrecioPedidoDetalle=itemView.findViewById(R.id.tvPrecioPedidoDetalle);
            tvSubtotalPedidoDetalle=itemView.findViewById(R.id.tvSubtotalPedidoDetalle);
        }
    }
}
