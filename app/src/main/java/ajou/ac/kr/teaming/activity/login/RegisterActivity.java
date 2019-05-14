package ajou.ac.kr.teaming.activity.login;

        import android.content.Intent;
        import android.support.constraint.Group;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
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

        import static android.os.Build.ID;
        import static retrofit2.converter.gson.GsonConverterFactory.*;

public class RegisterActivity extends AppCompatActivity {


    private RegisterService RegisterService = ServiceBuilder.create(RegisterService.class);

    private ArrayAdapter adapter;
    private Spinner spinner;
    private String UserID;
    private String UserGender;
    private String UserPassword;
    private String UserName;
    private String UserEmail;
    private String UserPhoneNumber;
    private String Userbigcity;
    private AlertDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spinner = (Spinner) findViewById(R.id.UserGender);
        adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner = (Spinner) findViewById(R.id.bigcitySpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText idText=(EditText)findViewById(R.id.idText);
        final EditText passwordText=(EditText)findViewById(R.id.passwordText);
        final EditText nameText=(EditText)findViewById(R.id.nameText);
        final EditText emailText=(EditText)findViewById(R.id.emailText);
        final EditText numberText=(EditText)findViewById(R.id.numberText);

        final Button registerButton= (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String UserID=idText.getText().toString();
                if(UserID.equals(""))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                    dialog=builder.setMessage("아이디는 빈칸일 수 없습니다.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }



            }
        });




    }


    public void registerButton(View view) {

        HashMap<String, Object> inputregister = new HashMap<>();

        inputregister.put("UserID", ((EditText) findViewById(R.id.idText)).getText().toString());
        inputregister.put("UserPassword", ((EditText) findViewById(R.id.passwordText)).getText().toString());
        inputregister.put("UserName", ((EditText) findViewById(R.id.nameText)).getText().toString());
        inputregister.put("UserEmail", ((EditText) findViewById(R.id.emailText)).getText().toString());
        inputregister.put("UserGender", ((Spinner) findViewById(R.id.UserGender)).getSelectedItem().toString());
        inputregister.put("UserBigcity", ((Spinner) findViewById(R.id.bigcitySpinner)).getSelectedItem().toString());
        inputregister.put("UserPhoneNumber", ((EditText) findViewById(R.id.numberText)).getText().toString());


        Call<RegisterVO> request = RegisterService.postsignUp(inputregister);

        request.enqueue(new Callback<RegisterVO>() {
            @Override
            public void onResponse(Call<RegisterVO> call, Response<RegisterVO> response) {
                // 성공시
                if (response.isSuccessful()) {
                    RegisterVO RegisterVOs = response.body();
                    //테스트 확인 log값
                    if (RegisterVOs != null) {
                        Log.d("TEST", RegisterVOs.getUserID());
                        Log.d("TEST", RegisterVOs.getUserPassword());
                        Log.d("TEST", RegisterVOs.getUserName());
                        Log.d("TEST", RegisterVOs.getUserEmail());
                        Log.d("TEST", RegisterVOs.getUserPhoneNumber());
                        Log.d("TEST", RegisterVOs.getUserGender());
                        Log.d("TEST", RegisterVOs.getUserbigcity());

                    }
                }
                Log.d("TEST", "onResponse:END ");

                Intent intent = new Intent(RegisterActivity.this, LoginMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RegisterVO> call, Throwable t) {

                Log.d("TEST", "통신 실패");

            }


        });
    }

}