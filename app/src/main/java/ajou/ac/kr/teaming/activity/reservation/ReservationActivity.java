package ajou.ac.kr.teaming.activity.reservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.gps.RealTimeDogWalkerListAcitvity;
import ajou.ac.kr.teaming.activity.gps.RealTimeGpsActivity;
import ajou.ac.kr.teaming.activity.login.SearchActivity;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class ReservationActivity extends AppCompatActivity {

    private RegisterVO registerVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("registerVO");

        ImageView RealTimeImage = findViewById(R.id.RealTimeImage);
        RealTimeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent realtimeIntent=new Intent(ReservationActivity.this, RealTimeDogWalkerListAcitvity.class);
                realtimeIntent.putExtra("registerVO", registerVO);
                ReservationActivity.this.startActivity(realtimeIntent);
            }
        });

        TextView RealTimeButton = findViewById(R.id.RealTimeButton);
        RealTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent realtimeIntent=new Intent(ReservationActivity.this, RealTimeDogWalkerListAcitvity.class);
                realtimeIntent.putExtra("registerVO", registerVO);
                ReservationActivity.this.startActivity(realtimeIntent);
            }
        });

        ImageView ReserveImage = findViewById(R.id.ReserveImage);
        ReserveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reserveIntent=new Intent(ReservationActivity.this, SearchActivity.class);
                reserveIntent.putExtra("registerVO",registerVO);
                ReservationActivity.this.startActivity(reserveIntent);
            }
        });

        TextView ReserveButton = findViewById(R.id.ReserveButton);
        ReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reserveIntent=new Intent(ReservationActivity.this, SearchActivity.class);
                reserveIntent.putExtra("registerVO",registerVO);
                ReservationActivity.this.startActivity(reserveIntent);
            }
        });




    }
}
