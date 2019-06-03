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
import ajou.ac.kr.teaming.activity.messageChatting.MessageChattingMainActivity;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class DoigwalkerSerchResultViewActivity extends AppCompatActivity {

    Button ReservationButton;
    Button MainButton;
    ImageView DogwalkerImageText;
    TextView nameText;
    TextView BigcityText;
    TextView siText;
    TextView DongText;
    TextView TimeText;
    TextView InfoText;
    DogwalkerVO dogwalkerVO;
    RegisterVO registerVO;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_result_view);


        ReservationButton=(Button) findViewById(R.id.ReservationButton);
        MainButton=(Button)findViewById(R.id.MainButton);
        DogwalkerImageText=(ImageView)findViewById(R.id.DogwalkerImageText);
        BigcityText=(TextView)findViewById(R.id.BigcityText);
        siText=(TextView)findViewById(R.id.siText);
        DongText=(TextView)findViewById(R.id.DongText);
        TimeText=(TextView)findViewById(R.id.TimeText);
        InfoText=(TextView)findViewById(R.id.InfoText);



        Intent vintent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        DogwalkerImageText.setImageBitmap(bitmap);
        dogwalkerVO=(DogwalkerVO) vintent. getSerializableExtra("DogwalkerVO");



        nameText.setText(dogwalkerVO.getUserID());
        BigcityText.setText(dogwalkerVO.getUserBigcity());
        siText.setText(dogwalkerVO.getUserSmallcity());
        DongText.setText(dogwalkerVO.getUserverySmallcity());
        TimeText.setText(dogwalkerVO.getUserTime());
        InfoText.setText(dogwalkerVO.getUserInfo());





        ReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent 넘겨줄때 registerVO  DogwalkerVO


                Intent message = new Intent(DoigwalkerSerchResultViewActivity.this, MessageChattingMainActivity.class);
                message.putExtra("DogwalkerVO", (Serializable) dogwalkerVO);
                message.putExtra("RegisterVO", registerVO);


                DoigwalkerSerchResultViewActivity.this.startActivity(message);
            }
        });

        MainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent message = new Intent(DoigwalkerSerchResultViewActivity.this, MyPet2Activity.class);
                DoigwalkerSerchResultViewActivity.this.startActivity(message);

            }
        });




    }
}
