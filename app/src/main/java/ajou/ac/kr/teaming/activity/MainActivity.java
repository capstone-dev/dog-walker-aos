package ajou.ac.kr.teaming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.gps.DogwalkerGpsActivity;
import ajou.ac.kr.teaming.activity.gps.GpsMainActivity;
import ajou.ac.kr.teaming.activity.login.DogwalkerRegister;
import ajou.ac.kr.teaming.activity.login.LoginMainActivity;
import ajou.ac.kr.teaming.activity.login.MyPageModifyActivity;
import ajou.ac.kr.teaming.activity.login.MyPetActivity;
import ajou.ac.kr.teaming.activity.messageChatting.MessageChattingMainActivity;
import ajou.ac.kr.teaming.activity.messageChatting.messageList.MessageListMainActivity;
import ajou.ac.kr.teaming.activity.myService.MyServiceAdapter;
import ajou.ac.kr.teaming.activity.myService.MyServiceMainActivity;
import ajou.ac.kr.teaming.activity.reservation.ReservationActivity;
import ajou.ac.kr.teaming.activity.userCommunity.UserCommunityMainActivity;
import ajou.ac.kr.teaming.service.gallery.GalleryService;
import ajou.ac.kr.teaming.service.gps.GpsRealTimeDogwalkerService;
import ajou.ac.kr.teaming.service.gps.GpsService;
import ajou.ac.kr.teaming.service.history.HistoryService;
import ajou.ac.kr.teaming.service.myService.MyServiceService;
import ajou.ac.kr.teaming.service.sample.SampleService;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.servicePay.ServicePayService;
import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.ServiceVO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener ,
        MyServiceAdapter.OnMessageItemClickListener, MyServiceAdapter.OnDeleteItemClickListener,MyServiceAdapter.OnMyServiceClickListener{

    private static final String TAG = "MainActivity";
    private SampleService sampleService = ServiceBuilder.create(SampleService.class);
    private GalleryService galleryService = ServiceBuilder.create(GalleryService.class);
    private HistoryService historyService = ServiceBuilder.create(HistoryService.class);
    private GpsService gpsService = ServiceBuilder.create(GpsService.class);
    private RegisterVO registerVO;
    private DogwalkerVO dogwalkerVO;
    private FloatingActionButton messageButton;


    private ServicePayService servicePayService = ServiceBuilder.create(ServicePayService.class);
    private MyServiceService myServiceService = ServiceBuilder.create(MyServiceService.class);
    private GpsRealTimeDogwalkerService gpsRealTimeDogwalkerService = ServiceBuilder.create(GpsRealTimeDogwalkerService.class);
    private RecyclerView myServiceView;
    private RecyclerView dogwalkerServiceView;
    private MyServiceAdapter myServiceAdapter;
    private MyServiceAdapter dogwalkerServiceAdapter;
    private TextView myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        /**
         * loging activity로 부터 main activity로 사용자 정보 받아옴
         */
        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("registerVO");

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // xml 파일에서 넣어놨던 header 선언
        View header = navigationView.getHeaderView(0);

        // header에 있는 리소스 가져오기
        TextView idview = (TextView) header.findViewById(R.id.idView);
        idview.setText(registerVO.getUserID());
        TextView emailview=(TextView)header.findViewById(R.id.emailView);
        emailview.setText(registerVO.getUserEmail());

        View textView=(View)findViewById(R.id.main_layout_1);
        messageButton=(FloatingActionButton)textView.findViewById(R.id.fab);
        messageButton.setOnClickListener(this);

        //contentview 연결
        View contentView=(View)textView.findViewById(R.id.connect_content_main);

        myServiceView = contentView.findViewById(R.id.my_service_list_view);
        dogwalkerServiceView = contentView.findViewById(R.id.dogwalker_my_service_list_view);
        myServiceView.setLayoutManager(new LinearLayoutManager(this));
        dogwalkerServiceView.setLayoutManager(new LinearLayoutManager(this));

        myService=contentView.findViewById(R.id.service_list_form);
        myService.setText(registerVO.getUserID()+"님의 서비스 리스트");

        myServiceAdapter = new MyServiceAdapter(this::deleteMyServiceEvent, this::contactMessageEvent,this::clickMyServiceEvent);
        dogwalkerServiceAdapter = new MyServiceAdapter(this::deleteMyServiceEvent, this::contactMessageEvent,this::clickMyServiceEvent);
        myServiceView.setAdapter(myServiceAdapter);
        dogwalkerServiceView.setAdapter(dogwalkerServiceAdapter);
        setMyServiceList();
        setDogwalkerServiceList();

        Log.d(TAG, "onCreate: smallcity"+registerVO.getUserSmallcity());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    /**
     *  사이드 바 메뉴
     *  Intent 기능 사용
     * */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_search) {
            //예약 페이지로 이동
            Intent fintent = new Intent(MainActivity.this, ReservationActivity.class);
            fintent.putExtra("RegisterVO", registerVO);
            startActivity(fintent);


        } else if (id == R.id.nav_my) {

            Intent my = new Intent(MainActivity.this, DogwalkerRegister.class);
            my.putExtra("RegisterVO", registerVO);
            startActivity(my);


        } else if (id == R.id.user_community) {

            Intent fintent = new Intent(MainActivity.this, UserCommunityMainActivity.class);
            fintent.putExtra("RegisterVO", registerVO);
            startActivity(fintent);


//        } else if (id == R.id.nav_gps) {
//
//            intent = new Intent(MainActivity.this, GpsMainActivity.class);
//
//
//        } else if (id == R.id.nav_dogwalker_gps) {
//
//            intent = new Intent(MainActivity.this, DogwalkerGpsActivity.class);


        }else if(id==R.id.nav_puppy){

            Intent puppy = new Intent(MainActivity.this, MyPetActivity.class);
            puppy.putExtra("RegisterVO", registerVO);
            startActivity(puppy);



        }else if (id == R.id.nav_logout) {

            Intent logout = new Intent(MainActivity.this, LoginMainActivity.class);
            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(logout);


        }else if(id==R.id.my_service){
            Intent service=new Intent(MainActivity.this, MyServiceMainActivity.class);
            service.putExtra("RegisterVO",registerVO);
            startActivity(service);
        }


        if (intent != null) {
            startActivity(intent);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.fab:
                Intent service=new Intent(MainActivity.this, MessageListMainActivity.class);
                service.putExtra("RegisterVO",registerVO);
                startActivity(service);
        }
    }

    @Override
    public void deleteMyServiceEvent(View view, ServiceVO serviceVO) {
        Call<ResponseBody> request = myServiceService.deleteService(Integer.toString(serviceVO.getId()));
        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 성공시
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    //테스트 확인 log값
                    if (body != null) {
                        Log.d("TEST", body.toString());
                    }
                }
                Log.d("TEST", "삭제 성공 ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //실패시
                Log.d("TEST", "삭제 실패" + t.toString());
            }
        });
        updateDogwalkerRealTime(serviceVO);
        //현재 페이지에서 메인 usercommunitymain으로 새로고침 하면서 이동
        setMyServiceList();
        setDogwalkerServiceList();
    }

    @Override
    public void contactMessageEvent(View view, ServiceVO serviceVO) {
        Intent intent = new Intent(MainActivity.this, MessageChattingMainActivity.class);
        intent.putExtra("RegisterVO", registerVO);
        intent.putExtra("ServiceVO", serviceVO);
        intent.putExtra("activityName", "나의서비스");

        startActivity(intent);
    }

    @Override
    public void clickMyServiceEvent(View view, ServiceVO serviceVO) {

        if (registerVO.getUserID().equals(serviceVO.getUser_UserID())) { //사용자일때
            Intent intent = new Intent(MainActivity.this, GpsMainActivity.class); //사용자 전용 gps 액티비티로 이동
            intent.putExtra("ServiceVo", serviceVO);
            startActivity(intent);

        } else if (registerVO.getUserID().equals(serviceVO.getUser_DogwalkerID())){ //로그인한 사람이 도그워커일때
            Intent intent = new Intent(MainActivity.this, DogwalkerGpsActivity.class); //도그워커 전용 gps 액티비티로 이동
            intent.putExtra("ServiceVo",serviceVO);
            startActivity(intent);
        }

    }

    private void setMyServiceList() {
        myServiceAdapter.checkService("my");
        myServiceAdapter.updateService();
        Call<List<ServiceVO>> request = servicePayService.getUserService(registerVO.getUserID());
        request.enqueue(new Callback<List<ServiceVO>>() {
            @Override
            public void onResponse(Call<List<ServiceVO>> call, Response<List<ServiceVO>> response) {

                List<ServiceVO> serviceVOS = response.body();
                // 성공시
                ArrayList<ServiceVO> serviceVOArrayList = new ArrayList<>();
                if (serviceVOS != null) {
                    for (ServiceVO serviceVO : serviceVOS) {
                        //서버로 부터 읽어드린 게시글 리스트를 전부 adapter 에 저장
                        serviceVOArrayList.add(serviceVO);
                        Log.d("TEST", "나의 서비스 가져오기! " + serviceVO.getServiceLocation());
                    }
                    myServiceAdapter.addService(serviceVOArrayList);
                }
                Log.d("TEST", "게시글 통신 성공");
            }

            @Override
            public void onFailure(Call<List<ServiceVO>> call, Throwable t) {
                //실패시
                Log.d("TEST", "게시글 통신 실패");
            }
        });
    }

    private void setDogwalkerServiceList() {

        dogwalkerServiceAdapter.checkService("dogwalker");
        dogwalkerServiceAdapter.updateService();
        Call<List<ServiceVO>> request = servicePayService.getDogwalkerService(registerVO.getUserID());
        request.enqueue(new Callback<List<ServiceVO>>() {
            @Override
            public void onResponse(Call<List<ServiceVO>> call, Response<List<ServiceVO>> response) {

                List<ServiceVO> serviceVOS = response.body();
                // 성공시
                ArrayList<ServiceVO> serviceVOArrayList = new ArrayList<>();
                if (serviceVOS != null) {
                    for (ServiceVO serviceVO : serviceVOS) {
                        //서버로 부터 읽어드린 게시글 리스트를 전부 adapter 에 저장
                        serviceVOArrayList.add(serviceVO);
                        Log.d("TEST", "나의 서비스 가져오기! " + serviceVO.getServiceLocation());
                    }
                    dogwalkerServiceAdapter.addService(serviceVOArrayList);
                }
                Log.d("TEST", "게시글 통신 성공");
            }

            @Override
            public void onFailure(Call<List<ServiceVO>> call, Throwable t) {
                //실패시
                Log.d("TEST", "게시글 통신 실패");
            }
        });
    }

    public void updateDogwalkerRealTime(ServiceVO serviceVO) {
        //실시간 도그워커 사용자가 신청중으로 다시 바꿈

        HashMap<String, Object> inputThread=new HashMap<>();

        inputThread.put("DogwalkerID",serviceVO.getUser_DogwalkerID());
        inputThread.put("selected","0");

        Call<DogwalkerListVO> request = gpsRealTimeDogwalkerService.deleteMyService(inputThread);
        request.enqueue(new Callback<DogwalkerListVO>() {
            @Override
            public void onResponse(Call<DogwalkerListVO> call, Response<DogwalkerListVO> response) {
                // 성공시
                if (response.isSuccessful()) {
                    DogwalkerListVO dogwalkerListVOs = response.body();
                    //테스트 확인 log값
                    if (dogwalkerListVOs != null) {
                        Log.d("TEST", dogwalkerListVOs.getDogwalkerID());
                    }
                }
                Log.d("TEST", "수정 성공 ");
            }

            @Override
            public void onFailure(Call<DogwalkerListVO> call, Throwable t) {
                //실패시
                Log.d("TEST", "수정 실패" + t.getMessage());
            }
        });
        setMyServiceList();
    }
}
