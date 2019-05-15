package ajou.ac.kr.teaming.activity.gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ajou.ac.kr.teaming.R;

public class DogwalkerGpsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_gps);

        Button callButton = (Button)findViewById(R.id.buttonCall);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"통화하기 버튼\n" +
                        "강아지 주인과 통화연결",Toast.LENGTH_LONG).show();
            }
        });

        Button chatButton = (Button)findViewById(R.id.buttonMessage);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"채팅하기 버튼\n" +
                        "강아지 주인과 채팅하기",Toast.LENGTH_LONG).show();
            }
        });

    }
}
