package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.LoginService;
import ajou.ac.kr.teaming.service.login.RegisterService;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;

import static retrofit2.converter.gson.GsonConverterFactory.create;

public class MyActivity extends AppCompatActivity {

    TextView idText;
    EditText passwordText;
    Button ModifyButton;
    LoginService loginService;
    RegisterVO registerVO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


            passwordText = (EditText)findViewById(R.id.passwordText);
            ModifyButton = (Button)findViewById(R.id.ModifyButton);
            loginService =ServiceBuilder.create(LoginService.class);
          idText = (TextView)findViewById(R.id.idText);

         Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("RegisterVO");
        idText.setText(registerVO.getUserID());



            ModifyButton.setOnClickListener(new View.OnClickListener(){


                @Override
                public void onClick(View view) {

                    String userid=registerVO.getUserID();

                    String userpassword=passwordText.getText().toString();

                    //Validate form
                    if(ValidateLogin(userid,userpassword)){

                        //dologin
                        doLogin(userid,userpassword);
                    }



                }
            });



        }
    private boolean ValidateLogin(String userid,String userpassword){

        if (userpassword==null ||userpassword.trim().isEmpty()){
            Toast.makeText(this,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private void doLogin(final String userid,final String userpassword){

        Call<RegisterVO> call=loginService.DoLogin(userid,userpassword);

        call.enqueue(new Callback<RegisterVO>() {
            @Override


            public void onResponse(Call<RegisterVO> call, Response<RegisterVO> response) {
                if(response.isSuccessful()){
                    RegisterVO registerVO=response.body();


                    if (registerVO != null) {
                        Log.d("TEST", registerVO.getUserID());
                        Log.d("TEST", registerVO.getUserPassword());

                    }

                    Intent intent=new Intent(MyActivity.this, MyPageActivity.class);
                    intent.putExtra("registerVO", registerVO);
                    startActivity(intent);
                }

            }


            @Override
            public void onFailure(Call<RegisterVO> call, Throwable t) {
                Toast.makeText(MyActivity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });

    }



}
