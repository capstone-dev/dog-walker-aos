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
import ajou.ac.kr.teaming.service.login.DogwalkerModify;
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
    EditText siText, DayText;
    EditText Dong1Text;
    Spinner Time3Spinner;
    ArrayAdapter<CharSequence> adapter3, adapter;
    EditText InfoText;
    RegisterVO registerVO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_modify2);


        idText = (TextView) findViewById(R.id.idText);
        DogwalkerButton = (Button) findViewById(R.id.DogwalkerButton);
        siText = (EditText) findViewById(R.id.siText);
        Dong1Text = (EditText) findViewById(R.id.Dong1Text);
        DogwalkerModify dogwalkerModify = ServiceBuilder.create(DogwalkerModify.class);
        InfoText = (EditText) findViewById(R.id.InfoText);
        DayText = (EditText) findViewById(R.id.DayText);


        Time3Spinner = (Spinner) findViewById(R.id.Time3Spinner);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.Time, android.R.layout.simple_spinner_dropdown_item);
        Time3Spinner.setAdapter(adapter3);
        BigcityText = (Spinner) findViewById(R.id.BigcityText);
        adapter = ArrayAdapter.createFromResource(this, R.array.bigcity, android.R.layout.simple_spinner_dropdown_item);
        BigcityText.setAdapter(adapter);


        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("registerVO");


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


                String userid = registerVO.getUserID();
                String userbigcity = BigcityText.getSelectedItem().toString();
                String userSmallcity = siText.getText().toString();
                String userInfo = InfoText.getText().toString();
                String userverysmallcity = Dong1Text.getText().toString();
                String usertime = Time3Spinner.getSelectedItem().toString();
                String userday = DayText.getText().toString();


                HashMap<String, Object> inputregister = new HashMap<>();
                inputregister.put("UserID", registerVO.getUserID());
                inputregister.put("UserBigcity", userbigcity);
                inputregister.put("UserSmallcity", userSmallcity);
                inputregister.put("UserverySmallcity", userverysmallcity);
                inputregister.put("UserTime", usertime);
                inputregister.put("UserInfo", userInfo);
                inputregister.put("UserDay", userday);

                Call<DogwalkerVO> request = dogwalkerModify.ModifyD(inputregister);

                request.enqueue(new Callback<DogwalkerVO>() {
                    @Override
                    public void onResponse(Call<DogwalkerVO> call, Response<DogwalkerVO> response) {


                        DogwalkerVO dogwalkerVO = response.body();

                        Intent vintent = new Intent(DogwalkerModify2Activity.this, MainActivity.class);
                        vintent.putExtra("registerVO", registerVO);
                        startActivity(vintent);
                    }


                    @Override
                    public void onFailure(Call<DogwalkerVO> call, Throwable t) {
                        Intent vintent = new Intent(DogwalkerModify2Activity.this, MainActivity.class);
                        vintent.putExtra("registerVO", registerVO);
                        startActivity(vintent);


                    }

                });

            }
        });


    }

}