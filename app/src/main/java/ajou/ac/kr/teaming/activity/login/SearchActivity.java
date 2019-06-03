package ajou.ac.kr.teaming.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.SearchService;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import retrofit2.Call;

public class SearchActivity extends AppCompatActivity {

    Button SearchButton;
    EditText BigcityText;
    EditText SmallcityText;
    Spinner YearSpinner;
    Spinner MonthSpinner;
    Spinner daySpinner;
    Spinner TimeSpinner;
    ArrayAdapter<CharSequence> adapter1,adapter2,adapter3,adapter4 ;
    SearchService searchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        searchService= ServiceBuilder.create(SearchService.class);

        SearchButton =(Button) findViewById (R.id.SearchButton);
        BigcityText = (EditText) findViewById (R.id.BigcityText);
        SmallcityText=(EditText) findViewById(R.id.SmallcityText);

        YearSpinner = (Spinner) findViewById(R.id.YearSpinner);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.Year, android.R.layout.simple_spinner_dropdown_item);
        YearSpinner.setAdapter(adapter1);


        MonthSpinner = (Spinner) findViewById(R.id.MonthSpinner);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.Month, android.R.layout.simple_spinner_dropdown_item);
        MonthSpinner.setAdapter(adapter2);

        daySpinner = (Spinner) findViewById(R.id.DaySpinner);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.Date, android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter3);

        TimeSpinner = (Spinner) findViewById(R.id.TimeSpinner);
        adapter4 = ArrayAdapter.createFromResource(this, R.array.Time, android.R.layout.simple_spinner_dropdown_item);
        TimeSpinner.setAdapter(adapter4);





        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String year = YearSpinner.getSelectedItem().toString();
                String month=MonthSpinner.getSelectedItem().toString();
                String day =daySpinner.getSelectedItem().toString();
                String userbigcity = BigcityText.getText().toString();
                String usersmallcity = SmallcityText.getText().toString();
                String usertime = TimeSpinner.getSelectedItem().toString();


                Call<DogwalkerVO> call =searchService.dogwalkersearch(year,month,day,userbigcity,usersmallcity,usertime);




            }
        });


    }
}
