package ajou.ac.kr.teaming.activity.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import ajou.ac.kr.teaming.R;

public class CameraPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo);

        Intent intent = getIntent();
        byte[] imageData = intent.getByteArrayExtra("imageBytes");

        ImageView imageView = findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        imageView.setImageBitmap(bitmap);
    }

    public void onClickUpload(View view) {
        Toast.makeText(this, "업로드 하자", Toast.LENGTH_LONG).show();
    }

    public void onClickFinish(View view) {
        finish();
    }
}
