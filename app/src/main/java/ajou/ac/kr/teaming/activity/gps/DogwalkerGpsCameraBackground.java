package ajou.ac.kr.teaming.activity.gps;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.camera.CameraMainFragment;

public class DogwalkerGpsCameraBackground extends AppCompatActivity {
/**
 * 카메라 화면이 전부 다 나올수 있게 새로 액티비티를 만들어 Fragment를 부름
 **/
    private Fragment cameraMianFragment;
    private byte[] imageData; //ByteArray로 이루어져 있음.
    private boolean isCameraActivated = false;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_gps_camera_background);


        if(isCameraActivated != true) {
            isCameraActivated = true;
            cameraMianFragment = new CameraMainFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.dogwalkerGpsCamera,cameraMianFragment);
            transaction.commit();
        }else{
            isCameraActivated = false;
/*            Intent intent = getIntent();
            imageData = intent.getByteArrayExtra("imageBytes");

            Intent intent2 = new Intent(getApplicationContext(), DogwalkerGpsActivity.class);
            intent.putExtra("photoImageBytes",imageData); //ByteArray
            setResult(RESULT_OK,intent2);
            finish();*/
        }



    }
}
