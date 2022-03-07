package ru.timestop.android.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {


    private class myView extends View {

        public myView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {


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
            canvas.getMaximumBitmapHeight();
            canvas.getMaximumBitmapWidth();

            canvas.drawBitmap(tempBitmap, 0, 0, null);

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
                    canvas.drawRect((int) (myMidPoint.x - myEyesDistance * 2.5),
                            (int) (myMidPoint.y - myEyesDistance * 2.5),
                            (int) (myMidPoint.x + myEyesDistance * 2.5),
                            (int) (myMidPoint.y + myEyesDistance * 2.5), myPaint);
                }
            } else {
                canvas.drawCircle(10, 10, 10, myPaint);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        /*boolean isCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        Log.d("It's mine", "Camera is : " + isCamera);*/

        super.onCreate(savedInstanceState);

        setContentView(new myView(this));
    }
}
