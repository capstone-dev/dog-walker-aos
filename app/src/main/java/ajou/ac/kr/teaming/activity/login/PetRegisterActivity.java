package ajou.ac.kr.teaming.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.login.LoginService;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class PetRegisterActivity extends AppCompatActivity {


    ImageView PetImage;
    TextView idText;
    EditText dognameText;
    EditText typeText;
    EditText dogageText;
    Button petRegisterButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_register);
    }
}
