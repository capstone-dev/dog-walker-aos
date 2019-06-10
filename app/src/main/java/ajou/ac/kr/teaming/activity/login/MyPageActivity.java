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
import ajou.ac.kr.teaming.activity.gps.GpsMainActivity;
import ajou.ac.kr.teaming.activity.reservation.ReservationActivity;
import ajou.ac.kr.teaming.service.login.LoginService;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class MyPageActivity extends AppCompatActivity {

    Button DogwalkerButton;
    RegisterVO registerVO;
   TextView idText;
   TextView nameText;
   TextView EmailText;
   TextView PhoneNumberText;
   TextView GenderText;
   TextView BigcityText;
   Button ModifyButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);




       DogwalkerButton = (Button) findViewById(R.id.DogwalkerButton);
       ModifyButton=(Button)findViewById(R.id.ModifyButton);
       idText=(TextView)findViewById(R.id.idText);
       nameText=(TextView)findViewById(R.id.nameText);
       EmailText=(TextView)findViewById(R.id.EmailText);
       PhoneNumberText=(TextView)findViewById(R.id.PhoneNumberText);
       GenderText=(TextView)findViewById(R.id.GenderText);
       BigcityText=(TextView)findViewById(R.id.BigcityText);


        Intent intent =getIntent();
        RegisterVO registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");
        idText.setText(registerVO.getUserID());
        nameText.setText(registerVO.getUserName());
        EmailText.setText(registerVO.getUserEmail());
        PhoneNumberText.setText(registerVO.getUserPhoneNumber());
        GenderText.setText(registerVO.getUserGender());
        BigcityText.setText(registerVO.getUserBigcity());




        DogwalkerButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent DogwalkerIntent=new Intent(MyPageActivity.this, DogwalkerRegister.class);
               DogwalkerIntent.putExtra("registerVO", registerVO);
               MyPageActivity.this.startActivity(DogwalkerIntent);
           }
       });

        ModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bIntent=new Intent(MyPageActivity.this,MyPageModifyActivity.class);
                bIntent.putExtra("registerVO", registerVO);
                MyPageActivity.this.startActivity(bIntent);
            }
        });


    }



}
