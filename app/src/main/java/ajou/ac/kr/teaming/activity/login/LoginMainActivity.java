package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;

public class LoginMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        TextView regiterButton = findViewById(R.id.registerButton);
        regiterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(LoginMainActivity.this,RegisterActivity.class);
                LoginMainActivity.this.startActivity(registerIntent);
            }
        });
    }
}
