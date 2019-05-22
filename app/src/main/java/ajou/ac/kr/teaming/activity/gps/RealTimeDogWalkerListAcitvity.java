package ajou.ac.kr.teaming.activity.gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.gps.GpsRealTimeDogwalkerService;
import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.DogwalkerVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealTimeDogWalkerListAcitvity extends AppCompatActivity {

    private RecyclerView dogwalkerview;
    private RealTimeDogwalkerListAdapter realTimeDogwalkerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_dog_walker_acitvity);

        dogwalkerview=findViewById(R.id.realtime_dogwalker_list);
        dogwalkerview.setLayoutManager(new LinearLayoutManager(this));


        realTimeDogwalkerListAdapter = new RealTimeDogwalkerListAdapter(this::chooseDogWalkerEvent);
        dogwalkerview.setAdapter(realTimeDogwalkerListAdapter);
        setRealTimeDogwalkerList();
    }


    /**
     * <p> 서버로부터 커뮤니티 게시글 목록을 읽어들여 리스트에 저장 후 adapter에 적용 </p>
     */
    public void setRealTimeDogwalkerList(){
        GpsRealTimeDogwalkerService gpsRealTimeDogwalkerService = ServiceBuilder.create(GpsRealTimeDogwalkerService.class);
        Call<List<DogwalkerListVO>> request = gpsRealTimeDogwalkerService.getThread();
        request.enqueue(new Callback<List<DogwalkerListVO>>() {
            @Override
            public void onResponse(Call<List<DogwalkerListVO>> call, Response<List<DogwalkerListVO>> response) {
                List<DogwalkerListVO> dogwalkerListVos = response.body();
                // 성공시
                ArrayList<DogwalkerListVO> realTimeDogwalkerList = new ArrayList<>();
                if(dogwalkerListVos!=null){
                    for (DogwalkerListVO dogwalkerListVO:dogwalkerListVos){
                        //서버로 부터 읽어드린 게시글 리스트를 전부 adapter 에 저장
                        realTimeDogwalkerList.add(dogwalkerListVO);
                        Log.d("TEST", "onResponse: " + dogwalkerListVO.getDogwalkerID());
                        Log.d("TEST", "onResponse: " + dogwalkerListVO.getDogwalkerBigcity());
                        Log.d("TEST", "onResponse: "+ dogwalkerListVO.getDogwalkerSmallcity());
                    }
                    realTimeDogwalkerListAdapter.addThread(realTimeDogwalkerList);
                }
                Log.d("TEST", "onResponse:END ");
            }
            @Override
            public void onFailure(Call<List<DogwalkerListVO>> call, Throwable t) {
                //실패시
                Log.d("TEST", "통신 실패");
            }
        });
    }



    /**
     * 실시간 dogwalker리스트에서 선택하였을때 일어나는 이벤트 handle
     * @param view
     * @param dogwalkerListVo -> 선택한 도그원커
     */
    private void chooseDogWalkerEvent(View view, DogwalkerListVO dogwalkerListVo) {
    }


    /**
     * 도그워커가 실시간 서비스를 신청하는 이벤트 handle
     * @param view
     */
    public void onClickDogwalkerRegister(View view) {
    }
}
