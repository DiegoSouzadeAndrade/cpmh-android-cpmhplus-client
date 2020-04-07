package br.com.cpmh.plus.sales.orders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.cpmh.plus.R;

/**
 *
 */
public class OrdersFragment extends Fragment implements EventListener<QuerySnapshot>
{
	private static final String TAG = "OrdersFragment";
	private FirebaseFirestore firestore;

	private List<Order> orderList;
	private OrderAdapter orderAdapter;

	private RecyclerView recyclerView;

	/**
	 *
	 */
	public OrdersFragment() {}

	/**
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_orders, container, false);
	}

	/**
	 * @param view
	 * @param savedInstanceState
	 */
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		firestore = FirebaseFirestore.getInstance();

		orderList = new ArrayList<>();
		orderAdapter = new OrderAdapter(orderList);

		recyclerView = view.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(orderAdapter);

		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		retrieveOrders();
	}

	/**
	 *
	 */
	public void retrieveOrders()
	{
		firestore.collection("orders").addSnapshotListener(this);
	}

	@Override
	public void onEvent(@javax.annotation.Nullable QuerySnapshot snapshot, @javax.annotation.Nullable FirebaseFirestoreException exception)
	{
		if (exception != null)
		{
			Log.w(TAG, "Listen failed.", exception);
			return;
		}
		else
		{
			for (QueryDocumentSnapshot documentSnapshot : snapshot)
			{
				orderList.add(documentSnapshot.toObject(Order.class));
			}

			orderAdapter.notifyDataSetChanged();
		}
	}
}
