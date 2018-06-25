package com.example.ishaycena.googlemaps.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ishaycena.googlemaps.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.Locale;

import static android.provider.UserDictionary.Words.LOCALE;

public class MapsActivity extends AppCompatActivity {

    private static final String TAG = "MapsActivity";
    private static final int RC_CAMERA_PERMISSION = 1;
    private static final int RC_CAMERA_PICTURE = 2;


    // widgets
    ImageView imageView;
    Button btnDetectFace;
    Button btnTakePicture;
    TextView tvSmileProbability;

    // vars
    public FaceDetector faceDetector;
    public Bitmap myBitmap;
    public Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        imageView = findViewById(R.id.imageViewFace);
        btnDetectFace = findViewById(R.id.btnDetect);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        tvSmileProbability = findViewById(R.id.tvSmileProbbility);

        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ishay_3);
//
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
//
        imageView.setImageBitmap(myBitmap);



        faceDetector = new FaceDetector.Builder(MapsActivity.this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        btnDetectFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectFace();
            }
        });

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            RC_CAMERA_PERMISSION);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), "MyAppPhoto.png");
                    uri = Uri.fromFile(file);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    startActivityForResult(cameraIntent, RC_CAMERA_PICTURE);
                }
            }
        });
    }

    private void detectFace(){
        final Paint rectPaint = new Paint();
        rectPaint.setColor(Color.GREEN);
        rectPaint.setStyle(Paint.Style.STROKE);

        final Bitmap tmpBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(tmpBitmap);
        canvas.drawBitmap(myBitmap, 0, 0, null);

        if (!faceDetector.isOperational()){
            Toast.makeText(MapsActivity.this,
                    "Face detection was not yet loaded on your device.\n" +
                            "Please try again later", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onClick: Face Detection failed to load");
            return;
        }

        Frame frame = new Frame.Builder()
                .setBitmap(myBitmap)
                .build();
        SparseArray<Face> sparseFaceArray = faceDetector.detect(frame);
        Log.d(TAG, "onClick: Detected face! num of faces: " + sparseFaceArray.size());

        for (int i = 0; i < sparseFaceArray.size(); i++) {
            Face face = sparseFaceArray.get(i);

            float smileProbability = face.getIsSmilingProbability();
            Log.d(TAG, "onClick: Smile probability: " + smileProbability);
            tvSmileProbability.setText(String.format(Locale.ENGLISH, "Smile: %f", smileProbability));

            float xStart = face.getPosition().x;
            float yStart = face.getPosition().y;

            float xEnd = face.getWidth() + xStart;
            float yEnd = face.getHeight() + yStart;

            RectF rectF = new RectF(xStart, yStart, xEnd, yEnd);
            canvas.drawRoundRect(rectF, 2, 2, rectPaint);
        }
        imageView.setImageDrawable(new BitmapDrawable(getResources(), tmpBitmap));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RC_CAMERA_PICTURE);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == RC_CAMERA_PICTURE && resultCode == Activity.RESULT_OK) {
                try {
                    Log.d(TAG, "onActivityResult: received photo!");
                    Log.d(TAG, "onActivityResult: photo uri is: " + uri.toString());
                    //Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String path = uri.getPath();
                    Toast.makeText(this, "path:\n" + path, Toast.LENGTH_SHORT).show();

                    myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ExifInterface ei = new ExifInterface(path);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(myBitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(myBitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(myBitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = myBitmap;
                    }
                    Drawable d = new BitmapDrawable(getResources(), myBitmap);

                    imageView.setImageDrawable(d);

                }catch (Exception ex){
                    Log.d(TAG, "onActivityResult: error on image capture: " + ex.toString());
                }
            }
        }
    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    }
