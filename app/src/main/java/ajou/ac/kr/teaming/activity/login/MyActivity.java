package ajou.ac.kr.teaming.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ajou.ac.kr.teaming.R;

public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }
}

class LoginContributor{


    public final String login;

    public  LoginContributor(String login){
        this.login=login;
    }


}