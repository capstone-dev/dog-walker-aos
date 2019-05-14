package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.LoginService;


public class LoginMainActivity extends AppCompatActivity {

    private LoginService LoginService = ServiceBuilder.create(LoginService.class);


    private EditText idText;
    private EditText passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        TextView registerButton = findViewById(R.id.ValidateButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(LoginMainActivity.this,RegisterActivity.class);
                LoginMainActivity.this.startActivity(registerIntent);
            }
        });

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(LoginMainActivity.this, MainActivity.class);
                LoginMainActivity.this.startActivity(loginIntent);
            }
        });



       // Call<RegisterVO>response=LoginService.getsignUp("summy","1234");








    }

}
