package ajou.ac.kr.teaming.activity.login;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.RadioGroup;
        import android.widget.Spinner;

        import ajou.ac.kr.teaming.R;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        EditText idText=(EditText) findViewById(R.id.idText);
        EditText passwordText=(EditText) findViewById(R.id.passwordText);
        EditText password2Text=(EditText) findViewById(R.id.password2Text);
        EditText emailText=(EditText) findViewById(R.id.emailText);
        EditText numberText=(EditText) findViewById(R.id.numberText);

        spinner=(Spinner) findViewById(R.id.bigcitySpinner);
        adapter=ArrayAdapter.createFromResource(this,R.array.city,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
