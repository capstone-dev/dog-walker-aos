package ajou.ac.kr.teaming.activity.gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.DogwalkerVo;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;

public class RealTimeDogWalkerAcitvity extends AppCompatActivity {

    private RecyclerView dogwalkerview;
    private RealTimeDogwalkerListAdapter realTimeDogwalkerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_dog_walker_acitvity);

        dogwalkerview=findViewById(R.id.realtime_dogwalker_list);
        dogwalkerview.setLayoutManager(new LinearLayoutManager(this));

        realTimeDogwalkerListAdapter=new RealTimeDogwalkerListAdapter(this::chooseDogWalkerEvent);
        dogwalkerview.setAdapter(realTimeDogwalkerListAdapter);
    }

    /**
     * 실시간 dogwalker리스트에서 선택하였을때 일어나는 이벤트 handle
     * @param view
     * @param dogwalkerVo -> 선택한 도그원커
     */
    private void chooseDogWalkerEvent(View view, DogwalkerVo dogwalkerVo) {
    }


    /**
     * 도그워커가 실시간 서비스를 신청하는 이벤트 handle
     * @param view
     */
    public void onClickDogwalkerRegister(View view) {
    }
}
