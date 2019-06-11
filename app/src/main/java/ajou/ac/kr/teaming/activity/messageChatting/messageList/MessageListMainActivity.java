package ajou.ac.kr.teaming.activity.messageChatting.messageList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.messageChatting.MessageAdapter;
import ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging.FirebaseMessagingService;
import ajou.ac.kr.teaming.vo.MessageVO;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class MessageListMainActivity extends AppCompatActivity implements
        MessageListAdapter.OnMessageClickEventListener, MessageListAdapter.OnDeleteMessageClickListener {

    private RecyclerView messageListView;
    private MessageListAdapter messageListAdapter;
    private RegisterVO registerVO;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list_main);

        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");

        messageListView = findViewById(R.id.message_list);
        messageListView.setLayoutManager(new LinearLayoutManager(this));

        messageListAdapter = new MessageListAdapter(this::clickMessageEvent,this::deleteMessageEvent);
        messageListView.setAdapter(messageListAdapter);
        setUserMessageList();
    }

    public void setUserMessageList() {
        FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
        firebaseMessagingService.initFirebaseDatabase(messageAdapter, registerVO.getUserID(),"1",registerVO.getUserID(),messageListAdapter);


    }

    public void onclickCloseActivity(View view) {
        finish();
    }

    /**
     *안드로이드 백버튼 막기
     */
    @Override
    public void onBackPressed() {
        return;
    }

    //메시지 클릭시 이벤트 발생
    @Override
    public void clickMessageEvent(View view, MessageVO messageVO) {



        Intent intent = new Intent(MessageListMainActivity.this, MessagePopUpActivity.class);
        intent.putExtra("MessageVO", messageVO);
        intent.putExtra("RegisterVO",registerVO);
        startActivity(intent);
    }

    //메시지 삭제 이벤트 발생
    @Override
    public void deleteMessageEvent(View view, MessageVO messageVO) {

    }
}
