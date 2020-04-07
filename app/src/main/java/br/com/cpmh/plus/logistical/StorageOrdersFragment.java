package br.com.cpmh.plus.logistical;


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

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.sales.orders.Order;


/**
 * A simple {@link Fragment} subclass.
 */
public class StorageOrdersFragment extends Fragment implements EventListener<QuerySnapshot> {

    private static final String TAG = "StorageOrdersFragment";
    private FirebaseFirestore firestore;

    private List<Order> orderList;
    private StorageOrdersAdapter storageOrdersAdapter;

    private RecyclerView recyclerView;

    public StorageOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storage_orders, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        orderList = new ArrayList<>();
        storageOrdersAdapter = new StorageOrdersAdapter(orderList);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(storageOrdersAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        retrieveOrders();
    }

    public void retrieveOrders()
    {
        firestore.collection("orders").whereEqualTo("finished",false).whereEqualTo("stage","Logistica").addSnapshotListener(this);
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

            storageOrdersAdapter.notifyDataSetChanged();
        }
    }
}
