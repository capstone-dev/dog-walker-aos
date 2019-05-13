package ajou.ac.kr.teaming.activity.login;

        import android.content.Intent;
        import android.support.constraint.Group;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioGroup;
        import android.widget.Spinner;

        import java.util.HashMap;

        import ajou.ac.kr.teaming.R;
        import ajou.ac.kr.teaming.activity.MainActivity;
        import ajou.ac.kr.teaming.activity.userCommunity.UserCommunityMainActivity;
        import ajou.ac.kr.teaming.activity.userCommunity.UserCommunityThreadRegisterActivity;
        import ajou.ac.kr.teaming.service.common.ServiceBuilder;
        import ajou.ac.kr.teaming.service.login.RegisterService;
        import ajou.ac.kr.teaming.service.userCommunity.UserCommunityThreadRegisterService;
        import ajou.ac.kr.teaming.vo.RegisterVO;
        import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

        import static retrofit2.converter.gson.GsonConverterFactory.*;

public class RegisterActivity extends AppCompatActivity {
    private RegisterService RegisterService = ServiceBuilder.create(RegisterService.class);


    private EditText idText;
    private EditText passwardText;
    private  EditText nameText;
    private EditText emailText;
    private  EditText numberText;
    private ArrayAdapter adapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText idText=(EditText) findViewById(R.id.idText);
        EditText passwordText=(EditText) findViewById(R.id.passwordText);
        EditText nameText=(EditText) findViewById(R.id.nameText);
        EditText emailText=(EditText) findViewById(R.id.emailText);
        EditText numberText=(EditText) findViewById(R.id.numberText);
        spinner=(Spinner) findViewById(R.id.bigcitySpinner);
        adapter=ArrayAdapter.createFromResource(this,R.array.city,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    public void onClickregisterButton(View view) {

        HashMap<String, Object> inputregister=new HashMap<>();

        inputregister.put("userId",(EditText)findViewById(R.id.idText));
        inputregister.put("userPwd",((EditText)findViewById(R.id.passwordText)));
        inputregister.put("username",((EditText)findViewById(R.id.nameText)));
        inputregister.put("useremail",((EditText)findViewById(R.id.emailText)));
        inputregister.put("userGender", ((RadioGroup) findViewById(R.id.genderGroup)));
        inputregister.put("bigcity",((Spinner)findViewById(R.id.bigcitySpinner)));

        Call<RegisterVO> request = RegisterService.postsignUp(inputregister);
        request.enqueue(new Callback<RegisterVO>() {
            @Override
            public void onResponse(Call<RegisterVO> call, Response<RegisterVO> response) {
                // 성공시
                if(response.isSuccessful()){
                    RegisterVO RegisterVOs = response.body();
                    //테스트 확인 log값
                    if(RegisterVOs!=null){
                        Log.d("TEST", RegisterVOs.getUserId());
                        Log.d("TEST", RegisterVOs.getUseremail());
                        Log.d("TEST", RegisterVOs.getUserPwd());
                        Log.d("TEST", RegisterVOs.getUserGender());
                        Log.d("TEST", RegisterVOs.getUsername());
                        Log.d("TEST", RegisterVOs.getBigcity());
                    }
                }
                Log.d("TEST", "onResponse:END ");
            }

            @Override
            public void onFailure(Call<RegisterVO> call, Throwable t) {

                Log.d("TEST", "통신 실패");

            }


        });

        //현재 페이지에서 메인 usercommunitymain으로 새로고침 하면서 이동
        Intent intent = new Intent(RegisterActivity.this, LoginMainActivity.class);
        startActivity(intent);
    }

    }
