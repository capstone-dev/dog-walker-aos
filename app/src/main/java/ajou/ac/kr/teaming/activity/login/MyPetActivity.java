package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ajou.ac.kr.teaming.vo.RegisterVO;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.login.LoginService;



public class MyPetActivity extends AppCompatActivity {


    Button MyPetRegisterButton;
    RegisterVO registerVO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pet);




        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("RegisterVO");


        MyPetRegisterButton = (Button) findViewById(R.id.MyPetRegisterButton);


        MyPetRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mypetregisterIntent = new Intent(MyPetActivity.this, MyPetRegisterActivity.class);
                mypetregisterIntent.putExtra("registerVO", registerVO);
                MyPetActivity.this.startActivity(mypetregisterIntent);


            }
        });



    }


}
