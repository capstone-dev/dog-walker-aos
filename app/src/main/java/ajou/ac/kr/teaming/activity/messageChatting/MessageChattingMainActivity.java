
package ajou.ac.kr.teaming.activity.messageChatting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import ajou.ac.kr.teaming.R;

import ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging.FirebaseMessagingService;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class MessageChattingMainActivity extends Activity {

    ListView messageListView;
    MessageAdapter messageAdapter;
    private RegisterVO registerVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chatting_main);

        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("RegisterVO");
        // 커스텀 어댑터 생성
        messageAdapter = new MessageAdapter();

        // Xml에서 추가한 ListView 연결
        messageListView = (ListView) findViewById(R.id.listView1);
        messageListView.setAdapter(messageAdapter);


        //파이어 베이스 메시징 서비스 이벤트 처리
        FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
        firebaseMessagingService.initFirebaseDatabase(messageAdapter);

        /*
         *
         * <p > 메시지 전송 표시</p >
         */
        findViewById(R.id.send_message).setOnClickListener(v -> {
                    EditText editText = (EditText) findViewById(R.id.message);
                    String inputValue = editText.getText().toString();
                    editText.setText("");

                    //메시지 추가
                    firebaseMessagingService.onClick(v,inputValue);
                }
        );

    }

    private void refresh(String inputValue, int _str) {
        messageAdapter.add(inputValue, _str);
        messageAdapter.notifyDataSetChanged();
    }
}
