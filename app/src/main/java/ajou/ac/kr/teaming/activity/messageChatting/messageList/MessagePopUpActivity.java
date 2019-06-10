package ajou.ac.kr.teaming.activity.messageChatting.messageList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.MessageVO;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class MessagePopUpActivity extends Activity {

    private RegisterVO registerVO;
    private MessageVO messagevo;

    private TextView messageId;
    private TextView messageContent;
    private TextView messageDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message_pop_up);

        Intent intent = getIntent();
        messagevo=(MessageVO) intent.getSerializableExtra("MessageVO");
        registerVO=(RegisterVO) intent.getSerializableExtra("RegisterVO");

        messageId=findViewById(R.id.message_user_id);
        messageContent=findViewById(R.id.message_content);
        messageDate=findViewById(R.id.message_date);

        Log.d("TEST", "onCreate:POPUP "+messagevo.getUser_message());

        messageId.setText("보낸 아이디: "+messagevo.getUser_name());
        messageContent.setText(Html.fromHtml("<p>내용</p>")+messagevo.getUser_message());
        messageDate.setText("보낸 날짜: " +messagevo.getUser_message_date());
    }

    /**
     *안드로이드 백버튼 막기
     */
    @Override
    public void onBackPressed() {
        return;
    }

    public void onClickCloseActiivity(View view) {
        finish();
    }
}
