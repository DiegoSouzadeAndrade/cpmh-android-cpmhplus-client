package br.com.cpmh.plus.logistical;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.sales.orders.Order;


public class StorageOrdersAdapter extends RecyclerView.Adapter<StorageOrdersAdapter.ViewHolder> implements Filterable {

    private List<Order> orderList;
    private List<Order> filteredOrderList;

    public StorageOrdersAdapter(List<Order> orderList) {
        this.orderList = orderList;
        filteredOrderList = orderList;
    }

    @NonNull
    @Override
    public StorageOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_order_condensed, viewGroup, false);
        return new StorageOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position)
    {
        viewHolder.thisOrder = orderList.get(position);

        //viewHolder.orderIcon.setImageBitmap();
        viewHolder.orderType.setText(filteredOrderList.get(position).getType());
        viewHolder.customerType.setText(filteredOrderList.get(position).getCustomer().getType());
        viewHolder.patientName.setText(filteredOrderList.get(position).getPatientName());
        viewHolder.orderId.setText(filteredOrderList.get(position).getNumber().toString());
        //viewHolder.orderDate.setText(filteredOrderList.get(position).getRegisteredAt().toString());
        viewHolder.orderStatus.setText(filteredOrderList.get(position).getStatus());
    }

    @Override
    public int getItemCount()
    {
        if(filteredOrderList == null)
        {
            return 0;
        }
        return filteredOrderList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private Order thisOrder;

        private ImageView orderIcon;
        private TextView orderType;
        private TextView customerType;
        private TextView patientName;
        private TextView orderId;
        private TextView orderDate;
        private TextView orderStatus;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);


            orderIcon = itemView.findViewById(R.id.product_icon);
            orderType = itemView.findViewById(R.id.order_type);
            customerType =itemView.findViewById(R.id.customer_name);
            patientName = itemView.findViewById(R.id.patient_name);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate= itemView.findViewById(R.id.order_date);
            orderStatus = itemView.findViewById(R.id.order_status);

        }

        @Override
        public void onClick(View view)
        {
            StorageActivity storageActivity =  (StorageActivity)view.getContext();
            storageActivity.setThisOrder(thisOrder);
            FragmentTransaction fragmentTransaction = storageActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.storage_fragment,new StorageItemsFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
