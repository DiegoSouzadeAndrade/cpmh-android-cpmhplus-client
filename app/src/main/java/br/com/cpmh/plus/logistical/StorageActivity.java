package br.com.cpmh.plus.logistical;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.logistical.StorageOrdersFragment;
import br.com.cpmh.plus.sales.orders.Order;

public class StorageActivity extends AppCompatActivity {

    private Order thisOrder;

    public Order getThisOrder() {
        return thisOrder;
    }

    public void setThisOrder(Order thisOrder) {
        this.thisOrder = thisOrder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.storage_fragment,new StorageOrdersFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
