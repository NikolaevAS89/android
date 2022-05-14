package ru.timestop.android.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.FaceDetector;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import ru.timestop.android.myapplication.R;

public class CameraActivity extends AppCompatActivity {
    private final static String CAMERA_LOG_TAG = "Camera:";


    ImageView posterImageView;

    private CameraManager cameraManager = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
        //shouldShowRequestPermissionRationale
        boolean isCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        Log.d(CAMERA_LOG_TAG, String.valueOf(isCamera));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        posterImageView = this.findViewById(R.id.image);
        //
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameras = null;
            cameras = new String[cameraManager.getCameraIdList().length];

            // выводим информацию по камере
            for (String cameraID : cameraManager.getCameraIdList()) {
                Log.i(CAMERA_LOG_TAG, "cameraID: " + cameraID);
                int id = Integer.parseInt(cameraID);

                // Получениe характеристик камеры
                CameraCharacteristics cc = cameraManager.getCameraCharacteristics(cameraID);
                // Получения списка выходного формата, который поддерживает камера
                StreamConfigurationMap configurationMap =
                        cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                //  Определение какая камера куда смотрит
                int Faceing = cc.get(CameraCharacteristics.LENS_FACING);

                if (Faceing == CameraCharacteristics.LENS_FACING_FRONT) {
                    Log.i(CAMERA_LOG_TAG, "Camera with ID: " + cameraID + "  is FRONT CAMERA  ");
                }

                if (Faceing == CameraCharacteristics.LENS_FACING_BACK) {
                    Log.i(CAMERA_LOG_TAG, "Camera with: ID " + cameraID + " is BACK CAMERA  ");
                }


                // Получения списка разрешений которые поддерживаются для формата jpeg
                Size[] sizesJPEG = configurationMap.getOutputSizes(ImageFormat.JPEG);

                if (sizesJPEG != null) {
                    for (Size item : sizesJPEG) {
                        Log.i(CAMERA_LOG_TAG, "w:" + item.getWidth() + " h:" + item.getHeight());
                    }
                } else {
                    Log.i(CAMERA_LOG_TAG, "camera don`t support JPEG");
                }
            }
        } catch (CameraAccessException e) {
            Log.e(CAMERA_LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
        //
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap myBitmap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.asd,
                options);

        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(myBitmap, 0, 0, null);

        FaceDetector faceDetector = new FaceDetector(myBitmap.getWidth(), myBitmap.getHeight(), 2);
        FaceDetector.Face[] faces = new FaceDetector.Face[2];
        int a = faceDetector.findFaces(myBitmap, faces);
        Paint myPaint = new Paint();
        myPaint.setColor(Color.GREEN);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3);
        if (a > 0) {
            for (int i = 0; i < 1; i++) {
                FaceDetector.Face face = faces[i];

                PointF myMidPoint = new PointF();
                face.getMidPoint(myMidPoint);
                float myEyesDistance = face.eyesDistance();
                canvas.drawRect((int) (myMidPoint.x - myEyesDistance * 1.25),
                        (int) (myMidPoint.y - myEyesDistance * 1.25),
                        (int) (myMidPoint.x + myEyesDistance * 1.25),
                        (int) (myMidPoint.y + myEyesDistance * 1.25), myPaint);
            }
        } else {
            canvas.drawCircle(10, 10, 10, myPaint);
        }
        posterImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }
}
