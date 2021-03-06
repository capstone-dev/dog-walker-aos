package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.MypetThreadService;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPetActivity extends AppCompatActivity {


    private static final int RESULT_OK = 1;

    Button MyPetRegisterButton;
    RegisterVO registerVO;
    MyPetVO myPetVO;
    private MypetThreadService mypetThreadService = ServiceBuilder.create(MypetThreadService.class);
    private RecyclerView mypetView;
    private MypetThreadAdapter mypetThreadAdapter;
    private byte[] imageData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pet3);



        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");



        MyPetRegisterButton = (Button) findViewById(R.id.MyPetRegisterButton);
        mypetView = findViewById(R.id.MyPet_list);
        mypetView.setLayoutManager(new LinearLayoutManager(this));



        mypetThreadAdapter = new MypetThreadAdapter(this::showThreadContentEvent);
        mypetView.setAdapter(mypetThreadAdapter);
        setmypetthreadList();





        MyPetRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent mypetregisterIntent = new Intent(MyPetActivity.this, PetRegisterActivity.class);
                mypetregisterIntent.putExtra("registerVO", registerVO);
                MyPetActivity.this.startActivity(mypetregisterIntent);

            }
        });


    }


    public void showThreadContentEvent(View view, MyPetVO myPetVO){

        Intent vintent = new Intent(MyPetActivity.this, PetView.class);
        vintent.putExtra("DogName",myPetVO.getDog_name());
        vintent.putExtra("DogType",myPetVO.getDog_species());
        vintent.putExtra("DogAge",myPetVO.getDog_age());
        vintent.putExtra("registerVO",registerVO);
        startActivity(vintent);

    }

    public void setmypetthreadList() {


        Call<List<MyPetVO>> request = mypetThreadService.petThread(registerVO.getUserID(),registerVO.getUserPassword());



        request.enqueue(new Callback<List<MyPetVO>>() {
            @Override
            public void onResponse(Call<List<MyPetVO>> call, Response<List<MyPetVO>> response) {
                List<MyPetVO> mypetVOs = response.body();
                ArrayList<MyPetVO> MypetList = new ArrayList<>();
                if (mypetVOs != null) {
                    for (MyPetVO myPetVO : mypetVOs) {
                        MypetList.add(myPetVO);
                        Log.d("TEST", "onResponse: " + myPetVO.getDog_name());
                    }
                    mypetThreadAdapter.addThread(MypetList);




                }
            }

            @Override
            public void onFailure(Call<List<MyPetVO>> call, Throwable t) {
                Log.d("TEST", "통신 실패");


            }

        });


    }



    }
