package ajou.ac.kr.teaming.activity.login;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.login.RegisterService;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;

import static retrofit2.converter.gson.GsonConverterFactory.create;

public class MyActivity extends AppCompatActivity {



    private TextView idText;
    private EditText passwordText;
    private RegisterVO RegisterVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        TextView idText=(TextView) findViewById(R.id.idText);
        EditText passwordText=(EditText) findViewById(R.id.passwordText);

        HashMap<String, Object> MypageThread=new HashMap<>();
        MypageThread.put("userId",((TextView)findViewById(R.id.idText)));;



    }




}
