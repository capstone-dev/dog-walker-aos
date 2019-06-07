package ajou.ac.kr.teaming.activity.userCommunity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.HashMap;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.decorator.OneDayDecorator;
import ajou.ac.kr.teaming.activity.decorator.SaturdayDecorator;
import ajou.ac.kr.teaming.activity.decorator.SundayDecorator;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.userCommunity.UserCommunityThreadRegisterService;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommunityThreadRegisterActivity extends AppCompatActivity {

    private UserCommunityThreadRegisterService userCommunityThreadRegister = ServiceBuilder.create(UserCommunityThreadRegisterService.class);
    private RegisterVO registerVO;
    private UserCommunityThreadVO userCommunityThreadVO;
    private Button threadRegisterButton;
    private String work;
    private MaterialCalendarView materialCalendarView;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private String walkingDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_community_thread_register);

        Intent intent = getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");
        work= intent.getExtras().getString("work");

        threadRegisterButton=findViewById(R.id.thread_register_button);
        materialCalendarView=findViewById(R.id.calendarView);

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

        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            int year=date.getYear();
            int month=date.getMonth()+1;
            int day=date.getDay();
            walkingDate=year+"/"+month+"/"+day;
            Log.d("TEST", "날짜: "+walkingDate);
        });


        if(work.equals("수정")){
            threadRegisterButton.setText("수정");
            userCommunityThreadVO=(UserCommunityThreadVO) intent.getSerializableExtra("UserCommunityThreadVO");
            ((EditText)findViewById(R.id.thread_title_edit_text)).setText(userCommunityThreadVO.getThreadTitle());
            ((EditText)findViewById(R.id.thread_location_edit_text)).setText(userCommunityThreadVO.getUserLocation());
            ((EditText)findViewById(R.id.thread_number_edit_text)).setText(Integer.toString(userCommunityThreadVO.getThreadNumber()));
            ((EditText)findViewById(R.id.thread_content_edit_text)).setText(userCommunityThreadVO.getThreadContent());
        }
    }

    /**
     * 다시 사용자 커뮤니티로 되돌아가는 메소드
     * @param view
     */
    public void onClicBackButton(View view) {
        finish();
    }

    /**
     * 사용자 커뮤니티에서 일정 form submit할 시 서버에 json 형식으로 보내어 저장
     * @param view
     */
    public void onClickSubmitButton(View view) {

        HashMap<String, Object> inputThread=new HashMap<>();

        String threadTitle=((EditText)findViewById(R.id.thread_title_edit_text)).getText().toString();
        String userLocation=((EditText)findViewById(R.id.thread_location_edit_text)).getText().toString();
        int threadNumber=Integer.parseInt(((EditText)findViewById(R.id.thread_number_edit_text)).getText().toString());
        String threadContent=((EditText)findViewById(R.id.thread_content_edit_text)).getText().toString();

        //등록 폼 검증 후 모든 값이 검증이 된다면 게시글 post
        if(checkThreadRegisterForm(threadTitle,userLocation,threadNumber,threadContent)) {

            //inputThread에 post해죽기 위해 값을 넣음.
            inputThread.put("user_UserID", registerVO.getUserID());
            inputThread.put("threadTitle", threadTitle);
            inputThread.put("userLocation", userLocation);
            inputThread.put("threadNumber", threadNumber);
            inputThread.put("threadWalkDate",walkingDate);
            inputThread.put("threadContent", threadContent);
            inputThread.put("chatroomUserName", "테스트");

            Call<UserCommunityThreadVO> request;

            if(work.equals("수정")){
                inputThread.put("threadId",userCommunityThreadVO.getThreadId());
                request = userCommunityThreadRegister.updateThread(inputThread);
            }
            else {
                request = userCommunityThreadRegister.postThread(inputThread);
            }
            request.enqueue(new Callback<UserCommunityThreadVO>() {
                @Override
                public void onResponse(Call<UserCommunityThreadVO> call, Response<UserCommunityThreadVO> response) {
                    // 성공시
                    if (response.isSuccessful()) {
                        UserCommunityThreadVO userCommunityThreadVOs = response.body();
                        //테스트 확인 log값
                       if (userCommunityThreadVOs != null) {
                            Log.d("TEST", userCommunityThreadVOs.getUser_UserID());
                        }
                    }
                    Log.d("TEST", "등록 성공 ");
                }

                @Override
                public void onFailure(Call<UserCommunityThreadVO> call, Throwable t) {
                    //실패시
                    Log.d("TEST", "등록 실패");
                }
            });

            //현재 페이지에서 메인 usercommunitymain으로 새로고침 하면서 이동
            Intent intent = new Intent(UserCommunityThreadRegisterActivity.this, UserCommunityMainActivity.class);
            intent.putExtra("RegisterVO",registerVO);
            startActivity(intent);
        }
    }

    /**
     * 등록 폼이 형식에 맞는지 검증하는 작업
     * 형식에 맞다면 1반환
     * 틀리다면 0 반환
     * @return
     */
    private Boolean checkThreadRegisterForm(String threadTitle, String userLocation, int number, String threadContent){

        if(threadTitle.length()==0){
            Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(userLocation.length()==0){
            Toast.makeText(this, "산책 장소를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(number==0||number>5){
            Toast.makeText(this, "인원은 1~5 사이로 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(threadContent.length()==0){
            Toast.makeText(this, "게시글 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //해당 게시글 삭제 요청 이벤트 handle
    public void onClickDeleteButton(View view) {

        Call<ResponseBody> request = userCommunityThreadRegister.deleteThread(userCommunityThreadVO.getThreadId());
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
                Log.d("TEST", "삭제 실패"+t.toString());
            }
        });
        //현재 페이지에서 메인 usercommunitymain으로 새로고침 하면서 이동
        Intent intent = new Intent(UserCommunityThreadRegisterActivity.this, UserCommunityMainActivity.class);
        intent.putExtra("RegisterVO",registerVO);
        startActivity(intent);
    }
}
