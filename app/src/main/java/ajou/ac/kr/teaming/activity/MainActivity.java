package ajou.ac.kr.teaming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.search.SearchMainActivity;
import ajou.ac.kr.teaming.activity.userCommunity.UserCommunityMainActivity;
import ajou.ac.kr.teaming.service.gallery.GalleryService;
import ajou.ac.kr.teaming.service.history.HistoryService;
import ajou.ac.kr.teaming.service.sample.SampleService;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.vo.SampleVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private SampleService sampleService = ServiceBuilder.create(SampleService.class);
    private GalleryService galleryService = ServiceBuilder.create(GalleryService.class);
    private HistoryService historyService = ServiceBuilder.create(HistoryService.class);

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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_search)  {
            intent = new Intent(getApplicationContext(), SearchMainActivity.class);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if(intent != null) {
            startActivity(intent);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    /**
     * 통신 test
     * @param view
     */

    public void onClickGetButton(View view) {
        Call<SampleVO> request = sampleService.getUser(1);
        request.enqueue(new Callback<SampleVO>() {
            @Override
            public void onResponse(Call<SampleVO> call, Response<SampleVO> response) {

                SampleVO sampleVO = response.body();

                // 성공시
                Log.d(TAG, "통신 성공 data: " + sampleVO);
                Toast.makeText(getApplicationContext(), "통신 성공: name = " + sampleVO.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<SampleVO> call, Throwable t) {
                //실패시
                Log.e(TAG, "통신 실패");
            }
        });
    }

    /**
     * user community event handle
     */

    public void onClickGetCommunityButton(View view) {
        Intent intent = new Intent(MainActivity.this, UserCommunityMainActivity.class);
        startActivity(intent);
    }



}
