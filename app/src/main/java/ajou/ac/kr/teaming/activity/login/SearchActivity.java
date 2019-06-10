package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.DogwalkerThreadService;
import ajou.ac.kr.teaming.service.login.MypetThreadService;
import ajou.ac.kr.teaming.service.login.SearchService;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    RegisterVO registerVO;


    private DogwalkerThreadService dogwalkerThreadService = ServiceBuilder.create(DogwalkerThreadService.class);
    private RecyclerView Dogwalker_list;
    private DogwalkerThreadAdater dogwalkerThreadAdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Intent intent =getIntent();

        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");



        SearchButton = (Button) findViewById(R.id.SearchButton);
        Dogwalker_list= findViewById(R.id.Dogwalker_list);
        Dogwalker_list.setLayoutManager(new LinearLayoutManager(this));



        dogwalkerThreadAdater = new DogwalkerThreadAdater(this::showThreadContentEvent);
        Dogwalker_list.setAdapter(dogwalkerThreadAdater);



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

                Log.d("Test",year);
                Log.d("Test",month);
                Log.d("Test",day);

                Call<List<DogwalkerVO>> request= dogwalkerThreadService.dogwalkerThread(year,month,day);
                request.enqueue(new Callback<List<DogwalkerVO>>() {
                    @Override
                    public void onResponse(Call<List<DogwalkerVO>> call, Response<List<DogwalkerVO>> response) {
                        Log.d("Test","테스트");

                        List<DogwalkerVO> dogwalkerVOS=new ArrayList<>();
                        ArrayList<DogwalkerVO> dogwalkerlist=new ArrayList<>();
                        dogwalkerVOS=response.body();



                        if (dogwalkerVOS !=null){
                            for (DogwalkerVO dogwalkerVO:dogwalkerVOS){
                                Log.d("Test",dogwalkerVO.getUserInfo());
                                dogwalkerlist.add(dogwalkerVO);


                            }
                            dogwalkerThreadAdater.addThread(dogwalkerlist);


                        }
                    }


                    @Override
                    public void onFailure(Call<List<DogwalkerVO>> call, Throwable t) {

                        Log.d("Test",t.getMessage());

                    }
                });




            }
        });


    }

    private void showThreadContentEvent(View view, DogwalkerVO dogwalkerVO) {



        Intent vintent = new Intent(SearchActivity.this, DoigwalkerSerchResultViewActivity.class);
        vintent.putExtra("UserName",dogwalkerVO.getUserID());
        vintent.putExtra("UserBigcity",dogwalkerVO.getUserBigcity());
        vintent.putExtra("UserSmallcity",dogwalkerVO.getUserInfo());
        vintent.putExtra("UserverySmallcity",dogwalkerVO.getUserverySmallcity());
        vintent.putExtra("UserInfo",dogwalkerVO.getUserInfo());
        vintent.putExtra("UserTime",dogwalkerVO.getUserTime());
        vintent.putExtra("registerVO",registerVO);
        startActivity(vintent);

    }



    }
