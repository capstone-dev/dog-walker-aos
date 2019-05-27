package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.LoginService;
import ajou.ac.kr.teaming.service.userCommunity.UserCommunityThreadRegisterService;
import ajou.ac.kr.teaming.vo.LoginVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginMainActivity extends AppCompatActivity {


    EditText idText;
    EditText passwordText;
    Button loginButton;
    TextView registerButton;
    LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);


        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (TextView) findViewById(R.id.registerButton);
        loginService = ServiceBuilder.create(LoginService.class);


        loginButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                String userid = idText.getText().toString();
                String userpassword = passwordText.getText().toString();

                //Validate form
                if (ValidateLogin(userid, userpassword)) {

                    //dologin
                    doLogin(userid, userpassword);
                }


            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginMainActivity.this, PetRegisterActivity.class);


                LoginMainActivity.this.startActivity(loginIntent);
            }
        });
    }


        private boolean ValidateLogin (String userid, String userpassword){
            if (userid == null || userid.trim().isEmpty()) {
                Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (userpassword == null || userpassword.trim().isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;

        }

        private void doLogin ( final String userid, final String userpassword){

            Call<RegisterVO> call = loginService.DoLogin(userid, userpassword);
            call.enqueue(new Callback<RegisterVO>() {
                @Override
                public void onResponse(Call<RegisterVO> call, Response<RegisterVO> response) {
                    if (response.isSuccessful()) {
                        RegisterVO registerVO = response.body();


                        if (registerVO != null) {
                            Log.d("TEST", registerVO.getUserID());
                            Log.d("TEST", registerVO.getUserPassword());

                        }

                        Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                        intent.putExtra("registerVO", registerVO);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<RegisterVO> call, Throwable t) {
                    Log.d("TEST", "통신 실패");
                    Toast.makeText(LoginMainActivity.this, "Error", Toast.LENGTH_SHORT).show();


                }
            });
        }
    }
