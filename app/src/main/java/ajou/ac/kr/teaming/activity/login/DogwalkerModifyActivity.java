package ajou.ac.kr.teaming.activity.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.DogwalkerThreadService;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogwalkerModifyActivity extends AppCompatActivity {

    TextView idText;
    TextView BigcityText;
    TextView siText,DayText;
    TextView Dong1Text;
    TextView Time3Spinner;
    TextView InfoText;
    RegisterVO registerVO;
    DogwalkerVO dogwalkerVO;
    Button DogwalkerModifyButton;
    private DogwalkerThreadService dogwalkerThreadService = ServiceBuilder.create(DogwalkerThreadService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_modify);


        idText = (TextView) findViewById(R.id.idText);
        BigcityText = (TextView) findViewById(R.id.BigcityText);
        siText = (TextView) findViewById(R.id.siText);
        DayText=(TextView) findViewById(R.id.DayText);
        Dong1Text=(TextView) findViewById(R.id.Dong1Text);
        Time3Spinner=(TextView)findViewById(R.id.Time3Spinner);
        InfoText=(TextView)findViewById(R.id.InfoText);

        DogwalkerModifyButton =(Button)findViewById(R.id.DogwalkerModifyButton);



        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");

        String smallcity = registerVO.getUserSmallcity();
        String verysmallcity=registerVO.getUserverySmallcity();


        Call<DogwalkerVO> request= dogwalkerThreadService.dogwalkerModify(smallcity,verysmallcity);
        request.enqueue(new Callback<DogwalkerVO>() {
            @Override
            public void onResponse(Call<DogwalkerVO> call, Response<DogwalkerVO> response) {
                DogwalkerVO dogwalkerVO=response.body();


            }

            @Override
            public void onFailure(Call<DogwalkerVO> call, Throwable t) {

            }
        });

        idText.setText(registerVO.getUserID());
        BigcityText.setText(registerVO.getUserBigcity());
        siText.setText(registerVO.getUserSmallcity());
        DayText.setText(registerVO.getUserDay());
        Time3Spinner.setText(registerVO.getUserTime());
        InfoText.setText(registerVO.getUserInfo());
        Dong1Text.setText(registerVO.getUserverySmallcity());


        DogwalkerModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent vintent = new Intent(DogwalkerModifyActivity.this, DogwalkerModify2Activity.class);
                vintent.putExtra("registerVO", registerVO);
                startActivity(vintent);

            }
        });



    }
}
