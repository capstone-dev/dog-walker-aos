
package ajou.ac.kr.teaming.activity.messageChatting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;

import ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging.FirebaseMessagingService;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;

public class MessageChattingMainActivity extends Activity {

    ListView messageListView;
    MessageAdapter messageAdapter;
    private RegisterVO registerVO;
    private UserCommunityThreadVO userCommunityThreadVO;
    private TextView userIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chatting_main);

        //해당 사용자 등록 정보
        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("RegisterVO");
        userCommunityThreadVO=(UserCommunityThreadVO)intent.getSerializableExtra("userCommunityThreadVO");
        Log.d("TEST", "CHATTING REGISTER VO onCreate: "+registerVO);

        //커뮤니티에서 메시지 연결시 해당 커뮤니티 게시글 사용자 ID 받아옴

        userIdTextView=(TextView)findViewById(R.id.user_id);

        userIdTextView.setText(userCommunityThreadVO.getChatroomUserName()+"님과의 chatting");

        // 커스텀 어댑터 생성
        messageAdapter = new MessageAdapter(this,0);
        messageListView = findViewById(R.id.listView1);
        messageListView.setAdapter(messageAdapter);

        //파이어 베이스 메시징 서비스 이벤트 처리
        FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
        firebaseMessagingService.initFirebaseDatabase(messageAdapter,registerVO.getUserName());

        /*
         * <p > 메시지 전송 표시</p >
         */
        findViewById(R.id.send_message).setOnClickListener(v -> {
                    EditText editText = (EditText) findViewById(R.id.message);
                    String inputValue = editText.getText().toString();
                    editText.setText("");
                    //메시지 추가
                    firebaseMessagingService.onClick(v,inputValue,registerVO.getUserName());
                }
        );



    }


    /**
     * 서비스 최종 에약 승인 클릭 이벤트 확인
     * @param view
     */
    public void onClickServiceSubmit(View view) {
        Intent intent = new Intent(MessageChattingMainActivity.this, ServiceSubmitActivity.class);
        startActivity(intent);
    }
}
