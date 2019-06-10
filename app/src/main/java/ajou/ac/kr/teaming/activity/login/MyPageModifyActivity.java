package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.MyPageModifySevice;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageModifyActivity extends AppCompatActivity {


    RegisterVO registerVO;
    TextView idText;
    EditText nameText;
    EditText EmailText;
    EditText PhoneNumberText;
    Spinner GenderText;
    Spinner BigcityText;
    Button ModifyconformButton;
    Spinner UserGender;
    Spinner UserBigcity;
    ArrayAdapter<CharSequence> adapter3, adapter1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_modify);


        MyPageModifySevice myPageModifySevice= ServiceBuilder.create(MyPageModifySevice.class);

        GenderText = (Spinner) findViewById(R.id.GenderText);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        GenderText.setAdapter(adapter3);


        BigcityText = (Spinner) findViewById(R.id.BigcityText);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.bigcity, android.R.layout.simple_spinner_dropdown_item);
        BigcityText.setAdapter(adapter1);

        idText = (TextView) findViewById(R.id.idText);
        nameText = (EditText) findViewById(R.id.nameText);
        EmailText = (EditText) findViewById(R.id.EmailText);
        PhoneNumberText = (EditText) findViewById(R.id.PhoneNumberText);
        ModifyconformButton = (Button) findViewById(R.id.ModifyconformButton);



        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");

        idText.setText(registerVO.getUserID());
        nameText.setText(registerVO.getUserName());
        EmailText.setText(registerVO.getUserEmail());
        PhoneNumberText.setText(registerVO.getUserPhoneNumber());
        GenderText.setPrompt(registerVO.getUserGender());
        BigcityText.setPrompt(registerVO.getUserBigcity());






        ModifyconformButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                HashMap<String, Object> inputregister = new HashMap<>();
                inputregister.put("UserID", registerVO.getUserID());
                inputregister.put("token",registerVO.getToken());
                inputregister.put("UserPassword",registerVO.getUserPassword());
                inputregister.put("UserName", ((EditText) findViewById(R.id.nameText)).getText().toString());
                inputregister.put("UserEmail", ((EditText) findViewById(R.id.EmailText)).getText().toString());
                inputregister.put("UserGender", ((Spinner) findViewById(R.id.GenderText)).getSelectedItem().toString());
                inputregister.put("UserBigcity", ((Spinner) findViewById(R.id.BigcityText)).getSelectedItem().toString());
                inputregister.put("UserPhoneNumber", ((EditText) findViewById(R.id.PhoneNumberText)).getText().toString());



               Call<RegisterVO> registerVOCall = myPageModifySevice.postModify(inputregister);
                registerVOCall.enqueue(new Callback<RegisterVO>() {
                    @Override
                    public void onResponse(Call<RegisterVO> call, Response<RegisterVO> response) {

                        RegisterVO RegisterVO2=response.body();

                        Toast.makeText(MyPageModifyActivity.this, "성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyPageModifyActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Call<RegisterVO> call, Throwable t) {

                        Log.d("TEST", "통신 실패");
                        Intent intent = new Intent(MyPageModifyActivity.this, MainActivity.class);
                        intent.putExtra("registerVO",registerVO);
                        startActivity(intent);
                    }
                });

            }
        });



    }
}
