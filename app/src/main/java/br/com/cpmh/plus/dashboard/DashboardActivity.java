package br.com.cpmh.plus.dashboard;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

import javax.annotation.Nullable;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.logistical.StorageActivity;
import br.com.cpmh.plus.marketing.AddSuspectActivity;
import br.com.cpmh.plus.sales.orders.OrdersActivity;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener,EventListener<DocumentSnapshot>
{
	private static final String TAG = "DashboardActivity";
	private FirebaseAuth auth;
	private FirebaseFirestore firestore;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		firestore = FirebaseFirestore.getInstance();
		auth = FirebaseAuth.getInstance();

		Button ordersButton = findViewById(R.id.orders_Button);
		Button storageButton = findViewById(R.id.storage_Button);
		ImageView addSuspectButton = findViewById(R.id.add_suspect_Button);
		Button cameraButton =  findViewById(R.id.cameraButton);
		ordersButton.setOnClickListener(this);
		addSuspectButton.setOnClickListener(this);
		storageButton.setOnClickListener(this);
		cameraButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.orders_Button:
				Intent suspectIntent = new Intent(this, OrdersActivity.class);
				startActivity(suspectIntent);
				break;
			case R.id.storage_Button:
				Intent storageIntent = new Intent(this, StorageActivity.class);
				startActivity(storageIntent);
				break;
			case R.id.add_suspect_Button:
				Intent addSuspectIntent = new Intent(this, AddSuspectActivity.class);
				startActivity(addSuspectIntent);
				break;
			case R.id.cameraButton:
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
				break;
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == this.RESULT_OK){

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
							//firestore.collection("orders").whereEqualTo("number",rawValue).addSnapshotListener();

						}
                }});
    }

	@Override
	public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

	}
}
