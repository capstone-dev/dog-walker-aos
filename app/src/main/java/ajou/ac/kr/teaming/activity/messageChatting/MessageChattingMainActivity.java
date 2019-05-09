/*
package ajou.ac.kr.teaming.activity.messageChatting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import ajou.ac.kr.teaming.R;

public class MessageChattingMainActivity extends AppCompatActivity {

    ListView m_ListView;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chatting_main);
    }


    // 커스텀 어댑터 생성
    m_Adapter = new CustomAdapter();

    // Xml에서 추가한 ListView 연결
    m_ListView = (ListView) findViewById(R.id.listView1);

    */

    // ListView에 어댑터 연결

/*
		m_ListView.setAdapter(m_Adapter);

		m_Adapter.add("이건 뭐지",1);
		m_Adapter.add("쿨쿨",1);
		m_Adapter.add("쿨쿨쿨쿨",0);
		m_Adapter.add("재미있게",1);
		m_Adapter.add("놀자라구나힐힐 감사합니다. 동해물과 백두산이 마르고 닳도록 놀자 놀자 우리 놀자",1);
		m_Adapter.add("재미있게",1);
		m_Adapter.add("재미있게",0);
		m_Adapter.add("2015/11/20",2);
		m_Adapter.add("재미있게",1);
		m_Adapter.add("재미있게",1);


    findViewById(R.id.button1).setOnClickListener(new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText editText = (EditText) findViewById(R.id.editText1) ;
            String inputValue = editText.getText().toString() ;
            editText.setText("");
            refresh(inputValue,0);
        }
    }
		);


    findViewById(R.id.button2).setOnClickListener(new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText editText = (EditText) findViewById(R.id.editText1) ;
            String inputValue = editText.getText().toString() ;
            editText.setText("");
            refresh(inputValue,1);
        }
    }
		);

}

    private void refresh (String inputValue, int _str) {
        m_Adapter.add(inputValue,_str) ;
        m_Adapter.notifyDataSetChanged();
    }
}
*/
