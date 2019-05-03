package ajou.ac.kr.teaming.activity.userCommunity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.userCommunity.UserCommunityService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 사용자 커뮤니티 intent activity
 */
public class UserCommunityMainActivity extends AppCompatActivity {

    private UserCommunityService userCommunityService = ServiceBuilder.create(UserCommunityService.class);
    private RecyclerView userthreadView;
    private UserCommunityThreadAdapter userCommunityThreadAdapter;
    private UserCommunityThreadAdapter.OnItemClickListener showThreadContentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_community_main);
        
        userCommunityThreadAdapter=new UserCommunityThreadAdapter(this.showThreadContentEvent);
        userthreadView=findViewById(R.id.user_community_thread_list);
        userthreadView.setAdapter(userCommunityThreadAdapter);

        setUserthreadList();
    }

    /**
     * 커뮤니티 게시글 목록 show
     */
    public void setUserthreadList(){
        Call<List<UserCommunityThreadVO>> request = userCommunityService.getThread("kim");
        request.enqueue(new Callback<List<UserCommunityThreadVO>>() {
            @Override
            public void onResponse(Call<List<UserCommunityThreadVO>> call, Response<List<UserCommunityThreadVO>> response) {

                List<UserCommunityThreadVO> userCommunityThreadVOs = response.body();

                // 성공시
                if(userCommunityThreadVOs!=null){
                    for (UserCommunityThreadVO userCommunityThreadVO:userCommunityThreadVOs){
                        Log.d("TEST", "onResponse: "+userCommunityThreadVO.getThreadTitle());
                    }
                }
                Log.d("TEST", "onResponse:END ");
            }

            @Override
            public void onFailure(Call<List<UserCommunityThreadVO>> call, Throwable t) {
                //실패시
                Log.d("TEST", "통신 실패");
            }
        });
    }


    /**
     * 특정 커뮤니티 게시글을 클릭하게 되면
     * 해당 게시글에 내한 내용을 확인할 수 있게 되는 eventhandler
     * @param view
     */
    public void showThreadContentEvent(View view, String thread_title){

    }


    public void onClickThreadRegister(View view) {
    }
}
