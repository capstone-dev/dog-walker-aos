package ajou.ac.kr.teaming.activity.gps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.camera.CameraMainFragment;

public class DogwalkerGpsCamera extends AppCompatActivity {


    private Fragment cameraMianFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_gps_camera);

        cameraMianFragment = new CameraMainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.dogwalkerGpsCamera,cameraMianFragment);
        transaction.commit();
    }
}
