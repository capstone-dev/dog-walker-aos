package ajou.ac.kr.teaming.activity.gps;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ListView;

import ajou.ac.kr.teaming.R;

public class RealTimeDogwalkerListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_dogwalker_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ListView dogWalkerListView ;
        RealTimeDogwalkerListAdapter adapter;
        // Adapter 생성
        adapter = new RealTimeDogwalkerListAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        dogWalkerListView = (ListView) findViewById(R.id.listRealtimeDogwalkerList);
        dogWalkerListView.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_account_box_black_36dp),
                "Box", "Account Box Black 36dp"); ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_account_circle_black_36dp),
                "Circle", "Account Circle Black 36dp"); ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_assignment_ind_black_36dp),
                "Ind", "Assignment Ind Black 36dp"); ;
    }

}
