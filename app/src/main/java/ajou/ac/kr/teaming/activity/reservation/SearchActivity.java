package ajou.ac.kr.teaming.activity.reservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.gps.GpsMainActivity;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button RealTimeButton = findViewById(R.id.SearchButton);
        RealTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchresultIntent=new Intent(SearchActivity.this, SearchResultActivity.class);
                SearchActivity.this.startActivity(searchresultIntent);
            }
        });


    }
}
