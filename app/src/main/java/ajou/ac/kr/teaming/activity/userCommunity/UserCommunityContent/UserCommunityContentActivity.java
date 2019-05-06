package ajou.ac.kr.teaming.activity.userCommunity.UserCommunityContent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.userCommunity.UserCommunityContentCommentService;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 사용자가 커뮤니티 게시글 리스트에서 특정 게시글을 선택하였을 때
 * 게시글에 대한 정보를 보여주는 Activity
 */
public class UserCommunityContentActivity extends Activity {

    private UserCommunityContentCommentService userCommunityContentCommentService =
            ServiceBuilder.create(UserCommunityContentCommentService.class);
    private UserCommunityThreadVO userCommunityThreadVO;
    private TextView threadTitle;
    private TextView userId;
    private TextView threadContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_community_content);

        threadTitle=(TextView)findViewById(R.id.thread_title);
        userId=(TextView)findViewById(R.id.user_id);
        threadContent=(TextView)findViewById(R.id.thread_content);

        //userCommunityMainActivity로 부터 가져온 객체 setting
        Intent intent = getIntent();
        userCommunityThreadVO=(UserCommunityThreadVO) intent.getSerializableExtra("userCommunityThreadVO");
        userId.setText(userCommunityThreadVO.getUserId());
        threadTitle.setText(userCommunityThreadVO.getThreadTitle());
        threadContent.setText(userCommunityThreadVO.getContent());
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

    /**
     * 게시글 댓글 남기는 이벤트 handle
     * @param view
     */
    public void onClickSetCommentButton(View view) {
        String comment;
        comment=((EditText)findViewById(R.id.comment)).getText().toString();

        HashMap<String, Object> inputThread=new HashMap<>();

        inputThread.put("commentId","testcommentid");
        inputThread.put("commentContent",((EditText)findViewById(R.id.comment)).getText().toString());
        inputThread.put("commentDate",System.currentTimeMillis());
        inputThread.put("threadId",userCommunityThreadVO.getThreadId());
        inputThread.put("userId","testuserid");


        Call<UserCommunityContentCommentVO> request = userCommunityContentCommentService.postComment(inputThread);
        request.enqueue(new Callback<UserCommunityContentCommentVO>() {
            @Override
            public void onResponse(Call<UserCommunityContentCommentVO> call, Response<UserCommunityContentCommentVO> response) {
                // 성공시
                if(response.isSuccessful()){
                    UserCommunityContentCommentVO userCommunityContentCommentVO = response.body();
                    //테스트 확인 log값
                    if(userCommunityContentCommentVO!=null){
                        Log.d("TEST", userCommunityContentCommentVO.getUserId());
                        Log.d("TEST", userCommunityContentCommentVO.getCommentDate());
                        Log.d("TEST", userCommunityContentCommentVO.getCommentContent());
                    }
                }
                Log.d("TEST", "onResponse:END ");
            }
            @Override
            public void onFailure(Call<UserCommunityContentCommentVO> call, Throwable t) {
                //실패시
                Log.d("TEST", "통신 실패");
            }
        });

    }
}
