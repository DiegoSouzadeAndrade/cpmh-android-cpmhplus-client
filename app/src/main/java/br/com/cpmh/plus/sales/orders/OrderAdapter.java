package br.com.cpmh.plus.sales.orders;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.cpmh.plus.R;

class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements Filterable
{
	private	List<Order> orderList;
	private List<Order> filteredOrderList;
	private ConstraintSet collapsedConstraintSet;
	private ConstraintSet expandedConstraintSet;
	private ConstraintLayout constraintLayout;
	private boolean expanded;

	public OrderAdapter(List<Order> orderList)
	{
		this.orderList = orderList;
		this.filteredOrderList = orderList;
		collapsedConstraintSet = new ConstraintSet();
		expandedConstraintSet = new ConstraintSet();
		expanded = false;
	}

	@NonNull
	@Override
	public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position)
	{
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_order_condensed, viewGroup, false);
		constraintLayout = (ConstraintLayout) view;

		collapsedConstraintSet.clone(constraintLayout);
		expandedConstraintSet.clone(constraintLayout.getContext(),R.layout.list_item_order_expanded);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

		viewHolder.itemAdapter = new ItemAdapter(filteredOrderList.get(position).getItems());
		viewHolder.itemsRecyclerView.setAdapter(viewHolder.itemAdapter);
		viewHolder.itemAdapter.notifyDataSetChanged();

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
	public Filter getFilter()
	{
		return null;
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
	{
		private RecyclerView itemsRecyclerView;
		private ItemAdapter itemAdapter;

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

			itemsRecyclerView = itemView.findViewById(R.id.items_recycler_view);
			itemsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

			orderIcon = itemView.findViewById(R.id.product_icon);
			orderType = itemView.findViewById(R.id.order_type);
			customerType =itemView.findViewById(R.id.customer_name);
			patientName = itemView.findViewById(R.id.patient_name);
			orderId = itemView.findViewById(R.id.order_id);
			orderDate= itemView.findViewById(R.id.order_date);
			orderStatus = itemView.findViewById(R.id.order_status);

            itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view)
		{
			TransitionManager.beginDelayedTransition(constraintLayout);
			if(expanded)
			{
				collapsedConstraintSet.applyTo((ConstraintLayout)view);
				expanded = false;
				return;
			}
			else
			{
				expandedConstraintSet.applyTo((ConstraintLayout) view);
				expanded = true;
				return;
			}
		}

		@Override
		public boolean onLongClick(View view)
		{
			return false;
		}
	}

	public class OrderFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint)
		{
			return null;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			filteredOrderList = (ArrayList<Order>) results.values;
			notifyDataSetChanged();
		}
	}
}
