package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.login.LoginService;

public class MyPetActivity extends AppCompatActivity {


    Button MyPetRegisterButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pet);


        MyPetRegisterButton = (Button) findViewById(R.id.MyPetRegisterButton);


        MyPetRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mypetregisterIntent = new Intent(MyPetActivity.this, MyPetRegisterActivity.class);


                MyPetActivity.this.startActivity(mypetregisterIntent);
            }
        });

    }


}
