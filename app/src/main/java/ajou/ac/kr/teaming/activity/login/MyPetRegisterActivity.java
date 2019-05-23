package ajou.ac.kr.teaming.activity.login;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import ajou.ac.kr.teaming.R;

public class MyPetRegisterActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pet_register);


        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                Toast.makeText(MyPetRegisterActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                Toast.makeText(MyPetRegisterActivity.this,"권한실패"+ deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("정상서비스를 받으시려면 권한을 승인해 주세요")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


    }



}
