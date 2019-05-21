package ajou.ac.kr.teaming.activity.messageChatting;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import ajou.ac.kr.teaming.R;

public class ServiceSubmitActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_service_submit);
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


    public void onClickSetCommentButton(View view) {
    }


    /**
     * 서비스 구매 결정시 이벤트 handle
     * @param view
     */
    public void onClickPaySetButton(View view) {
    }

}
