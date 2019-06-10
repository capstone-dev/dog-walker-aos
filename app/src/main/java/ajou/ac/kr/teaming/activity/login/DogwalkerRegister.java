package ajou.ac.kr.teaming.activity.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.activity.gps.PermissionManager;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.DogwalkerRegisterService;
import ajou.ac.kr.teaming.service.login.LoginService;
import ajou.ac.kr.teaming.service.login.MyPetService;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogwalkerRegister extends AppCompatActivity {

    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private Uri imgUri, photoURI, albumURI;
    private String mCurrentPhotoPath;
    RegisterVO registerVO;
    DogwalkerRegisterService dogwalkerRegisterService;
    DogwalkerVO dogwalkerVO;


    Button DogwalkerRegisterButton;
    TextView idText;
    TextView BigcityText;
    EditText siText,DayText;
    EditText Dong1Text;
    Spinner Time3Spinner;
    ArrayAdapter<CharSequence>adapter3;
    ImageView DogwalkerImage;
    EditText InfoText;
    Button DogwalkersendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_register);


        idText = (TextView) findViewById(R.id.idText);
        BigcityText = (TextView) findViewById(R.id.BigcityText);
        DogwalkerRegisterButton = (Button) findViewById(R.id.DogwalkerRegisterButton);
        siText = (EditText) findViewById(R.id.siText);
        Dong1Text = (EditText) findViewById(R.id.Dong1Text);
        DogwalkerRegisterService dogwalkerRegisterService= ServiceBuilder.create(DogwalkerRegisterService.class);
        InfoText=(EditText)findViewById(R.id.InfoText);
        DayText=(EditText)findViewById(R.id.DayText);
        DogwalkersendButton=(Button)findViewById(R.id.DogwalkersendButton);





        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");

        idText.setText(registerVO.getUserID());
        BigcityText.setText(registerVO.getUserBigcity());


        DogwalkersendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent vintent = new Intent(DogwalkerRegister.this, DogwalkerSendActivity.class);
                vintent.putExtra("registerVO",registerVO);
                startActivity(vintent);

            }
        });


        DogwalkerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid = registerVO.getUserID();
                String userbigcity = registerVO.getUserBigcity();
                String userSmallcity = siText.getText().toString();
                String userInfo=InfoText.getText().toString();
                String userverysmallcity =Dong1Text.getText().toString();
                String usertime =Time3Spinner.getSelectedItem().toString();
                String userday= DayText.getText().toString();


                HashMap<String, Object> inputregister = new HashMap<>();
                inputregister.put("UserID",registerVO.getUserID());
                inputregister.put("UserBigcity", userbigcity);
                inputregister.put("UserSmallcity", userSmallcity);
                inputregister.put("UserverySmallcity", userverysmallcity);
                inputregister.put("UserTime", usertime);
                inputregister.put("UserInfo", userInfo);
                inputregister.put("UserDay", userday);



                Call<DogwalkerVO> request =dogwalkerRegisterService.post(inputregister);


                request.enqueue(new Callback<DogwalkerVO>() {
                    @Override
                    public void onResponse(Call<DogwalkerVO> call, Response<DogwalkerVO> response) {
                        if (response.isSuccessful()) {
                            DogwalkerVO dogwalkerVO = response.body();
                            Intent vintent = new Intent(DogwalkerRegister.this, MainActivity.class);
                            vintent.putExtra("UserBigcity",dogwalkerVO.getUserBigcity());
                            vintent.putExtra("UserSmallcity",dogwalkerVO.getUserInfo());
                            vintent.putExtra("UserverySmallcity",dogwalkerVO.getUserverySmallcity());
                            startActivity(vintent);
                        }
                        Log.d("TEST", "onResponse:END ");
                    }

                    @Override
                    public void onFailure(Call<DogwalkerVO> call, Throwable t) {


                        Intent vintent = new Intent(DogwalkerRegister.this, MainActivity.class);
                        vintent.putExtra("registerVO",registerVO);
                        startActivity(vintent);
                    }
                });





            }
        });






        Time3Spinner = (Spinner) findViewById(R.id.Time3Spinner);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.Time, android.R.layout.simple_spinner_dropdown_item);
        Time3Spinner.setAdapter(adapter3);

    }




}


