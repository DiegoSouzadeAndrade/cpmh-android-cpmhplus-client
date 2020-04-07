package br.com.cpmh.plus.logistical;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.sales.orders.OrderDetail;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>
{
    private List<OrderDetail> itemList;


    public OrderDetailAdapter(List<OrderDetail> itemList)
    {
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_detail, viewGroup, false);
        return new OrderDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailAdapter.ViewHolder viewHolder, int position)
    {

        viewHolder.productName.setText(itemList.get(position).getProductName());
        viewHolder.amount.setText(itemList.get(position).getAmount().toString());
        viewHolder.sku.setText(itemList.get(position).getSku());
        viewHolder.partNumber.setText(itemList.get(position).getPartNumber());
        viewHolder.anvisa.setText(itemList.get(position).getAnvisa());
        viewHolder.manufacturer.setText(itemList.get(position).getManufacturer());
        viewHolder.manufacturerTaxpayerNumber.setText(itemList.get(position).getManufacturerTaxpayerNumber());
    }

    @Override
    public int getItemCount()
    {
        if(itemList == null)
        {
            return 0;
        }
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private CheckBox productName;
        private TextView amount;
        private TextView sku;
        private TextView partNumber;
        private TextView anvisa;
        private TextView manufacturer;
        private TextView manufacturerTaxpayerNumber;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productName = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            sku = itemView.findViewById(R.id.sku);
            partNumber = itemView.findViewById(R.id.productNumber);
            anvisa = itemView.findViewById(R.id.anvisa);
            manufacturer = itemView.findViewById(R.id.manufacturer);
            manufacturerTaxpayerNumber = itemView.findViewById(R.id.manufacturerTaxpayerNumber);
        }
    }
}
