package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
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

import java.io.Serializable;
import java.util.HashMap;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.DogwalkerRegisterService;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogwalkerModify2Activity extends AppCompatActivity {

    Button DogwalkerButton;
    TextView idText;
   Spinner BigcityText;
    EditText siText,DayText;
    EditText Dong1Text;
    Spinner Time3Spinner;
    ArrayAdapter<CharSequence> adapter3,adapter;
    ImageView DogwalkerImage;
    EditText InfoText;
    Button DogwalkersendButton;
    RegisterVO registerVO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_modify2);



        idText = (TextView) findViewById(R.id.idText);
        DogwalkerButton = (Button) findViewById(R.id.DogwalkerButton);
        siText = (EditText) findViewById(R.id.siText);
        Dong1Text = (EditText) findViewById(R.id.Dong1Text);
        DogwalkerRegisterService dogwalkerRegisterService= ServiceBuilder.create(DogwalkerRegisterService.class);
        InfoText=(EditText)findViewById(R.id.InfoText);
        DayText=(EditText)findViewById(R.id.DayText);


        Time3Spinner = (Spinner) findViewById(R.id.Time3Spinner);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.Time, android.R.layout.simple_spinner_dropdown_item);
        Time3Spinner.setAdapter(adapter3);
        BigcityText = (Spinner) findViewById(R.id.BigcityText);
        adapter = ArrayAdapter.createFromResource(this, R.array.bigcity, android.R.layout.simple_spinner_dropdown_item);
        BigcityText.setAdapter(adapter);



        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");


        idText.setText(registerVO.getUserID());
        BigcityText.setPrompt(registerVO.getUserBigcity());
        siText.setText(registerVO.getUserSmallcity());
        DayText.setText(registerVO.getUserDay());
        Time3Spinner.setPrompt(registerVO.getUserTime());
        InfoText.setText(registerVO.getUserInfo());
        Dong1Text.setText(registerVO.getUserverySmallcity());


        DogwalkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });





    }


    private boolean ValidateRegister(String userid, String
            userday, String userInfo, String userSmallcity, String userverysmallcity, String usertime,String userbigcity) {
        if (userid == null || userid.trim().isEmpty()) {
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userday == null || userday.trim().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (usertime == null || usertime.trim().isEmpty()) {
            Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userverysmallcity == null || userverysmallcity.trim().isEmpty()) {
            Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userSmallcity == null || userSmallcity.trim().isEmpty()) {
            Toast.makeText(this, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userInfo == null || userInfo.trim().isEmpty()) {
            Toast.makeText(this, "성을 선택하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userbigcity == null || userbigcity.trim().isEmpty()) {
            Toast.makeText(this, "성을 선택하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
}
