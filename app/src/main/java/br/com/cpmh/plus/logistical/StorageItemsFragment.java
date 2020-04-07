package br.com.cpmh.plus.logistical;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.gson.JsonObject;

import java.util.List;

import javax.annotation.Nullable;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.sales.orders.OrderDetail;


/**
 * A simple {@link Fragment} subclass.
 */
public class StorageItemsFragment extends Fragment implements EventListener<DocumentSnapshot> ,View.OnClickListener{

    private String TAG = "StorageItemsFragment";
    private FirebaseFirestore firestore;
    private List<OrderDetail> itemList;
    private OrderDetailAdapter orderDetailAdapter;
    private RecyclerView recyclerView;
    private String rawValue;

    public StorageItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storage_items, container, false);
    }

    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},1);
        //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},1);
        firestore = FirebaseFirestore.getInstance();

        itemList = ((StorageActivity)getContext()).getThisOrder().getItems();
        orderDetailAdapter = new OrderDetailAdapter(itemList);

        recyclerView = view.findViewById(R.id.order_details_recycler_view);
        recyclerView.setAdapter(orderDetailAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button openCamera = view.findViewById(R.id.cam);
        Button send = view.findViewById(R.id.send);
        
        openCamera.setOnClickListener(this);
        send.setOnClickListener(this);
    }


    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.cam:

                //CameraFragment cameraFragment = new CameraFragment();
                //StorageActivity storageActivity = (StorageActivity) getActivity();
                //cameraFragment.show(storageActivity.getSupportFragmentManager(), "Alert Dialog Fragment");
                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);

                break;
            case R.id.send:
                finalizeStorageProcess();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == getActivity().RESULT_OK){

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                cameraQrCode(bitmap);
            }
        }
    }

    public void cameraQrCode(Bitmap bitmap)
    {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE)
                        .build();

        FirebaseVisionBarcodeDetector firebaseVisionBarcodeDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
        Task<List<FirebaseVisionBarcode>> task = firebaseVisionBarcodeDetector.detectInImage(firebaseVisionImage);
        task.addOnSuccessListener(
                new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes)
                    {
                        for(FirebaseVisionBarcode barcode : firebaseVisionBarcodes)
                        {
                            String rawValue = barcode.getRawValue();
                            //JsonObject jsonObj = gson.fromJson(rawValue, OrderDetail.class);
                        }

                    }});
    }

    private  void compareItens(OrderDetail orderDetail)
    {

    }
    private void finalizeStorageProcess()
    {

    }

}
