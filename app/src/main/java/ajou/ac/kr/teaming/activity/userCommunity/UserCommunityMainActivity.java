package ajou.ac.kr.teaming.activity.userCommunity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ajou.ac.kr.teaming.R;

/**
 * 사용자 커뮤니티 intent activity
 */
public class UserCommunityMainActivity extends AppCompatActivity {
    
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
    
    }

    /**
     * 커뮤니티 게시글 목록 show
     */
    public void setUserthreadList(){

    }


    /**
     * 특정 커뮤니티 게시글을 클릭하게 되면
     * 해당 게시글에 내한 내용을 확인할 수 있게 되는 eventhandler
     * @param view
     */
    public void showThreadContentEvent(View view, String thread_title){

    }


}
