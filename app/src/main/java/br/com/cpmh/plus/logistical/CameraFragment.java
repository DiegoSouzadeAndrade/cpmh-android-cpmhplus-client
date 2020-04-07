package br.com.cpmh.plus.logistical;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import br.com.cpmh.plus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends DialogFragment implements SurfaceHolder.Callback, Handler.Callback, View.OnClickListener
{
    private String TAG = "CameraFragment";

    static final int MY_PERMISSIONS_REQUEST_CAMERA = 1242;
    private static final int CAMERA_OPENED = 1;
    private static final int SURFACE_READY = 2;
    private final Handler handler = new Handler(this);

    private SurfaceView cameraSurfaceView;
    private SurfaceHolder cameraSurfaceHolder;
    private CameraManager cameraManager;
    private String[] cameraIDList;
    private CameraDevice.StateCallback cameraStateCallBack;
    private CameraDevice cameraDevice;
    boolean surfaceCreated = true;
    boolean isCameraConfigured = false;
    private Surface cameraSurface = null;
    private Button takePictureButton;
    private CameraCaptureSession cameraCaptureSession;

    public CameraFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState)
    {
        cameraSurfaceView = view.findViewById(R.id.cameraSurface);
        cameraSurfaceHolder = cameraSurfaceView.getHolder();
        cameraSurfaceHolder.addCallback(this);
        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        try
        {
            cameraIDList = cameraManager.getCameraIdList();

            for (String id : cameraIDList)
            {
                Log.v(TAG, "CameraID: " + id);
            }

        }
        catch (CameraAccessException cameraAccessException)
        {
            cameraAccessException.printStackTrace();
        }
        cameraStateCallBack = new CameraDevice.StateCallback()
        {
            @Override
            public void onOpened(CameraDevice camera)
            {
                cameraDevice = camera;
                handler.sendEmptyMessage(CAMERA_OPENED);
            }

            @Override
            public void onDisconnected(CameraDevice camera)
            {

            }

            @Override
            public void onError(CameraDevice camera, int error)
            {

            }
        };
        takePictureButton = view.findViewById(R.id.pictureButton);
        takePictureButton.setOnClickListener(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        try
        {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraManager.openCamera(cameraIDList[0], cameraStateCallBack, new Handler());
        } catch (CameraAccessException cameraAccessException) {
            cameraAccessException.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (cameraCaptureSession != null) {
                cameraCaptureSession.stopRepeating();
                cameraCaptureSession.close();
                cameraCaptureSession = null;
            }

            isCameraConfigured = false;
        } catch (final CameraAccessException cameraAccessException ) {
            cameraAccessException.printStackTrace();
        } catch (final IllegalStateException illegalStateException ) {
            illegalStateException.printStackTrace();
        } finally {
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
                cameraCaptureSession = null;
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CAMERA_OPENED:
            case SURFACE_READY:
                // if both surface is created and camera device is opened
                // - ready to set up preview and other things
                if (surfaceCreated && (cameraDevice != null)
                        && !isCameraConfigured) {
                    configureCamera();
                }
                break;
        }
        return true;
    }

    private void configureCamera() {
    // prepare list of surfaces to be used in capture requests
    List<Surface> surfaceList = new ArrayList<Surface>();

    surfaceList.add(cameraSurface); // surface for viewfinder preview

    // configure camera with all the surfaces to be ever used
    try {
        cameraDevice.createCaptureSession(surfaceList,
                new CaptureSessionListener(), null);
    } catch (CameraAccessException cameraAccessException) {
        cameraAccessException.printStackTrace();
    }
    isCameraConfigured = true;
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    try {
                        cameraManager.openCamera(cameraIDList[1], cameraStateCallBack, new Handler());
                    } catch (CameraAccessException cameraAccessException) {
                        cameraAccessException.printStackTrace();
                    }
                break;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        cameraSurface = surfaceHolder.getSurface();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        cameraSurface = surfaceHolder.getSurface();
        surfaceCreated = true;
        handler.sendEmptyMessage(SURFACE_READY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        surfaceCreated = false;
    }

    @Override
    public void onClick(View view)
    {
        takePicture();
        //dismiss();
    }
    private class CaptureSessionListener extends CameraCaptureSession.StateCallback {
        @Override
        public void onConfigureFailed(final CameraCaptureSession session) {
        }

        @Override
        public void onConfigured(final CameraCaptureSession session) {
            cameraCaptureSession = session;
            try {
                CaptureRequest.Builder previewRequestBuilder = cameraDevice
                        .createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                previewRequestBuilder.addTarget(cameraSurface);
                cameraCaptureSession.setRepeatingRequest(previewRequestBuilder.build(),
                        null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
    private void takePicture()
    {
        Bitmap bitmap = Bitmap.createBitmap(cameraSurfaceView.getWidth(), cameraSurfaceView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        cameraSurfaceView.draw(canvas);
        cameraQrCode(bitmap);
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
        task.addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionBarcode>> task)
            {
                if(task.getResult().isEmpty())
                {
                    Log.i(TAG ,"isEmpty");
                }
                else
                {
                    Log.i(TAG ,"notEmpty");

                }
            }
        }).addOnSuccessListener(
                new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes)
                    {
                        if(firebaseVisionBarcodes.isEmpty())
                        {
                            Log.i(TAG ,"isEmpty");
                        }
                        else
                        {
                            Log.i(TAG ,"notEmpty");

                        }
                        for(FirebaseVisionBarcode barcode : firebaseVisionBarcodes)
                        {
                            //rawValue = barcode.getRawValue();
                            Log.i(TAG ,"passou");
                        }

                    }});
    }
}
