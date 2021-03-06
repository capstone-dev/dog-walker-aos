package ajou.ac.kr.teaming.activity.gps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class DogwalkerGpsResult extends AppCompatActivity {

    private TextView txtResultTitle;
    private TextView txtResultPlain;
    private TextView txtDogName;
    private TextView txtTotalWalkDistance;
    private TextView txtTotalWalkTIme;
    private TextView inputDogName;
    private TextView inputPhotoOfTimes;
    private TextView inputWalkDistance;
    private TextView inputWalkTime;
    private Button btnGoToMain;
    private ImageView imgWalkScreenshot;
    private RegisterVO registerVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_gps_result);


        txtResultTitle = (TextView) findViewById(R.id.txtResultTitle);
        txtResultPlain = (TextView) findViewById(R.id.txtResultPlain);
      //  txtDogName = (TextView) findViewById(R.id.txtDogName);
        txtTotalWalkDistance = (TextView) findViewById(R.id.txtTotalWalkDistance);
        txtTotalWalkTIme = (TextView) findViewById(R.id.txtTotalWalkTIme);
     //   inputDogName = (TextView) findViewById(R.id.inputDogName);
        inputPhotoOfTimes = (TextView) findViewById(R.id.inputPhotoOfTimes);
        inputWalkDistance = (TextView) findViewById(R.id.inputWalkDistance);
        inputWalkTime = (TextView) findViewById(R.id.inputWalkTime);
        btnGoToMain = (Button) findViewById(R.id.btnGoToMain);
        imgWalkScreenshot = (ImageView) findViewById(R.id.imgWalkScreenshot);


        Intent intent = getIntent();

        registerVo = (RegisterVO) intent.getSerializableExtra("RegisrerVo");

//        byte[] captureMapImage = intent.getExtras().getByteArray("captureMapimage");
//        //byte배열을 비트맵으로 변환
//        Bitmap mapBitmap = BitmapFactory.decodeByteArray( captureMapImage, 0, captureMapImage.length ) ;
//        imgWalkScreenshot.setImageBitmap(mapBitmap);

        Double walkDistance = intent.getExtras().getDouble("totalWalkDistance");
        String walkDistanceContext = String.format("%.2f",walkDistance);
        inputWalkDistance.setText(walkDistanceContext + "m");

        Long walkTime = intent.getExtras().getLong("totalWalkTime");
        Date walkdate = new Date(walkTime);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(walkdate);
        inputWalkTime.setText(formatDate.substring(0,8));    // TextView 에 현재 시간 문자열 할당

        Integer photoTImes = intent.getExtras().getInt("PhotoTImes");
        inputPhotoOfTimes.setText(photoTImes + "회");


        btnGoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                intent1.putExtra("registerVO", registerVo);
                startActivity(intent1);
            }
        });
    }






}
