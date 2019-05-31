package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class MyPet2Activity extends AppCompatActivity {

    Button MyPetRegisterButton;
    RegisterVO registerVO;
    MyPetVO myPetVO;
    private MypetThreadService mypetThreadService = ServiceBuilder.create(MypetThreadService.class);
    private RecyclerView mypetView;
    private MypetThreadAdapter mypetThreadAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pet2);

        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("RegisterVO");

        MyPetRegisterButton = (Button) findViewById(R.id.MyPetRegisterButton);
        mypetView = findViewById(R.id.MyPet_list);
        mypetView.setLayoutManager(new LinearLayoutManager(this));



        mypetThreadAdapter = new MypetThreadAdapter(this::showThreadContentEvent);
        mypetView.setAdapter(mypetThreadAdapter);
        setmypetthreadList();






        MyPetRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent mypetregisterIntent = new Intent(MyPet2Activity.this, PetRegisterActivity.class);
                mypetregisterIntent.putExtra("RegisterVO", registerVO);
                MyPet2Activity.this.startActivity(mypetregisterIntent);

            }
        });


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
                Log.d("Test",registerVO.getUserID());
                Log.d("Test",registerVO.getUserPassword());
            }

        });


    }

    public void showThreadContentEvent(View view, MyPetVO myPetVO){

        Intent intent = new Intent(MyPet2Activity.this, MypetShowActivity.class);
        intent.putExtra("RegisterVO",registerVO);
        startActivity(intent);

    }
}
