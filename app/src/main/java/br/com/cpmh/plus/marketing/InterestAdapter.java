package br.com.cpmh.plus.marketing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import br.com.cpmh.plus.R;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> implements Filterable
{
	private CompoundButton.OnCheckedChangeListener checkedChangeListener;
	private List<String> interestList;
	private List<String> filteredInterestList;
	private List<CheckBox> checkBoxList;

	public List<CheckBox> getCheckBoxList()
	{
		return checkBoxList;
	}

	public InterestAdapter(List<String> interestList, CompoundButton.OnCheckedChangeListener checkedChangeListener)
	{
		this.checkedChangeListener = checkedChangeListener;
		this.interestList = interestList;
		filteredInterestList = interestList;
	}

	@NonNull
	@Override
	public InterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position)
	{
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_interest, viewGroup, false);

		return new InterestAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull InterestAdapter.ViewHolder viewHolder, int position)
	{
		viewHolder.checkBox.setText(interestList.get(position));
		viewHolder.checkBox.setOnCheckedChangeListener(checkedChangeListener);
	}

	@Override
	public int getItemCount()
	{
		if(filteredInterestList == null)
		{
			return 0;
		}
		return filteredInterestList.size();
	}

	@Override
	public Filter getFilter()
	{
		return null;
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private CheckBox checkBox;

		public ViewHolder(@NonNull View itemView)
		{
			super(itemView);

			checkBox = itemView.findViewById(R.id.check_box);
		}
	}

	public class InterestFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint)
		{
			return null;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			filteredInterestList = (ArrayList<String>) results.values;
			notifyDataSetChanged();
		}
	}

}
