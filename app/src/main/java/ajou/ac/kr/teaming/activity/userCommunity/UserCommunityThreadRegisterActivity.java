package ajou.ac.kr.teaming.activity.userCommunity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.userCommunity.UserCommunityThreadRegisterService;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommunityThreadRegisterActivity extends AppCompatActivity {

    private UserCommunityThreadRegisterService userCommunityThreadRegister = ServiceBuilder.create(UserCommunityThreadRegisterService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_community_thread_register);
    }

    /**
     * 다시 사용자 커뮤니티로 되돌아가는 메소드
     * @param view
     */
    public void onClicBackButton(View view) {
        Intent intent = new Intent(UserCommunityThreadRegisterActivity.this, UserCommunityMainActivity.class);
        startActivity(intent);
    }

    /**
     * 사용자 커뮤니티에서 일정 form submit할 시 서버에 json 형식으로 보내어 저장
     * @param view
     */
    public void onClickSubmitButton(View view) {

        HashMap<String, Object> inputThread=new HashMap<>();

        inputThread.put("userId","testId");
        inputThread.put("threadTitle",((EditText)findViewById(R.id.thread_title_edit_text)).getText().toString());
        inputThread.put("userLocation",((EditText)findViewById(R.id.thread_location_edit_text)).getText().toString());
        inputThread.put("threadNumber",Integer.parseInt(((EditText)findViewById(R.id.thread_number_edit_text))
                .getText().toString()));
        inputThread.put("threadDate",((EditText)findViewById(R.id.thread_date_edit_text)).getText().toString());
        inputThread.put("threadContent",((EditText)findViewById(R.id.thread_content_edit_text)).getText().toString());

        Call<UserCommunityThreadVO> request = userCommunityThreadRegister.postThread(inputThread);
        request.enqueue(new Callback<UserCommunityThreadVO>() {
            @Override
            public void onResponse(Call<UserCommunityThreadVO> call, Response<UserCommunityThreadVO> response) {
                // 성공시
                if(response.isSuccessful()){
                    UserCommunityThreadVO userCommunityThreadVOs = response.body();
                    //테스트 확인 log값
                    if(userCommunityThreadVOs!=null){
                        Log.d("TEST", userCommunityThreadVOs.getUserId());
                        Log.d("TEST", userCommunityThreadVOs.getThreadDate());
                        Log.d("TEST", userCommunityThreadVOs.getContent());
                    }
                }
                Log.d("TEST", "onResponse:END ");
            }
            @Override
            public void onFailure(Call<UserCommunityThreadVO> call, Throwable t) {
                //실패시
                Log.d("TEST", "통신 실패");
            }
        });
    }
}
