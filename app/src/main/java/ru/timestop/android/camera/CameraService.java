package ru.timestop.android.camera;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class CameraService {
    private static final int CAMERA1 = 0;
    private static final int CAMERA2 = 1;
    private static final String SERVICE_LOG_TAG = "CameraService:";

    private CameraService[] myCameras = null;

    private CameraManager mCameraManager = null;
    private String mCameraID;
    private CameraDevice mCameraDevice = null;
    private CameraCaptureSession mCaptureSession;
    private CameraDevice.StateCallback mCameraCallback;

    public CameraService(CameraManager cameraManager, String cameraID) {
        mCameraCallback = new CameraCallBack();
        mCameraManager = cameraManager;
        mCameraID = cameraID;
    }

    public boolean isOpen() {

        if (mCameraDevice == null) {
            return false;
        } else {
            return true;
        }
    }


    public void openCamera(Context context) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCameraManager.openCamera(mCameraID, mCameraCallback, null);
        } catch (CameraAccessException e) {
            Log.i(SERVICE_LOG_TAG, e.getMessage());
        }
    }


    public void closeCamera() {

        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    private static final class CameraCallBack extends CameraDevice.StateCallback {

        private CameraDevice mCameraDevice = null;

        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            Log.i(SERVICE_LOG_TAG, "Open camera  with id:" + mCameraDevice.getId());

            // TODO createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            mCameraDevice.close();

            Log.i(SERVICE_LOG_TAG, "disconnect camera  with id:" + mCameraDevice.getId());
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.i(SERVICE_LOG_TAG, "error! camera id:" + camera.getId() + " error:" + error);
        }
    }

}
