package ajou.ac.kr.teaming.activity.userCommunity.UserCommunityContent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.messageChatting.MessageChattingMainActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.userCommunity.UserCommunityContentCommentService;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 사용자가 커뮤니티 게시글 리스트에서 특정 게시글을 선택하였을 때
 * 게시글에 대한 정보를 보여주는 Activity
 */
public class UserCommunityContentActivity extends Activity implements UserCommunityContentCommentAdapter.OnItemClickListener {

    private UserCommunityContentCommentService userCommunityContentCommentService =
            ServiceBuilder.create(UserCommunityContentCommentService.class);

    private RecyclerView userCommunityCommentView;
    private UserCommunityContentCommentAdapter userCommunityContentCommentAdapter;

    private UserCommunityThreadVO userCommunityThreadVO;
    private RegisterVO registerVO;
    private TextView threadTitle;
    private TextView userId;
    private TextView threadContent;
    private int type;

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
        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");

        userId.setText(userCommunityThreadVO.getUser_UserID());
        threadTitle.setText(userCommunityThreadVO.getThreadTitle());
        threadContent.setText(userCommunityThreadVO.getContent());

        //userCommunityContentCommentAdapter에서 가져온 댓글리스트 생성
        userCommunityCommentView=findViewById(R.id.comment_list);
        userCommunityCommentView.setLayoutManager(new LinearLayoutManager(this));

        userCommunityContentCommentAdapter=new UserCommunityContentCommentAdapter(this::matchMessageUserEvent);
        userCommunityCommentView.setAdapter(userCommunityContentCommentAdapter);
        setCommentList();
    }

    /**
     * <p> 서버로부터 커뮤니티 게시글 댓글 목록을 읽어들여 리스트에 저장 후 adapter에 적용 </p>
     */
    public void setCommentList(){
        //게시글의 속성 threadId에 맞는 댓글리스트 불러드림
        Call<List<UserCommunityContentCommentVO>> request = userCommunityContentCommentService.
                getComment(userCommunityThreadVO.getThreadId());

        request.enqueue(new Callback<List<UserCommunityContentCommentVO>>() {
            @Override
            public void onResponse(Call<List<UserCommunityContentCommentVO>> call, Response<List<UserCommunityContentCommentVO>> response) {

                List<UserCommunityContentCommentVO> userCommunityContentCommentVOS = response.body();
                // 성공시
                ArrayList<UserCommunityContentCommentVO> userCommunityCommentVOArrayList=new ArrayList<>();
                if(userCommunityContentCommentVOS!=null){
                    for (UserCommunityContentCommentVO userCommunityContentCommentVO:userCommunityContentCommentVOS){
                        //서버로 부터 읽어드린 댓글 리스트를 전부 adapter 에 저장
                        userCommunityCommentVOArrayList.add(userCommunityContentCommentVO);
                    }
                    userCommunityContentCommentAdapter.addCommentList(userCommunityCommentVOArrayList,registerVO,userCommunityThreadVO);
                }
                Log.d("TEST", "댓글 리스트 post 성공 ");
            }
            @Override
            public void onFailure(Call<List<UserCommunityContentCommentVO>> call, Throwable t) {
                //실패시
                Log.d("TEST", "댓글 불러오기 실패");
            }
        });
    }

    /**
     * 닫기 버튼 클릭 시 액티비티(팝업) 닫기
     */
    public void onclickCloseActivity(View v){
        //기존 INTENT 제작
        Intent intent=new Intent();
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

        HashMap<String, Object> inputThread=new HashMap<>();

        inputThread.put("commentContent",((EditText)findViewById(R.id.comment)).getText().toString());
        inputThread.put("threadId",userCommunityThreadVO.getThreadId());
        inputThread.put("user_UserID",registerVO.getUserID());

        Call<UserCommunityContentCommentVO> request = userCommunityContentCommentService.postComment(inputThread);
        request.enqueue(new Callback<UserCommunityContentCommentVO>() {
            @Override
            public void onResponse(Call<UserCommunityContentCommentVO> call, Response<UserCommunityContentCommentVO> response) {
                // 성공시
                if(response.isSuccessful()){
                    UserCommunityContentCommentVO userCommunityContentCommentVO = response.body();
                    //테스트 확인 log값
                    if(userCommunityContentCommentVO!=null){
                        Log.d("TEST", userCommunityContentCommentVO.getUser_UserID());
                    }
                }
                Log.d("TEST", "댓글 남기기 성공");
            }
            @Override
            public void onFailure(Call<UserCommunityContentCommentVO> call, Throwable t) {
                //실패시
                Log.d("TEST", "댓글 남기기 실패");
            }
        });
        setCommentList();
    }

    /**
     * 연결 버튼을 누르게 되면 해당 사용자와 message 할 수 있게됨
     * @param view
     * @param userCommunityContentCommentVO 해당 commentVO
     */
    @Override
    public void matchMessageUserEvent(View view, UserCommunityContentCommentVO userCommunityContentCommentVO) {
        //메시지 연결
        Intent intent = new Intent(UserCommunityContentActivity.this, MessageChattingMainActivity.class);
        intent.putExtra("RegisterVO",registerVO);
        intent.putExtra("activityName","사용자커뮤니티");
        intent.putExtra("UserCommunityContentCommentVO", userCommunityContentCommentVO);
        intent.putExtra("UserCommunityThreadVO",this.userCommunityThreadVO);
        startActivity(intent);
    }
}
