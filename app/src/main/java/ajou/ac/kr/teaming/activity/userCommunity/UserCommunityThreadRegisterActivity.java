package ajou.ac.kr.teaming.activity.userCommunity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ajou.ac.kr.teaming.R;

public class UserCommunityThreadRegisterActivity extends AppCompatActivity {

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

    public void onClickSubmitButton(View view) {
    }
}
