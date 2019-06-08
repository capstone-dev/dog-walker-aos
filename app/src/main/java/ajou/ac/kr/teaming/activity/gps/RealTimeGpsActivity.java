package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapLabelInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.LogManager;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.gps.GpsDogwalkerLocationService;
import ajou.ac.kr.teaming.vo.GpsLocationVo;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealTimeGpsActivity extends AppCompatActivity {

    private RegisterVO registerVO;

    private final String TAG = "RealTimeGpsActivity";
    private final String TMAP_API_KEY = "78f4044b-3ca4-439d-8d0e-10135941f054";
    private Context mContext;
    private TMapView tMapView = null;
    private FloatingActionButton btnTrackDogWalkerFAB = null;
    private TMapGpsManager gps;


    TMapGpsManager tMapGps = null;
    PermissionManager permissionManager = null; // 권한요청 관리자
    TMapCircle tMapCircle = null;

    private boolean m_bShowMapIcon = false;
    private boolean m_bTrafficeMode = false;
    private boolean m_bSightVisible = false;
    private boolean m_bTrackingMode = false;
    private boolean m_bOverlayMode = false;
    private int m_nCurrentZoomLevel = 0;
    private ArrayList<Bitmap> mOverlayList;

    private double userLatitude;
    private double userLongitude;
    private double dogwalkerLatitude;
    private double dogwalkerLongitude;
    private static int mMarkerID;
    ArrayList<String> mArrayMarkerID;
    Intent intent = null;

    private double betweenUserDistance;
    ArrayList<TMapPoint> dogwalkerRealtimeLocationPoint = new ArrayList<TMapPoint>();

    /***
     * 버튼 아이디 정리
     */
    private static final int[] mArrayRealTimeMapButton = {
            R.id.btnEnlargeCircle,
            R.id.btnNarrowCircle,
            R.id.btnIntentWalkerPage,
    };
    private int markerId;


    /**
     * setSKTMapApiKey()에 ApiKey를 입력 한다.
     */
    private void apiKeyMapView() {
        tMapView.setSKTMapApiKey(TMAP_API_KEY);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_gps);


        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");


        mContext = this;
        permissionManager = new PermissionManager(this); // 권한요청 관리자


        /**지도 생성*/
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        apiKeyMapView(); //T MAP API 서버키 인증
        linearLayoutTmap.addView(tMapView);


        gps = new TMapGpsManager(RealTimeGpsActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }

        initView(); //리스너 실행



        tMapView.setTrackingMode(true);
        tMapView.setZoomLevel(15);
        tMapView.setIconVisibility(true);
        setGps();
        Toast.makeText(getApplicationContext(), "현재 위치를 찾는 중입니다.", Toast.LENGTH_LONG).show();
        tMapGps = new TMapGpsManager(RealTimeGpsActivity.this);
        tMapGps.setMinTime(1000);
        tMapGps.setMinDistance(5);
        tMapGps.setProvider(tMapGps.GPS_PROVIDER);//gps를 이용해 현 위치를 잡는다.
        tMapGps.OpenGps();
        tMapGps.setProvider(tMapGps.NETWORK_PROVIDER);//연결된 인터넷으로 현 위치를 잡는다.
        tMapGps.OpenGps();



        /***
         * FAB버튼
         * 사용자의 현재 위치를 찾아줌.
         */
        btnTrackDogWalkerFAB = (FloatingActionButton) findViewById(R.id.btnFloatingActionButton);
        btnTrackDogWalkerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.setTrackingMode(true);
                tMapView.setZoomLevel(15);
                tMapView.setIconVisibility(true);

                setGps();
                Toast.makeText(getApplicationContext(), "현재 위치를 찾는 중입니다.", Toast.LENGTH_LONG).show();
                tMapGps = new TMapGpsManager(RealTimeGpsActivity.this);
                tMapGps.setMinTime(1000);
                tMapGps.setMinDistance(5);
                tMapGps.setProvider(tMapGps.GPS_PROVIDER);//gps를 이용해 현 위치를 잡는다.
                tMapGps.OpenGps();
                tMapGps.setProvider(tMapGps.NETWORK_PROVIDER);//연결된 인터넷으로 현 위치를 잡는다.
                tMapGps.OpenGps();

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
    }//initView


    /**
     * 현재 위치정보 받기
     * LocationListener와 setGps를 통해 현재위치를 gps를 통해 받아온다.
     */
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            userLatitude = location.getLatitude();
            userLongitude = location.getLongitude();


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

    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000 * 5, // 통지사이의 최소 시간간격 (miliSecond)
                10, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000 * 5, // 통지사이의 최소 시간간격 (miliSecond)
                10, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }


    /**
     * initView - 버튼에 대한 리스너를 등록한다.
     */
    private void initView() {
        for (int btnMapView : mArrayRealTimeMapButton) {
            Button ActiveButton = (Button) findViewById(btnMapView);
            ActiveButton.setOnClickListener(this::onClick);
        }

        tMapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKTMapApikeySucceed() {
                LogManager.printLog("MainActivity SKTMapApikeySucceed");
            }

            @Override
            public void SKTMapApikeyFailed(String errorMsg) {
                LogManager.printLog("MainActivity SKTMapApikeyFailed " + errorMsg);
            }
        });


        tMapView.setOnEnableScrollWithZoomLevelListener(new TMapView.OnEnableScrollWithZoomLevelCallback() {
            @Override
            public void onEnableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                LogManager.printLog("MainActivity onEnableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
            }
        });

        tMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                LogManager.printLog("MainActivity onDisableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
            }
        });

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                LogManager.printLog("MainActivity onPressUpEvent " + markerlist.size());
                return false;
            }


            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                LogManager.printLog("MainActivity onPressEvent " + markerlist.size());

                for (int i = 0; i < markerlist.size(); i++) {
                    TMapMarkerItem item = markerlist.get(i);
                    LogManager.printLog("MainActivity onPressEvent " + item.getName() + " " + item.getTMapPoint().getLatitude() + " " + item.getTMapPoint().getLongitude());
                }
                return false;
            }
        });

        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point) {
                LogManager.printLog("MainActivity onLongPressEvent " + markerlist.size());
            }
        });

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                String strMessage = "";
                strMessage = "ID: " + markerItem.getID() + " " + "Title " + markerItem.getCalloutTitle();
                Common.showAlertDialog(RealTimeGpsActivity.this, "Callout Right Button", strMessage);
            }
        });

        tMapView.setOnClickReverseLabelListener(new TMapView.OnClickReverseLabelListenerCallback() {
            @Override
            public void onClickReverseLabelEvent(TMapLabelInfo findReverseLabel) {
                if (findReverseLabel != null) {
                    LogManager.printLog("MainActivity setOnClickReverseLabelListener " + findReverseLabel.id + " / " + findReverseLabel.labelLat
                            + " / " + findReverseLabel.labelLon + " / " + findReverseLabel.labelName);

                }
            }
        });

        m_nCurrentZoomLevel = -1;
        m_bShowMapIcon = false;
        m_bTrafficeMode = false;
        m_bSightVisible = false;
        m_bTrackingMode = false;

    }//initView


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tMapGps != null) {
            tMapGps.CloseGps();
        }
        if (mOverlayList != null) {
            mOverlayList.clear();
        }
    }


    /**
     * onClick Event
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIntentWalkerPage:
                intentWalkerListPage();
                break;
        }
    }

    private void intentWalkerListPage() {
        Intent intent = new Intent(RealTimeGpsActivity.this, RealTimeDogWalkerListAcitvity.class);
        intent.putExtra("RegisterVO", registerVO);
        startActivity(intent);
    }

    //도그워커의 현재 위치를 전부 표시
    public void showDogwalkerLocationMarker() { //TODO : 실시간 도그워커를 등록하면 현재 위치를 전달해 마커를 생성
        for(int i = 0; i < dogwalkerRealtimeLocationPoint.size(); i++) { //i == markerid
            Log.d("TEST", "도그워커 위치 마커 생성" + i);
            //도그워커의 현재위치에 마커 생성
            TMapMarkerItem markerItem = new TMapMarkerItem();
            String strID = String.format("%02d", i); //두자릿수로 나오도록 설정
            markerId = i; //마커 아이디 변수 지정
            // 마커 아이콘 지정
            markerItem.getTMapPoint();
            markerItem.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
            // 마커의 좌표 지정
            markerItem.setTMapPoint(dogwalkerRealtimeLocationPoint.get(i));
            markerItem.setCanShowCallout(true);
            markerItem.setCalloutTitle("테스트" + strID); //도그워커 이름으로
            markerItem.setCalloutSubTitle("안녕하세요" + strID); //도그워커와의 거리로

            tMapView.addMarkerItem(strID, markerItem);
        }
    }


    /**
     * 도그워커 실시간으로 가능한 리스트 보여주는 이벤트 handle
     *
     * @param view
     */
    public void onClickShowListButton(View view) {
        Intent intent = new Intent(RealTimeGpsActivity.this, RealTimeDogWalkerListAcitvity.class);
        startActivity(intent);
    }


    /**
     * 도그워커와의 거리 계산
     */

    private void distanceBetweenUser() {

        //사용자 자신의 위치와 도그워커간의 거리를 구한다.
        betweenUserDistance = distance(userLatitude, userLongitude, dogwalkerLatitude, dogwalkerLongitude, "kilometer");
    }

    /**
     *
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     * @return dist*/

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }
        return (dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }






    /**
     * GET RealTime Dogwalker Location
     * 도그워커의 실시간 현재 위치를 서버로부터 받아온다.
     *
     *
     * Service객체와 Vo부분 수정 요망
     *
     *
     */
   /* public void getRealTimeDogwalkerLocationInfo() {
        GpsDogwalkerLocationService gpsDogwalkerLocationService = ServiceBuilder.create(GpsDogwalkerLocationService.class);
        Call<GpsLocationVo> call = gpsDogwalkerLocationService.doGetLocationInfo();
        call.enqueue(new Callback<GpsLocationVo>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<GpsLocationVo> call, @NonNull Response<GpsLocationVo> response) {
                GpsLocationVo gpsGetLocationVo = response.body();
                if(gpsGetLocationVo != null){
                    Toast.makeText(getApplicationContext(), "도그워커 현재 위도" + gpsGetLocationVo.getDogwalkerLatitude()
                            + "도그워커 현재 경도" + gpsGetLocationVo.getDogwalkerLongitude(), Toast.LENGTH_SHORT).show();

//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getMarkerId());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getPhotoURL());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getPhotoLatitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getPhotoLongitude());
                    Log.d("TEST", "onResponse: " + gpsGetLocationVo.getDogwalkerLatitude());
                    Log.d("TEST", "onResponse: " + gpsGetLocationVo.getDogwalkerLongitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getStartDogwalkerLatitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getStartDogwalkerLongitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getEndDogwalkerLatitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getEndDogwalkerLongitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getWalkDistance());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getStart_time());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getEnd_time());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getWalkTime());

                    *//**서버로부터 받은 데이터를 저장*//*
                    dogwalkerLatitude = gpsGetLocationVo.getDogwalkerLatitude();
                    dogwalkerLongitude = gpsGetLocationVo.getDogwalkerLongitude();

                    dogwalkerRealtimeLocationPoint.add( new TMapPoint(dogwalkerLatitude, dogwalkerLongitude));
                }
                Log.d("TEST", "onResponse:END ");
            }
            @Override
            public void onFailure(@NonNull Call<GpsLocationVo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패\n도그워커의 현재 위치를 전달받을 수 없습니다.",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패");
            }
        });
    }*/

}// RealTimeGpsActivity





