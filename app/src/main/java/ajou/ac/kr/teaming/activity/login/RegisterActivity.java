package ajou.ac.kr.teaming.activity.login;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.google.firebase.iid.FirebaseInstanceId;

        import java.util.HashMap;

        import ajou.ac.kr.teaming.R;
        import ajou.ac.kr.teaming.service.common.ServiceBuilder;
        import ajou.ac.kr.teaming.service.login.RegisterService;
        import ajou.ac.kr.teaming.vo.RegisterVO;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    String token;
    EditText idText;
    EditText passwordText;
    EditText nameText;
    EditText emailText;
    EditText numberText;
    Spinner UserGender;
    Spinner UserBigcity;
    Button registerButton;
    ArrayAdapter<CharSequence> adapter3, adapter1;
    RegisterService registerService = ServiceBuilder.create(RegisterService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserGender = (Spinner) findViewById(R.id.UserGender);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        UserGender.setAdapter(adapter3);
        UserBigcity = (Spinner) findViewById(R.id.UserBigcity);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.bigcity, android.R.layout.simple_spinner_dropdown_item);
        UserBigcity.setAdapter(adapter1);


        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        nameText = (EditText) findViewById(R.id.nameText);
        emailText = (EditText) findViewById(R.id.emailText);
        numberText = (EditText) findViewById(R.id.numberText);
        UserGender = (Spinner) findViewById(R.id.UserGender);
        UserBigcity = (Spinner) findViewById(R.id.UserBigcity);
        registerButton = (Button) findViewById(R.id.registerButton);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            token = instanceIdResult.getToken();
            //토큰 받기
            Log.d("TEST", "onCreate: "+token);
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userid = idText.getText().toString();
                String userpassword = passwordText.getText().toString();
                String username = nameText.getText().toString();
                String useremail = emailText.getText().toString();
                String usernumber = numberText.getText().toString();
                String usergender = UserGender.getSelectedItem().toString();
                String userbigcity = UserBigcity.getSelectedItem().toString();

                //Validate form
                if (ValidateRegister(userid, userpassword, username, useremail, usernumber, usergender, userbigcity)) {

                    HashMap<String, Object> inputregister = new HashMap<>();
                    inputregister.put("UserID", ((EditText) findViewById(R.id.idText)).getText().toString());
                    inputregister.put("UserPassword", ((EditText) findViewById(R.id.passwordText)).getText().toString());
                    inputregister.put("UserName", ((EditText) findViewById(R.id.nameText)).getText().toString());
                    inputregister.put("UserEmail", ((EditText) findViewById(R.id.emailText)).getText().toString());
                    inputregister.put("UserGender", ((Spinner) findViewById(R.id.UserGender)).getSelectedItem().toString());
                    inputregister.put("UserBigcity", ((Spinner) findViewById(R.id.UserBigcity)).getSelectedItem().toString());
                    inputregister.put("UserPhoneNumber", ((EditText) findViewById(R.id.numberText)).getText().toString());
                    inputregister.put("token",token);


                    Call<RegisterVO> request = registerService.postSignUp(inputregister);
                    request.enqueue(new Callback<RegisterVO>() {
                        @Override
                        public void onResponse(Call<RegisterVO> call, Response<RegisterVO> response) {
                            // 성공시
                            if (response.isSuccessful()) {
                                RegisterVO RegisterVOs = response.body();
                            }
                            Log.d("TEST", "onResponse:END ");
                        }

                        @Override
                        public void onFailure(Call<RegisterVO> call, Throwable t) {

                            Log.d("TEST", "통신 실패");

                        }
                    });
                    Intent intent = new Intent(RegisterActivity.this, LoginMainActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    private boolean ValidateRegister(String userid, String userpassword, String
            username, String useremail, String usernumber, String usergender, String userbigcity) {
        if (userid == null || userid.trim().isEmpty()) {
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userpassword == null || userpassword.trim().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username == null || username.trim().isEmpty()) {
            Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (useremail == null || useremail.trim().isEmpty()) {
            Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (usernumber == null || usernumber.trim().isEmpty()) {
            Toast.makeText(this, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (usergender == null || usergender.trim().isEmpty()) {
            Toast.makeText(this, "성을 선택하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userbigcity == null || userbigcity.trim().isEmpty()) {
            Toast.makeText(this, "도시를 선택하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

}