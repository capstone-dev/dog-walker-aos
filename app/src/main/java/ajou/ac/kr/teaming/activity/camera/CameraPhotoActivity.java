package ajou.ac.kr.teaming.activity.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.gps.DogwalkerGpsActivity;
import ajou.ac.kr.teaming.activity.gps.DogwalkerGpsCameraBackground;

public class CameraPhotoActivity extends AppCompatActivity {

    private static final String TAG = "CameraPhotoActivity";
    private byte[] imageData; //ByteArray로 이루어져 있음.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo);

        Intent intent = getIntent();
        imageData = intent.getByteArrayExtra("imageBytes");

        ImageView imageView = findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        imageView.setImageBitmap(bitmap);

        Log.e(TAG,"onCreate");


    }

    public void onClickUpload(View view) {

        Intent intent = new Intent(getApplicationContext(), DogwalkerGpsActivity.class);
        intent.putExtra("photoImageBytes",imageData); //ByteArray
        setResult(RESULT_OK,intent);
        finish();
        Toast.makeText(this, "업로드 하자", Toast.LENGTH_LONG).show();
    }

    public void onClickFinish(View view) {
        finish();
    }
}
