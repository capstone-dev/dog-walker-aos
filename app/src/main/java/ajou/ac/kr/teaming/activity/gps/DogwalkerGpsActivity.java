package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.LogManager;

/**  <도그워커 메인 액티비티>
 *
 * 도그워커는 사진찍기를 누를 시, 자신의 현재 위치에 마커가 생성되며, 위치정보와 사진정보가 기록된다.
 * 위치정보와 사진정보는 마커를 눌러 풍선뷰를 통해 확인할 수 있다.
 * 산책 시, 자신의 위치정보를 반영하여, 자신이 산책한 거리와 시간을 기록한다.
 * 위의 정보들은 강아지 주인도 확인할 수 있다.
 * 사진찍기는 핸드폰 기본앱과 연동하여 사용한다.
 * 위의 기록들은 히스토리에 남겨지도록 한다.
 * 산책 종료를 누르면 이번 산책의 코멘트를 남길 수 있다.
 *
 * (도그워커는 자신이 생성한 마커를 삭제할 수 있다.)
 * 강아지 주인과 통화하기 및 채팅하기 기능
 * ----------------------------------------------------------------------------
 *
 * - 생성해야할 메소드
 *
 * 1. 사진찍기
 * 2. 마커 생성
 * 3. 풍선뷰 불러오기
 * 4. 위치정보 받기
 * 5. 사진 정보 기록
 * 6. 산책거리 계산 및 반영
 * 7. 산책시간 계산 및 반영
 * 8. 산책 종료시 코멘트 화면 이동
 * */


public class DogwalkerGpsActivity extends AppCompatActivity {


    private final String TMAP_API_DOGWALKER_KEY = "d6eadaec-baa2-4266-b054-204122d5a779";



    /***
     * 버튼 아이디 정리
     */
    private static final int[] mArrayMapDogwalkerButton = {
            R.id.btnCallToUser,
            R.id.btnChatToUser,
            R.id.btnPhotoAndMarker,
            R.id.btnGetDogWalkerLocationPoint,
            R.id.btnWalkDistance,
            R.id.btnWalkEnd,
    };

    private double m_Latitude;
    private double m_Longitude;

    private PermissionManager permissionManager = null;
    private boolean m_bTrackingMode;
    private TMapView tMapView = null;
    private TMapGpsManager tMapGps = null;

    /**
     * setSKTMapApiKey()에 ApiKey를 입력 한다.
     */
    private void apiKeyMapView() {
        tMapView.setSKTMapApiKey(TMAP_API_DOGWALKER_KEY);
    }

    /**
     * 권한 요청 관리자
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionManager.setResponse(requestCode, grantResults); // 권한요청 관리자에게 결과 전달
    }


    /**
     * onCreate
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_gps);

        LinearLayout linearLayoutDogwalkerTmap = (LinearLayout)findViewById(R.id.linearLayoutDogwalkerTmap);
        tMapView = new TMapView(this);
        apiKeyMapView(); //T MAP API 서버키 인증
        linearLayoutDogwalkerTmap.addView( tMapView );

        initView();
        setGps();

        /**
         * PermissionManager 클래스에서 상속
         * 위치정보 허용기능
         * */
        permissionManager.request(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionManager.PermissionListener() {
            @Override
            public void granted() {
                tMapGps = new TMapGpsManager(DogwalkerGpsActivity.this);
                tMapGps.setMinTime(1000);
                tMapGps.setMinDistance(5);
                tMapGps.setProvider(tMapGps.GPS_PROVIDER);//gps를 이용해 현 위치를 잡는다.
                tMapGps.OpenGps();
                tMapGps.setProvider(tMapGps.NETWORK_PROVIDER);//연결된 인터넷으로 현 위치를 잡는다.
                tMapGps.OpenGps();
            }
            @Override
            public void denied() {
                Log.w("LOG", "위치정보 접근 권한이 필요합니다.");
            }
        });


        /**
         * 클릭 이벤트 설정 구간
         * Toast시. MapEvent.this가 아닌 getApplicationContext()를 사용할 것.
         * **/
        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //    Toast.makeText(getApplicationContext(), "onPress~!", Toast.LENGTH_SHORT).show();

                return false;
            }
            @Override
            public boolean onPressUpEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //   Toast.makeText(getApplicationContext(), "onPressUp~!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });




        // 롱 클릭 이벤트 설정
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint) {
                //    Toast.makeText(getApplicationContext(), "onLongPress~!", Toast.LENGTH_SHORT).show();
            }
        });

        // 지도 스크롤 종료시, 확대 레벨 및 경도 위도 출력
        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                //       Toast.makeText(getApplicationContext(), "zoomLevel=" + zoom +
                //                                       "\n경도 =" + centerPoint.getLongitude() +
                //                                      "\n위도 =" + centerPoint.getLatitude(), Toast.LENGTH_SHORT).show();
            }
        });


    }//onCreate


    /**
     * 현재 위치정보 받기
     * LocationListener와 setGps를 통해 현재위치를 gps를 통해 받아온다.
     * */
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                tMapView.setLocationPoint(longitude, latitude);
                tMapView.setCenterPoint(longitude, latitude);
            }
        }
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void setGps(){
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }




    /**
     * 버튼 리스너 등록
     * */
    private void initView() {
        for (int btnMapView : mArrayMapDogwalkerButton) {
            Button ActiveButton = (Button) findViewById(btnMapView);
            ActiveButton.setOnClickListener(this::onClick);
        }
    }

    /**
     * onClick Event
     */
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnCallToUser		                  : 	callToUser(); 			        break;
            case R.id.btnChatToUser		                  : 	chatToUser(); 			        break;
            case R.id.btnPhotoAndMarker	               	  : 	makePhotoAndMarker(); 			break;
            case R.id.btnGetDogWalkerLocationPoint		  : 	getDogwalkerLocationPoint(); 	break;
            case R.id.btnWalkDistance		              : 	walkDistance(); 			    break;
            case R.id.btnWalkEnd		                  : 	walkEnd(); 			            break;
        }
    }


    /**
     * 강아지 주인과 통화하기
     * */
    private void callToUser() {

    }

    /**
     * 강아지 주인과 채팅하기
     * */

    private void chatToUser(){

    }

    /**
     * 사진 찍기 시 마커 생성
     * */
    private void makePhotoAndMarker() {

    }

    /**
     * 도그워커의 위치 받기
     * */
    private void getDogwalkerLocationPoint() {
        TMapPoint point = tMapView.getLocationPoint();
        double Latitude = point.getLatitude();
        double Longitude = point.getLongitude();

        m_Latitude  = Latitude;
        m_Longitude = Longitude;

        LogManager.printLog("Latitude " + Latitude + " Longitude " + Longitude);
        String strResult = String.format("Latitude = %f Longitude = %f", Latitude, Longitude);

        Common.showAlertDialog(this, "", strResult);
    }

    /**
     * 도그워커의 이동거리 계산
     * */
    private void walkDistance() {

    }


    /**
     * 도그워커 산책 종료
     **/
    private void walkEnd() {

    }


}//DogwalkerGpsActivity




