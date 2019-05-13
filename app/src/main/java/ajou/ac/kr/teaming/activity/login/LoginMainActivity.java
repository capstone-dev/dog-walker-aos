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
import ajou.ac.kr.teaming.vo.RegisterVO;


public class LoginMainActivity extends AppCompatActivity {


    private EditText idText;
    private EditText passwordText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        RegisterVO RegisterVO;
        EditText idText=(EditText) findViewById(R.id.idText);
        EditText passwordText=(EditText) findViewById(R.id.passwordText);


        TextView registerButton = findViewById(R.id.registerButton);
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


    }
}
