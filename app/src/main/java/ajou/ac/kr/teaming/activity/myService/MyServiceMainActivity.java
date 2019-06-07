package ajou.ac.kr.teaming.activity.myService;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.messageChatting.MessageChattingMainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.gps.GpsRealTimeDogwalkerService;
import ajou.ac.kr.teaming.service.myService.MyServiceService;
import ajou.ac.kr.teaming.service.servicePay.ServicePayService;
import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.ServiceVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyServiceMainActivity extends AppCompatActivity implements MyServiceAdapter.OnMessageItemClickListener, MyServiceAdapter.OnDeleteItemClickListener {

    private ServicePayService servicePayService = ServiceBuilder.create(ServicePayService.class);
    private MyServiceService myServiceService = ServiceBuilder.create(MyServiceService.class);
    private GpsRealTimeDogwalkerService gpsRealTimeDogwalkerService = ServiceBuilder.create(GpsRealTimeDogwalkerService.class);
    private RecyclerView myServiceView;
    private RecyclerView dogwalkerServiceView;
    private MyServiceAdapter myServiceAdapter;
    private MyServiceAdapter dogwalkerServiceAdapter;
    private RegisterVO registerVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_main);

        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");

        myServiceView = findViewById(R.id.my_service_list_view);
        dogwalkerServiceView = findViewById(R.id.dogwalker_my_service_list_view);
        myServiceView.setLayoutManager(new LinearLayoutManager(this));
        dogwalkerServiceView.setLayoutManager(new LinearLayoutManager(this));

        myServiceAdapter = new MyServiceAdapter(this::deleteMyServiceEvent, this::contactMessageEvent);
        dogwalkerServiceAdapter = new MyServiceAdapter(this::deleteMyServiceEvent, this::contactMessageEvent);
        myServiceView.setAdapter(myServiceAdapter);
        dogwalkerServiceView.setAdapter(dogwalkerServiceAdapter);
        setMyServiceList();
        setDogwalkerServiceList();
    }

    /**
     * <p> 서버로부터 도그워커 서비스 목록을 읽어들여 리스트에 저장 후 adapter에 적용 </p>
     */
    private void setDogwalkerServiceList() {
        dogwalkerServiceAdapter.updateService();
        Call<List<ServiceVO>> request = servicePayService.getDogwalkerService(registerVO.getUserID());
        request.enqueue(new Callback<List<ServiceVO>>() {
            @Override
            public void onResponse(Call<List<ServiceVO>> call, Response<List<ServiceVO>> response) {

                List<ServiceVO> serviceVOS = response.body();
                // 성공시
                ArrayList<ServiceVO> serviceVOArrayList = new ArrayList<>();
                if (serviceVOS != null) {
                    for (ServiceVO serviceVO : serviceVOS) {
                        //서버로 부터 읽어드린 게시글 리스트를 전부 adapter 에 저장
                        serviceVOArrayList.add(serviceVO);
                        Log.d("TEST", "나의 서비스 가져오기! " + serviceVO.getServiceLocation());
                    }
                    dogwalkerServiceAdapter.addService(serviceVOArrayList);
                }
                Log.d("TEST", "게시글 통신 성공");
            }

            @Override
            public void onFailure(Call<List<ServiceVO>> call, Throwable t) {
                //실패시
                Log.d("TEST", "게시글 통신 실패");
            }
        });
    }

    /**
     * <p> 서버로부터 사용자 서비스 목록을 읽어들여 리스트에 저장 후 adapter에 적용 </p>
     */
    private void setMyServiceList() {
        myServiceAdapter.updateService();
        Call<List<ServiceVO>> request = servicePayService.getUserService(registerVO.getUserID());
        request.enqueue(new Callback<List<ServiceVO>>() {
            @Override
            public void onResponse(Call<List<ServiceVO>> call, Response<List<ServiceVO>> response) {

                List<ServiceVO> serviceVOS = response.body();
                // 성공시
                ArrayList<ServiceVO> serviceVOArrayList = new ArrayList<>();
                if (serviceVOS != null) {
                    for (ServiceVO serviceVO : serviceVOS) {
                        //서버로 부터 읽어드린 게시글 리스트를 전부 adapter 에 저장
                        serviceVOArrayList.add(serviceVO);
                        Log.d("TEST", "나의 서비스 가져오기! " + serviceVO.getServiceLocation());
                    }
                    myServiceAdapter.addService(serviceVOArrayList);
                }
                Log.d("TEST", "게시글 통신 성공");
            }

            @Override
            public void onFailure(Call<List<ServiceVO>> call, Throwable t) {
                //실패시
                Log.d("TEST", "게시글 통신 실패");
            }
        });
    }

    /**
     * 나의 서비스에서 해당 서비스 삭제하려고 할때 일어나는 이벤트 handle
     *
     * @param view
     * @param serviceVO 해당 서비스
     */
    @Override
    public void deleteMyServiceEvent(View view, ServiceVO serviceVO) {
        Call<ResponseBody> request = myServiceService.deleteService(Integer.toString(serviceVO.getId()));
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 성공시
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    //테스트 확인 log값
                    if (body != null) {
                        Log.d("TEST", body.toString());
                    }
                }
                Log.d("TEST", "삭제 성공 ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //실패시
                Log.d("TEST", "삭제 실패" + t.toString());
            }
        });
        updateDogwalkerRealTime(serviceVO);
        //현재 페이지에서 메인 usercommunitymain으로 새로고침 하면서 이동
        setMyServiceList();
        setDogwalkerServiceList();
    }

    /**
     * 나의 서비스에서 해당 서비스사용자와 채팅하려고 할때 일어나는 이벤트 handle
     *
     * @param view
     * @param serviceVO 해당 서비스
     */
    @Override
    public void contactMessageEvent(View view, ServiceVO serviceVO) {
        Intent intent = new Intent(MyServiceMainActivity.this, MessageChattingMainActivity.class);
        intent.putExtra("RegisterVO", registerVO);
        intent.putExtra("ServiceVO", serviceVO);
        intent.putExtra("activityName", "나의서비스");

        startActivity(intent);
    }

    public void updateDogwalkerRealTime(ServiceVO serviceVO) {
        //실시간 도그워커 사용자가 신청중으로 다시 바꿈

        HashMap<String, Object> inputThread=new HashMap<>();

        inputThread.put("DogwalkerID",serviceVO.getUser_DogwalkerID());
        inputThread.put("selected","0");

        Call<DogwalkerListVO> request = gpsRealTimeDogwalkerService.deleteMyService(inputThread);
        request.enqueue(new Callback<DogwalkerListVO>() {
            @Override
            public void onResponse(Call<DogwalkerListVO> call, Response<DogwalkerListVO> response) {
                // 성공시
                if (response.isSuccessful()) {
                    DogwalkerListVO dogwalkerListVOs = response.body();
                    //테스트 확인 log값
                    if (dogwalkerListVOs != null) {
                        Log.d("TEST", dogwalkerListVOs.getDogwalkerID());
                    }
                }
                Log.d("TEST", "수정 성공 ");
            }

            @Override
            public void onFailure(Call<DogwalkerListVO> call, Throwable t) {
                //실패시
                Log.d("TEST", "수정 실패" + t.getMessage());
            }
        });
        setMyServiceList();
    }
}
