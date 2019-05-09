package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;



public class LoginMainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        TextView registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(LoginMainActivity.this,RegisterActivity.class);
                LoginMainActivity.this.startActivity(registerIntent);
            }
        });

        TextView loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(LoginMainActivity.this, MainActivity.class);
                LoginMainActivity.this.startActivity(loginIntent);
            }
        });


    }
}
