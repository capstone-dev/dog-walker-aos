package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.activity.messageChatting.MessageChattingMainActivity;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class DoigwalkerSerchResultViewActivity extends AppCompatActivity {

    Button ReservationButton;
    Button MainButton;
    TextView nameText;
    TextView BigcityText;
    TextView siText;
    TextView DongText;
    TextView TimeText;
    TextView InfoText;
    DogwalkerVO dogwalkerVO;
    RegisterVO registerVO;

    String username;
    String userbigcity;
    String usersmallcity;
    String userverysmallcity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_result_view);


        ReservationButton=(Button) findViewById(R.id.ReservationButton);
        MainButton=(Button)findViewById(R.id.MainButton);
        nameText=(TextView)findViewById(R.id.nameText);
        BigcityText=(TextView)findViewById(R.id.BigcityText);
        siText=(TextView)findViewById(R.id.siText);
        DongText=(TextView)findViewById(R.id.DongText);
        TimeText=(TextView)findViewById(R.id.TimeText);
        InfoText=(TextView)findViewById(R.id.InfoText);



        Intent intent = getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");
        username=intent.getStringExtra("UserName");
        userbigcity=intent.getStringExtra("UserBigcity");
        usersmallcity=intent.getStringExtra("UserSmallcity");
        String userinfo =intent.getStringExtra("UserInfo");
        userverysmallcity=intent.getStringExtra("UserverySmallcity");
        String usertime=intent.getStringExtra("UserTime");


        nameText.setText(username);
        BigcityText.setText(userbigcity);
        siText.setText(usersmallcity);
        DongText.setText(userverysmallcity);
        TimeText.setText(usertime);
        InfoText.setText(userinfo);







        ReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent 넘겨줄때 registerVO  DogwalkerVO


                Intent message = new Intent(DoigwalkerSerchResultViewActivity.this, MessageChattingMainActivity.class);
                message.putExtra("UserName",username);
                message.putExtra("userbigcity",userbigcity);
                message.putExtra("usersmallcity",usersmallcity);
                message.putExtra("userverysmallcity",userverysmallcity);
                message.putExtra("activityName","도그워커예약");
                message.putExtra("DogwalkerVO", (Serializable) dogwalkerVO);
                message.putExtra("RegisterVO", registerVO);


                DoigwalkerSerchResultViewActivity.this.startActivity(message);
            }
        });

        MainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(DoigwalkerSerchResultViewActivity.this, MainActivity.class);
                intent1.putExtra("registerVO",registerVO);
                DoigwalkerSerchResultViewActivity.this.startActivity(intent1);

            }
        });




    }
}
