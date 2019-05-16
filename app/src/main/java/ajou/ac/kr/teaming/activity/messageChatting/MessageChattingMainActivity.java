
package ajou.ac.kr.teaming.activity.messageChatting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import ajou.ac.kr.teaming.R;

import ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging.FirebaseMessagingService;

public class MessageChattingMainActivity extends Activity {

    ListView messageListView;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chatting_main);

        // 커스텀 어댑터 생성
        messageAdapter = new MessageAdapter();

        // Xml에서 추가한 ListView 연결
        messageListView = (ListView) findViewById(R.id.listView1);


        // ListView에 어댑터 연결

        messageListView.setAdapter(messageAdapter);


    /* 메시지 adapter 테스트*/
        messageAdapter.add("이건 뭐지", 1);
        messageAdapter.add("쿨쿨", 1);
        messageAdapter.add("쿨쿨쿨쿨", 0);
        messageAdapter.add("재미있게", 1);
        messageAdapter.add("놀자라구나힐힐 감사합니다. 동해물과 백두산이 마르고 닳도록 놀자 놀자 우리 놀자", 1);
        messageAdapter.add("재미있게", 1);
        messageAdapter.add("재미있게", 0);
        messageAdapter.add("2015/11/20", 2);
        messageAdapter.add("재미있게", 1);
        messageAdapter.add("재미있게", 1);


        /*
         *
         * <p > 수신측 메시지 표시</p >
         */

        findViewById(R.id.button1).setOnClickListener(v -> {
                    EditText editText = (EditText) findViewById(R.id.editText1);
                    String inputValue = editText.getText().toString();
                    editText.setText("");
                    refresh(inputValue, 0);
                }
        );
        /*
         *
         * <p > 송신측 메시지 표시</p >
         */

        findViewById(R.id.button2).setOnClickListener(v -> {
                    EditText editText = (EditText) findViewById(R.id.editText1);
                    String inputValue = editText.getText().toString();
                    editText.setText("");
                    refresh(inputValue, 1);
                    FirebaseMessagingService firebaseMessagingService=new FirebaseMessagingService();
                    firebaseMessagingService.initFirebaseDatabase(messageAdapter);
                }
        );

    }

    private void refresh(String inputValue, int _str) {
        messageAdapter.add(inputValue, _str);
        messageAdapter.notifyDataSetChanged();
    }
}
