package ajou.ac.kr.teaming.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
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

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.gps.DogwalkerGpsActivity;
import ajou.ac.kr.teaming.activity.gps.GpsMainActivity;
import ajou.ac.kr.teaming.activity.login.DogwalkerRegister;
import ajou.ac.kr.teaming.activity.login.LoginMainActivity;
import ajou.ac.kr.teaming.activity.login.MyActivity;
import ajou.ac.kr.teaming.activity.login.MyPet2Activity;
import ajou.ac.kr.teaming.activity.myService.MyServiceMainActivity;
import ajou.ac.kr.teaming.activity.reservation.ReservationActivity;
import ajou.ac.kr.teaming.activity.userCommunity.UserCommunityMainActivity;
import ajou.ac.kr.teaming.service.gallery.GalleryService;
import ajou.ac.kr.teaming.service.gps.GpsService;
import ajou.ac.kr.teaming.service.history.HistoryService;
import ajou.ac.kr.teaming.service.sample.SampleService;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private SampleService sampleService = ServiceBuilder.create(SampleService.class);
    private GalleryService galleryService = ServiceBuilder.create(GalleryService.class);
    private HistoryService historyService = ServiceBuilder.create(HistoryService.class);
    private GpsService gpsService = ServiceBuilder.create(GpsService.class);
    private RegisterVO registerVO;

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
        TextView text = (TextView) header.findViewById(R.id.idView);
        text.setText("xxxxx");
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


        } else if (id == R.id.nav_gps) {

            intent = new Intent(MainActivity.this, GpsMainActivity.class);


        } else if (id == R.id.nav_dogwalker_gps) {

            intent = new Intent(MainActivity.this, DogwalkerGpsActivity.class);


        }else if(id==R.id.nav_puppy){

            Intent puppy = new Intent(MainActivity.this, MyPet2Activity.class);
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

}
