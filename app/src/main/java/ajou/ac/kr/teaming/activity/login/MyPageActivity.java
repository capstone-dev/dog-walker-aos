package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.activity.gps.GpsMainActivity;
import ajou.ac.kr.teaming.activity.reservation.ReservationActivity;

public class MyPageActivity extends AppCompatActivity {

    Button DogwalkerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

       DogwalkerButton = (Button) findViewById(R.id.DogwalkerButton);

       DogwalkerButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent DogwalkerIntent=new Intent(MyPageActivity.this, MainActivity.class);
               MyPageActivity.this.startActivity(DogwalkerIntent);
           }
       });



    }



}
