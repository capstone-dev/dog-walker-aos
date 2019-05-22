package ajou.ac.kr.teaming.activity.messageChatting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.servicePay.ServicePayService;
import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.ServiceVO;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceSubmitActivity extends Activity {

    private ServicePayService servicePayService = ServiceBuilder.create(ServicePayService.class);

    private RegisterVO registerVO;
    private UserCommunityContentCommentVO userCommunityContentCommentVO;
    private UserCommunityThreadVO userCommunityThreadVO;
    private DogwalkerListVO dogwalkerListVO;
    private String activityName;

    private EditText serviceCost;
    private TextView dogwalkerId;
    private TextView serviceLocation;
    private EditText serviceTime;
    private EditText walkingTime;
    private TextView totalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_service_submit);

        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");
        activityName = intent.getExtras().getString("activityName");

        serviceCost = findViewById(R.id.service_cost);
        dogwalkerId = findViewById(R.id.dogwalker_id);
        serviceLocation = findViewById(R.id.service_location);
        serviceTime = findViewById(R.id.service_time);
        totalCost = findViewById(R.id.total_cost);
        walkingTime = findViewById(R.id.service_time_range);

        if(activityName.equals("사용자커뮤니티")) {
            userCommunityContentCommentVO = (UserCommunityContentCommentVO) intent.getSerializableExtra("UserCommunityContentCommentVO");
            userCommunityThreadVO = (UserCommunityThreadVO) intent.getSerializableExtra("userCommunityThreadVO");

            dogwalkerId.setText(userCommunityThreadVO.getUser_UserID());
            serviceLocation.setText(userCommunityThreadVO.getUserLocation());
        }

        else if(activityName.equals("실시간도그워커")){
            dogwalkerListVO=(DogwalkerListVO) intent.getSerializableExtra("DogwalkerListVO");
            dogwalkerId.setText(dogwalkerListVO.getDogwalkerID());
            serviceLocation.setText("현재 사용자 위치");
        }
    }


    /**
     * 안드로이드 백버튼 막기
     */
    @Override
    public void onBackPressed() {
        return;
    }

    /**
     * 서비스 구매 결정시 이벤트 handle
     * 현재 구매결정한 서비스를 서버로 post
     *
     * @param view
     */
    public void onClickPaySetButton(View view) {

        HashMap<String, Object> inputService = new HashMap<>();
        //post할 serviceVO값 넣기
        inputService.put("price", serviceCost);
        inputService.put("walkingTime", walkingTime);

        if(activityName.equals("사용자커뮤니티")) {
            inputService.put("user_UserID", userCommunityContentCommentVO.getUser_UserID());
            inputService.put("user_DogwalkerID", userCommunityThreadVO.getUser_UserID());
            inputService.put("serviceLocation", userCommunityThreadVO.getUserLocation());
/*
        userCommunityThreadVO.setThreadNumber(userCommunityThreadVO.getThreadNumber()-1);*/
            inputService.put("peopleNumber", userCommunityThreadVO.getThreadNumber());
        }

        else if(activityName.equals("실시간도그워커")){
            inputService.put("user_UserID",registerVO.getUserID());
            inputService.put("user_DogwalkerID",dogwalkerListVO.getDogwalkerID());
            inputService.put("serviceLocation",serviceLocation);

            inputService.put("peopleNumber","1");
        }
        Call<ServiceVO> request = servicePayService.postService(inputService);
        request.enqueue(new Callback<ServiceVO>() {
            @Override
            public void onResponse(Call<ServiceVO> call, Response<ServiceVO> response) {
                // 성공시
                if (response.isSuccessful()) {
                    ServiceVO serviceVO = response.body();
                    Log.d("TEST", "onResponseBODY: " + response.body());
                    //테스트 확인 log값
                    if (serviceVO != null) {
                        Log.d("TEST", serviceVO.getUser_UserID());
                        Log.d("TEST", serviceVO.getUser_DogwalkerID());
                        Log.d("TEST", String.valueOf(serviceVO.getPrice()));
                    }
                }
                Log.d("TEST", "시버스 입력 성공 ");
            }

            @Override
            public void onFailure(Call<ServiceVO> call, Throwable t) {
                //실패시
                Log.d("TEST", "서비스 입력 실패");
            }
        });
        Intent intent = new Intent(ServiceSubmitActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "산책 서비스 신청이 완료되었습니다!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 다시 메시지 화면으로 전환
     *
     * @param view
     */
    public void onClickBackToButton(View view) {
        Intent intent = new Intent();
        finish();
    }

    /**
     * 총합계 조회하는 화면
     *
     * @param view
     */
    public void onClickgetTotalButton(View view) {
        int cost = Integer.parseInt(String.valueOf(serviceCost.getText()));
        int time = Integer.parseInt(String.valueOf(serviceTime.getText()));


        totalCost.setText("총 금액: " + String.valueOf(cost * time) + "원");
    }
}
