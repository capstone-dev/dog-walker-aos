package ajou.ac.kr.teaming.activity.myService;

import android.content.Intent;
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
import ajou.ac.kr.teaming.service.servicePay.ServicePayService;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.ServiceVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyServiceMainActivity extends AppCompatActivity implements MyServiceAdapter.OnMessageItemClickListener, MyServiceAdapter.OnDeleteItemClickListener {

    private ServicePayService servicePayService = ServiceBuilder.create(ServicePayService.class);
    private RecyclerView myServiceView;
    private MyServiceAdapter myServiceAdapter;
    private RegisterVO registerVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_main);

        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");

        myServiceView = findViewById(R.id.my_service_list_view);
        myServiceView.setLayoutManager(new LinearLayoutManager(this));

        myServiceAdapter = new MyServiceAdapter(this::deleteMyServiceEvent, this::contactMessageEvent);
        myServiceView.setAdapter(myServiceAdapter);
        setMyServiceList();

    }

    /**
     * <p> 서버로부터 커뮤니티 게시글 목록을 읽어들여 리스트에 저장 후 adapter에 적용 </p>
     */
    private void setMyServiceList() {

        Call<List<ServiceVO>> request = servicePayService.getComment(registerVO.getUserID());
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

    @Override
    public void deleteMyServiceEvent(View view, ServiceVO serviceVO) {

    }

    @Override
    public void contactMessageEvent(View view, ServiceVO serviceVO) {

    }
}
