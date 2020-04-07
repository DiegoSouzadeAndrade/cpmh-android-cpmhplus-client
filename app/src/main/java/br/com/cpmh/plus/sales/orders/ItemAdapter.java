package br.com.cpmh.plus.sales.orders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import br.com.cpmh.plus.R;

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>
{
	private List<OrderDetail> itemList;


	public ItemAdapter(List<OrderDetail> itemList)
	{
		this.itemList = itemList;
	}


	@NonNull
	@Override
	public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_name, viewGroup, false);
		return new ItemAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ItemAdapter.ViewHolder viewHolder, int position)
	{
		viewHolder.itemName.setText(itemList.get(position).getProductName());
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
		private TextView itemName;

		public ViewHolder(@NonNull View itemView)
		{
			super(itemView);

			itemName = itemView.findViewById(R.id.item_name);
		}
	}
}
