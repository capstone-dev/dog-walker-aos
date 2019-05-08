package ajou.ac.kr.teaming.activity.login;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;

        import ajou.ac.kr.teaming.R;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        spinner=(Spinner) findViewById(R.id.liveSpinner);
        adapter=ArrayAdapter.createFromResource(this,R.array.city,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
