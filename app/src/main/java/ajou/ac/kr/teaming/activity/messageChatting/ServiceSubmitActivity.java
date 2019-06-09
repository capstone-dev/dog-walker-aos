package ajou.ac.kr.teaming.activity.messageChatting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.HashMap;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.activity.decorator.OneDayDecorator;
import ajou.ac.kr.teaming.activity.decorator.SaturdayDecorator;
import ajou.ac.kr.teaming.activity.decorator.SundayDecorator;
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

public class ServiceSubmitActivity extends Activity implements TimePicker.OnTimeChangedListener {

    private ServicePayService servicePayService = ServiceBuilder.create(ServicePayService.class);
    private RegisterVO registerVO;
    private DogwalkerListVO dogwalkerListVO;

    private String activityName;
    private MaterialCalendarView materialCalendarView;
    private TimePicker timePicker;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private EditText serviceCost;
    private TextView dogwalkerId;
    private TextView serviceLocation;
    private EditText serviceTime;
    private TextView totalCost;
    private String uploadCost;
    private String walkingDate;
    private int check=0;
    private int hourOfDay, minute,finishHour,finishMinute;

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
        materialCalendarView=findViewById(R.id.calendarView);
        timePicker=findViewById(R.id.timePicker);

       if(activityName.equals("실시간도그워커")){
            dogwalkerListVO=(DogwalkerListVO) intent.getSerializableExtra("DogwalkerListVO");

            dogwalkerId.setText(dogwalkerListVO.getDogwalkerID());
            serviceLocation.setText("현재 사용자 위치");
        } else if(activityName.equals("도그워커예약")){

        }

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2018,6,1))
                .setMaximumDate(CalendarDay.from(2022,12,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener(this);

        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            int year=date.getYear();
            int month=date.getMonth()+1;
            int day=date.getDay();
            walkingDate=year+"/"+month+"/"+day;
            timePicker.setVisibility(View.VISIBLE);
        });
    }

    //안드로이드 백버튼 막기
    @Override
    public void onBackPressed() {
        return;
    }

    /**
     * 서비스 구매 결정시 이벤트 handle
     * 현재 구매결정한 서비스를 서버로 post
     * @param view
     */
    public void onClickPaySetButton(View view) {

        String cost=(serviceCost).getText().toString();
        String time=(serviceTime).getText().toString();
        walkingDate=walkingDate+" "+hourOfDay+":"+minute+" ~ "+finishHour+":"+finishMinute;

        if(checkServiceRegisterForm(cost,time,walkingDate,check)) {
            HashMap<String, Object> inputService = new HashMap<>();
            //post할 serviceVO값 넣기
            inputService.put("price", uploadCost);
            inputService.put("walkingTime", walkingDate);

            if (activityName.equals("실시간도그워커")) {
                inputService.put("user_UserID", registerVO.getUserID());
                inputService.put("user_DogwalkerID", dogwalkerListVO.getDogwalkerID());
                inputService.put("serviceLocation", ((TextView) serviceLocation).getText().toString());
                inputService.put("peopleNumber", "1");
            } else if (activityName.equals("도그워커예약")) {

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
            intent.putExtra("registerVO", registerVO);
            startActivity(intent);
            Toast.makeText(this, "산책 서비스 신청이 완료되었습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 다시 메시지 화면으로 전환
     * @param view
     */
    public void onClickBackToButton(View view) {
        Intent intent = new Intent();
        finish();
    }

    /**
     * 총합계 조회하는 화면
     * @param view
     */
    public void onClickgetTotalButton(View view) {
        int cost = Integer.parseInt(String.valueOf(serviceCost.getText()));
        int time = Integer.parseInt(String.valueOf(serviceTime.getText()));
        finishHour=hourOfDay+time;
        finishMinute=minute;
        totalCost.setText("총 금액: " + String.valueOf(cost * time) + "원");
        uploadCost= ((TextView)totalCost).getText().toString();
        check=1;
    }

    //시간 조회
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay=hourOfDay;
        this.minute=minute;
    }

    /**
     * 등록 폼이 형식에 맞는지 검증하는 작업
     * 형식에 맞다면 1반환
     * 틀리다면 0 반환
     * @return
     */
    private Boolean checkServiceRegisterForm(String cost, String serviceHour, String walkingDate, int check){

        if(cost.length()==0){
            Toast.makeText(this, "시간별 금액을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Integer.parseInt(serviceHour)<1 ){
            Toast.makeText(this, "시간은 1시간 단위로 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(walkingDate.length()==0){
            Toast.makeText(this, "날짜와 시간을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(check==0){
            Toast.makeText(this, "조회 버튼을 누르세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
