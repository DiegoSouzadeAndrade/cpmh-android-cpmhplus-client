package br.com.cpmh.plus.marketing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.util.List;

import br.com.cpmh.plus.R;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder>
{
	private CompoundButton.OnCheckedChangeListener checkedChangeListener;
	private List<String> typeList;
	private RadioButton lastChecked;


	public TypeAdapter(List<String> typeList , CompoundButton.OnCheckedChangeListener checkedChangeListener)
	{
		this.checkedChangeListener = checkedChangeListener;
		this.typeList = typeList;
	}


	@NonNull
	@Override
	public TypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_type, viewGroup, false);
		return new TypeAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final TypeAdapter.ViewHolder viewHolder, int position)
	{
		viewHolder.radioButton.setText(typeList.get(position));
		viewHolder.radioButton.setOnCheckedChangeListener(checkedChangeListener);
	}

	@Override
	public int getItemCount()
	{
		if(typeList == null)
		{
			return 0;
		}
		return typeList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private RadioButton radioButton;

		public ViewHolder(@NonNull View itemView)
		{
			super(itemView);

			radioButton = itemView.findViewById(R.id.radio_button);
		}
	}
}
