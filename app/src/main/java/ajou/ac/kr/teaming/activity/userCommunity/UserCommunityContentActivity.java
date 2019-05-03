package ajou.ac.kr.teaming.activity.userCommunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;

/**
 * 사용자가 커뮤니티 게시글 리스트에서 특정 게시글을 선택하였을 때
 * 게시글에 대한 정보를 보여주는 Activity
 */
public class UserCommunityContentActivity extends Activity {

    TextView threadTitle;
    TextView userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_community_content);

        threadTitle=(TextView)findViewById(R.id.thread_title);
        userId=(TextView)findViewById(R.id.user_id);

        //userCommunityMainActivity로 부터 가져온 데이터
        Intent intent = getIntent();
        String title=intent.getStringExtra("threadTitle");
        String id=intent.getStringExtra("userId");
        Log.d("TEST", "onCreate: TITLE"+title);

        userId.setText(id);
        threadTitle.setText(title);

    }


    /**
     * 닫기 버튼 클릭 시 액티비티(팝업) 닫기
     */
    public void onclickCloseActivity(View v){

        Intent intent = new Intent();

        finish();
    }

    /**
     *안드로이드 백버튼 막기
     */
    @Override
    public void onBackPressed() {
        return;
    }
}
