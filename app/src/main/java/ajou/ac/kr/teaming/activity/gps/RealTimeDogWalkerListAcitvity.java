package ajou.ac.kr.teaming.activity.gps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.messageChatting.MessageChattingMainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.gps.GpsRealTimeDogwalkerService;
import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealTimeDogWalkerListAcitvity extends AppCompatActivity {

    private GpsRealTimeDogwalkerService gpsRealTimeDogwalkerService = ServiceBuilder.create(GpsRealTimeDogwalkerService.class);
    private RegisterVO registerVO;
    private RecyclerView dogwalkerview;
    private RealTimeDogwalkerListAdapter realTimeDogwalkerListAdapter;
    private Switch registerSwitch;
    private String switchInitialValue = "turnoff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_dog_walker_acitvity);

        registerSwitch = findViewById(R.id.registerSwtich);
        dogwalkerview = findViewById(R.id.realtime_dogwalker_list);
        dogwalkerview.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");

        realTimeDogwalkerListAdapter = new RealTimeDogwalkerListAdapter(this::chooseDogWalkerEvent);
        dogwalkerview.setAdapter(realTimeDogwalkerListAdapter);
        setRealTimeDogwalkerList();


        registerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked == true && switchInitialValue.equals("turnoff")) {
                Log.d("TEST", "onCreate: hello");
                Toast.makeText(this, "실시간 신청 ON", Toast.LENGTH_SHORT).show();
                onClickDogwalkerRegister();
            } else if (isChecked == false && switchInitialValue.equals("turnon")) {
                Log.d("TEST", "onCreate: hello");
                Toast.makeText(this, "실시간 신청 OFF", Toast.LENGTH_SHORT).show();
                onClickDogwalkerUnRegister();
            }
        });
    }

    //<p> 서버로부터 실시간 도그워커 목록을 읽어들여 리스트에 저장 후 adapter에 적용 </p>
    public void setRealTimeDogwalkerList() {

        realTimeDogwalkerListAdapter.deleteList();
        realTimeDogwalkerListAdapter.setUser(registerVO.getUserID());
        Call<List<DogwalkerListVO>> request = gpsRealTimeDogwalkerService.getThread();
        request.enqueue(new Callback<List<DogwalkerListVO>>() {
            @Override
            public void onResponse(Call<List<DogwalkerListVO>> call, Response<List<DogwalkerListVO>> response) {
                List<DogwalkerListVO> dogwalkerListVos = response.body();
                // 성공시
                ArrayList<DogwalkerListVO> realTimeDogwalkerList = new ArrayList<>();
                if (dogwalkerListVos != null) {
                    for (DogwalkerListVO dogwalkerListVO : dogwalkerListVos) {
                        //서버로 부터 읽어드린 게시글 리스트를 전부 adapter 에 저장
                        if (dogwalkerListVO.getDogwalkerID().equals(registerVO.getUserID())) {
                            switchInitialValue = "turnon";
                            registerSwitch.setChecked(true);
                        }
                        realTimeDogwalkerList.add(dogwalkerListVO);
                        Log.d("TEST", "onResponse: " + dogwalkerListVO.getDogwalkerID());
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
     * @param dogwalkerListVO -> 선택한 도그원커
     */
    private void chooseDogWalkerEvent(View view, DogwalkerListVO dogwalkerListVO) {
        Log.d("TEST", "onClickMessageActivity: ");

        if (checkAvailableService(dogwalkerListVO)) {
            HashMap<String, Object> updateThread = new HashMap<>();

            updateThread.put("DogwalkerID", dogwalkerListVO.getDogwalkerID());
            updateThread.put("DogwalkerBigcity", dogwalkerListVO.getDogwalkerBigcity());
            updateThread.put("DogwalkerSmallcity", dogwalkerListVO.getDogwalkerSmallcity());
            updateThread.put("DogwalkerGender", dogwalkerListVO.getDogwalkerGender());
            updateThread.put("selected", registerVO.getUserID());

            Call<DogwalkerListVO> request = gpsRealTimeDogwalkerService.putSelected(updateThread);
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
            Intent intent = new Intent(RealTimeDogWalkerListAcitvity.this, MessageChattingMainActivity.class);
            intent.putExtra("RegisterVO", registerVO);
            intent.putExtra("DogwalkerListVO", dogwalkerListVO);
            intent.putExtra("activityName", "실시간도그워커");
            startActivity(intent);
        } else {
            Toast.makeText(this, "해당 서비스는 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 도그워커가 실시간 서비스를 신청하는 이벤트 handle
     */
    public void onClickDogwalkerRegister() {
        //만약 중복 있다면
        if (!checkRegisterForm()) {
            Toast.makeText(this, "이미 신청한 서비스가 있습니다", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> inputThread = new HashMap<>();

            inputThread.put("DogwalkerID", registerVO.getUserID());
            inputThread.put("DogwalkerBigcity", registerVO.getUserSmallcity());
            inputThread.put("DogwalkerSmallcity", registerVO.getUserverySmallcity());
            inputThread.put("DogwalkerGender", registerVO.getUserGender());
            inputThread.put("selected", "0");

            Call<DogwalkerListVO> request = gpsRealTimeDogwalkerService.postThread(inputThread);
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
                    Log.d("TEST", "등록 성공 ");
                }

                @Override
                public void onFailure(Call<DogwalkerListVO> call, Throwable t) {
                    //실패시
                    Log.d("TEST", "등록 실패" + t.toString());
                }
            });
            setRealTimeDogwalkerList();
        }
    }

    //실시간 신청 종료 이벤트
    public void onClickDogwalkerUnRegister() {

        Call<ResponseBody> request = gpsRealTimeDogwalkerService.deleteData(registerVO.getUserID());
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 성공시
                if (response.isSuccessful()) {
                    switchInitialValue = "turnoff";
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
        setRealTimeDogwalkerList();
    }

    /**
     * 실시간 도그워커 서비스이기 때문에 만일 도그워커가 등록했는데 다시 등록할 수는 없다.
     *
     * @return
     */
    private Boolean checkRegisterForm() {
        int checkValue = realTimeDogwalkerListAdapter.check(registerVO.getUserID());
        //중복있을때 false 값 반환
        if (checkValue == 1) {
            return false;
        }
        return true;
    }

    /**
     * 이미 누군가가 사용중인 서비스일 경우 사용할 수 없다.
     *
     * @return
     */
    private Boolean checkAvailableService(DogwalkerListVO dogwalkerListVO) {
        if (!dogwalkerListVO.getSelected().equals("0")) {
            return false;
        }
        return true;
    }
}