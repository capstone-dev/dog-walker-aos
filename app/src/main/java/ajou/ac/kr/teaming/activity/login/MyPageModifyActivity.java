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
    EditText idText;
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


        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("RegisterVO");
        idText.setText(registerVO.getUserID());
        nameText.setText(registerVO.getUserName());
        EmailText.setText(registerVO.getUserEmail());
        PhoneNumberText.setText(registerVO.getUserPhoneNumber());
        GenderText.setPrompt(registerVO.getUserGender());
        BigcityText.setPrompt(registerVO.getUserBigcity());

        MyPageModifySevice myPageModifySevice= ServiceBuilder.create(MyPageModifySevice.class);


        UserGender = (Spinner) findViewById(R.id.UserGender);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        UserGender.setAdapter(adapter3);
        UserBigcity = (Spinner) findViewById(R.id.UserBigcity);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.bigcity, android.R.layout.simple_spinner_dropdown_item);
        UserBigcity.setAdapter(adapter1);
        idText = (EditText) findViewById(R.id.idText);
        nameText = (EditText) findViewById(R.id.nameText);
        EmailText = (EditText) findViewById(R.id.EmailText);
        PhoneNumberText = (EditText) findViewById(R.id.PhoneNumberText);
        UserGender = (Spinner) findViewById(R.id.GenderText);
        UserBigcity = (Spinner) findViewById(R.id.BigcityText);
        ModifyconformButton = (Button) findViewById(R.id.ModifyconformButton);





        ModifyconformButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String userid = idText.getText().toString();
                String username = nameText.getText().toString();
                String useremail = EmailText.getText().toString();
                String usernumber = PhoneNumberText.getText().toString();
                String usergender = UserGender.getSelectedItem().toString();
                String userbigcity = UserBigcity.getSelectedItem().toString();


                HashMap<String, Object> inputregister = new HashMap<>();
                inputregister.put("UserID", ((EditText) findViewById(R.id.idText)).getText().toString());
                inputregister.put("UserPassword", ((EditText) findViewById(R.id.passwordText)).getText().toString());
                inputregister.put("UserName", ((EditText) findViewById(R.id.nameText)).getText().toString());
                inputregister.put("UserEmail", ((EditText) findViewById(R.id.emailText)).getText().toString());
                inputregister.put("UserGender", ((Spinner) findViewById(R.id.UserGender)).getSelectedItem().toString());
                inputregister.put("UserBigcity", ((Spinner) findViewById(R.id.UserBigcity)).getSelectedItem().toString());
                inputregister.put("UserPhoneNumber", ((EditText) findViewById(R.id.numberText)).getText().toString());


               Call<RegisterVO> registerVOCall = myPageModifySevice.postModify(inputregister);
                registerVOCall.enqueue(new Callback<RegisterVO>() {
                    @Override
                    public void onResponse(Call<RegisterVO> call, Response<RegisterVO> response) {

                        RegisterVO RegisterVO2=response.body();
                        Log.d("TEST", RegisterVO2.getUserID());
                        Log.d("TEST", RegisterVO2.getUserPassword());
                        Log.d("TEST", RegisterVO2.getUserName());
                        Log.d("TEST", RegisterVO2.getUserEmail());
                        Log.d("TEST", RegisterVO2.getUserPhoneNumber());
                        Log.d("TEST", RegisterVO2.getUserGender());
                        Log.d("TEST", RegisterVO2.getUserBigcity());

                        Toast.makeText(MyPageModifyActivity.this, "성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyPageModifyActivity.this, MyPageActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Call<RegisterVO> call, Throwable t) {

                        Log.d("TEST", "통신 실패");

                    }
                });

            }
        });



    }
}
