package com.cs407.lab9app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int imageIndex = 1;
    private final int maxIndex = 6;
    private static final int REQUEST_CAMERA_PERMISSIONS = 1;
    private static final int IMAGE_CAPTURE_CODE = 2;
    private ImageView imageHolder;
    private TextView textOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageHolder = findViewById(R.id.imageHolder);
        textOutput = findViewById(R.id.textOutput);
        textOutput.setShowSoftInputOnFocus(false);
        textOutput.setFocusable(false);
    }

    // The purpose of this method is to perform text recognition on an image displayed in an ImageView
    public void onText(View view) {

    }

    // The purpose of this method is to perform image labeling on an image displayed in an ImageView
    public void onLabel(View view) {

    }

    // The purpose of this method is to perform face detection on an image displayed in an ImageView
    public void onFace(View view) {

    }

    private void toTextBox(String label, Object value) {
        textOutput.append(label + ": " + value + "\n");
    }

    private void drawBox(Rect bounds, String label, int boxColor, int textColor) {
        DrawingView drawingView = new DrawingView(getApplicationContext(), bounds, label, boxColor, textColor);
        Bitmap bitmap = ((BitmapDrawable)imageHolder.getDrawable()).getBitmap();
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        drawingView.draw(new Canvas(mutableBitmap));
        runOnUiThread(() -> imageHolder.setImageBitmap(mutableBitmap));
    }

    private void drawLine(List<PointF> points, int lineColor) {
        DrawingLineView drawingView = new DrawingLineView(getApplicationContext(), points, lineColor);
        Bitmap bitmap = ((BitmapDrawable)imageHolder.getDrawable()).getBitmap();
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        drawingView.draw(new Canvas(mutableBitmap));
        runOnUiThread(() -> imageHolder.setImageBitmap(mutableBitmap));
    }

    private void addImage(float x, float y, Rect bounds, float angle, String fileName) {
        ImageView img = new ImageView(this);
        int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
        img.setImageResource(resID);
        FrameLayout frame = findViewById(R.id.frame);
        frame.addView(img);
        ViewGroup.LayoutParams params = img.getLayoutParams();
        params.height = bounds.height();
        params.width = bounds.width();
        img.setLayoutParams(params);
        img.setVisibility(View.VISIBLE);
        img.setX(x - (bounds.width() / 2));
        img.setY(y - (bounds.height() / 2));
        img.setRotation(angle);
        img.bringToFront();
    }

    public void launchCamera(View view) {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS);
        } else {
            Intent cIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cIntent, IMAGE_CAPTURE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cIntent, IMAGE_CAPTURE_CODE);
                }
                return;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageHolder.setImageBitmap(bitmap);
            }
        }
    }

    public void onNext(View view) {
        imageIndex++;
        if (imageIndex > maxIndex) {
            imageIndex = 1;
        }
        int resID = getResources().getIdentifier("pic" + imageIndex, "drawable", getPackageName());
        imageHolder.setImageResource(resID);
        textOutput.setText("");
        FrameLayout frame = findViewById(R.id.frame);
        frame.removeAllViews();
        frame.addView(imageHolder);
    }

    public void onPrev(View view) {
        imageIndex--;
        if (imageIndex <= 0) {
            imageIndex = maxIndex;
        }
        int resID = getResources().getIdentifier("pic" + imageIndex, "drawable", getPackageName());
        imageHolder.setImageResource(resID);
        textOutput.setText("");
        FrameLayout frame = findViewById(R.id.frame);
        frame.removeAllViews();
        frame.addView(imageHolder);
    }
}


