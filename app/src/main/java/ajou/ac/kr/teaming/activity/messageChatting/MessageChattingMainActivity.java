
package ajou.ac.kr.teaming.activity.messageChatting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;

import ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging.FirebaseMessagingService;
import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;

public class MessageChattingMainActivity extends Activity {

    ListView messageListView;
    MessageAdapter messageAdapter;
    private RegisterVO registerVO;
    private DogwalkerListVO dogwalkerListVO;
    private UserCommunityContentCommentVO userCommunityContentCommentVO;
    private UserCommunityThreadVO userCommunityThreadVO;
    private TextView userIdTextView;
    private String activityName;
    private Button submitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chatting_main);

        //해당 사용자 등록 정보
        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");
        activityName = intent.getExtras().getString("activityName");

        submitService=findViewById(R.id.submit_service);

        if(activityName.equals("사용자커뮤니티")) {
            userCommunityContentCommentVO = (UserCommunityContentCommentVO) intent.getSerializableExtra("UserCommunityContentCommentVO");
            userCommunityThreadVO = (UserCommunityThreadVO) intent.getSerializableExtra("UserCommunityThreadVO");

            //커뮤니티에서 메시지 연결시 해당 커뮤니티 게시글 댓글 사용자 ID 받아옴
            userIdTextView = (TextView) findViewById(R.id.user_id);
            submitService.setVisibility(View.GONE);

            if (registerVO.getUserID().equals(userCommunityThreadVO.getUser_UserID())) {
                userIdTextView.setText(userCommunityContentCommentVO.getUser_UserID() + "님과의 채팅");
            } else {
                userIdTextView.setText(userCommunityThreadVO.getUser_UserID() + "님과의 채팅");
            }
        }
        else if(activityName.equals("실시간도그워커")){
            dogwalkerListVO=(DogwalkerListVO) intent.getSerializableExtra("DogwalkerListVO");
            userIdTextView = (TextView) findViewById(R.id.user_id);


            submitService.setVisibility(View.VISIBLE);
            if (registerVO.getUserID().equals(dogwalkerListVO.getDogwalkerID())) {
                userIdTextView.setText(dogwalkerListVO.getSelect() + "님과의 채팅");
            } else if (registerVO.getUserID().equals(dogwalkerListVO.getSelect())){
                userIdTextView.setText(dogwalkerListVO.getDogwalkerID() + "님과의 채팅");
            }
        }

        // 커스텀 어댑터 생성
        messageAdapter = new MessageAdapter(this, 0);
        messageListView = findViewById(R.id.listView1);
        messageListView.setAdapter(messageAdapter);

        //파이어 베이스 메시징 서비스 이벤트 처리
        if(activityName.equals("사용자커뮤니티")) {

            FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
            firebaseMessagingService.initFirebaseDatabase(messageAdapter,  registerVO.getUserID(), userCommunityContentCommentVO.getCommentId());

            /*
             * <p > 메시지 전송 표시</p >
             */
            findViewById(R.id.send_message).setOnClickListener(v -> {
                        EditText editText = (EditText) findViewById(R.id.message);
                        String inputValue = editText.getText().toString();
                        editText.setText("");
                        //메시지 추가

                        if (registerVO.getUserID().equals(userCommunityThreadVO.getUser_UserID())) {
                            firebaseMessagingService.onClick(v, inputValue, registerVO.getUserID(),
                                    userCommunityContentCommentVO.getUser_UserID(), userCommunityContentCommentVO.getCommentId());
                        } else {
                            firebaseMessagingService.onClick(v, inputValue, registerVO.getUserID(),
                                    userCommunityThreadVO.getUser_UserID(), userCommunityContentCommentVO.getCommentId());
                        }
                    }
            );
        }

        else if(activityName.equals("실시간도그워커")){
            FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
            firebaseMessagingService.initFirebaseDatabase(messageAdapter, registerVO.getUserID(), dogwalkerListVO.getDogwalkerID());

            /*
             * <p > 메시지 전송 표시</p >
             */
            findViewById(R.id.send_message).setOnClickListener(v -> {
                        EditText editText = (EditText) findViewById(R.id.message);
                        String inputValue = editText.getText().toString();
                        editText.setText("");
                        //메시지 추가

                        if (registerVO.getUserID().equals(dogwalkerListVO.getDogwalkerID())) {
                            firebaseMessagingService.onClick(v, inputValue, registerVO.getUserID(),
                                    dogwalkerListVO.getSelect(), dogwalkerListVO.getSelect());
                        } else {
                            firebaseMessagingService.onClick(v, inputValue, registerVO.getUserID(),
                                    dogwalkerListVO.getDogwalkerID(), dogwalkerListVO.getDogwalkerID());
                        }
                    }
            );
        }


    }


    /**
     * 서비스 최종 에약 승인 클릭 이벤트 확인
     *
     * @param view
     */
    public void onClickServiceSubmit(View view) {
        Intent intent = new Intent(MessageChattingMainActivity.this, ServiceSubmitActivity.class);
        intent.putExtra("RegisterVO",registerVO);
        if(activityName.equals("사용자커뮤니티")) {
            intent.putExtra("userCommunityThreadVO", userCommunityThreadVO);
            intent.putExtra("UserCommunityContentCommentVO", userCommunityContentCommentVO);
            intent.putExtra("activityName","사용자커뮤니티");
        }
        else if(activityName.equals("실시간도그워커")){
            intent.putExtra("DogwalkerListVO",dogwalkerListVO);
            intent.putExtra("activityName","실시간도그워커");
        }
        startActivity(intent);
    }

    /**
     * 뒤로가기 버튼
     * 현재 있던 메시지 전부 서버로 저장
     *
     * @param view
     */
    public void onClickBackButton(View view) {
        finish();
    }
}
