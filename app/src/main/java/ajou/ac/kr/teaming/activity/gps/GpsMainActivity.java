package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapLabelInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.LogManager;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.gps.GpsDogwalkerLocationService;
import ajou.ac.kr.teaming.service.gps.GpsMarkerService;
import ajou.ac.kr.teaming.service.gps.GpsService;
import ajou.ac.kr.teaming.vo.GpsLocationVo;
import ajou.ac.kr.teaming.vo.GpsMarkerVo;
import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.PhotoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**  <사용자 메인 액티비티>
 *
 * 사용자는 Tmap 지도를 사용할 수 있다.
 * 도그워커의 위치를 파악할 수 있다.
 * 도그워커가 이동한 경로 및 거리와 산책 시간을 확인할 수 있다.
 * 마커를 눌렀을 시, 풍선뷰가 생성된다.
 * 풍선뷰를 통해 도그워커가 남긴 사진정보와 위치 정보를 확인할 수 있다.
 *
 *
 * ----------------------------------------------------------------------------
 *
 * - 생성해야할 메소드
 *
 * <showMarkerPoint>
 * 1. 마커 클릭
 * 1-1.풍선뷰 안의 사진정보와 위치정보 확인
 * 2. 풍선뷰 확인
 *
 * <fab>
 * <btnTrackDogWalkerFAB>
 * 3. 도그워커 위치 추적 (fab)
 *
 * <drawPedestrianPath>
 * 4. 도그워커의 이동경로 지도에 표시
 *
 *
 * <showDistanceAndTime>
 * 5. 도그워커가 이동한 거리 및 산책시간 확인
 *
 * 2.3.4.5는 도그워커의 지도를 공유하는 방식으로 만들 생각.
 *
 *
 * */





public class GpsMainActivity extends AppCompatActivity{

    private final String TAG = "GpsMainActivity";
    private final String TMAP_API_KEY = "78f4044b-3ca4-439d-8d0e-10135941f054";
    private Context mContext;
    private TMapView tMapView = null;
    private FloatingActionButton btnTrackDogWalkerFAB = null;

    TMapGpsManager tMapGps = null;
    PermissionManager permissionManager = null; // 권한요청 관리자

    private boolean m_bShowMapIcon = false;
    private boolean m_bTrafficeMode = false;
    private boolean m_bSightVisible = false;
    private boolean m_bTrackingMode = false;
    private boolean m_bOverlayMode = false;
    private int m_nCurrentZoomLevel = 0;
    private ArrayList<Bitmap> mOverlayList;

    private double m_Latitude;
    private double m_Longitude;
    private static int 	mMarkerID;
    ArrayList<String> mArrayMarkerID;

    private double dogwalkerLatitude;
    private double dogwalkerLongitude;
    private double startDogwalkerLatitude;
    private double startDogwalkerLongitude;
    private double endDogwalkerLatitude;
    private double endDogwalkerLongitude;
    private String walkDistance;
    private String start_time;
    private String end_time;
    private String walkTime;


    ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();


    /***
     * 버튼 아이디 정리
     */
    private static final int[] mArrayMapButton = {
/*          R.id.btnSetMapType,
            R.id.btnSetDogwalkerLocation,
            R.id.btnGetDogwalkerLocation,
            R.id.btnSetCompassMode,
            R.id.btnGetIsCompass,
            R.id.btnSetSightVisible,
            R.id.btnSetTrackIngMode,
            R.id.btnGetIsTracking,
            R.id.btnMapPath,
            R.id.btnRemoveMapPath,
            R.id.btnDisplayMapInfo,
            R.id.btnPedestrian_Path,
            R.id.btnGetCenterPoint,
            R.id.btnFindAllPoi,
            R.id.btnConvertToAddress,
            R.id.btnTileType,
            R.id.btnMarkerPoint2*/
    };
    private TextView txtusergongback;
    private TextView txtShowWalkDistance;
    private TextView txtShowWalkTime;
    private int gpsId;

    /**
     * setSKTMapApiKey()에 ApiKey를 입력 한다.
     */
    private void apiKeyMapView() {
        tMapView.setSKTMapApiKey(TMAP_API_KEY);
    }


    /**
     * OnCreate
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_gps_main);

        mContext = this;
        permissionManager = new PermissionManager(this); // 권한요청 관리자


        /**지도 생성*/
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        apiKeyMapView(); //T MAP API 서버키 인증
        linearLayoutTmap.addView( tMapView );

        initView(); //리스너 실행

        txtusergongback = (TextView) findViewById(R.id.txtusergongback);
        txtShowWalkDistance = (TextView) findViewById(R.id.txtShowWalkDistance);
        txtShowWalkTime = (TextView) findViewById(R.id.txtShowWalkTime);



        //이동경로를 그리기 전 도그워커의 GPS INFO를 받아와야함.
        traceStart();
        //도그워커 이동경로 그리기
     //   showDistanceAndTime(); //도그워커의 이동거리 및 시간 보여주기



        /***
         * FAB버튼
         *강아지 주인의 현재 위치를 추적하는 아이콘으로 만들 생각
         */

        btnTrackDogWalkerFAB = (FloatingActionButton) findViewById(R.id.btnFloatingActionButton);
        btnTrackDogWalkerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.setZoomLevel(16);
                tMapView.setCenterPoint(endDogwalkerLongitude, endDogwalkerLongitude);
                Toast.makeText(getApplicationContext(), "도그워커의 현재 위치를 찾는 중입니다.\n잠시 기다려 주세요.", Toast.LENGTH_LONG).show();

                getGpsInfo();
                getLocationInfo();
                getMarkerInfo();
                drawPedestrianPath();
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

//        /**
//         * PermissionManager 클래스에서 상속
//         * 위치정보 허용기능
//         * */
//        permissionManager.request(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionManager.PermissionListener() {
//            @Override
//            public void granted() {
//                tMapGps = new TMapGpsManager(GpsMainActivity.this);
//                tMapGps.setMinTime(1000);
//                tMapGps.setMinDistance(5);
//                tMapGps.setProvider(tMapGps.GPS_PROVIDER);//gps를 이용해 현 위치를 잡는다.
//                tMapGps.OpenGps();
//                tMapGps.setProvider(tMapGps.NETWORK_PROVIDER);//연결된 인터넷으로 현 위치를 잡는다.
//                tMapGps.OpenGps();
//            }
//            @Override
//            public void denied() {
//                Log.w("LOG", "위치정보 접근 권한이 필요합니다.");
//            }
//        });

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.e(TAG, "Permission Granted");
                Toast.makeText(GpsMainActivity.this, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Log.e(TAG, "Permission Denied");
                Toast.makeText(GpsMainActivity.this,"권한이 거부되었습니다."+ deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                //finish();
            }
        };

        /**
         * 위치 이외에 카메라, 저장공간 접근 권한 허용을 위해 만든 부분
         * 오픈소스인 TedPermission을 이용하여 쉽게 구현
         * */
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("산책 서비스를 이용하기 위해 위치, 저장공간, 카메라 접근 권한을 허용해주세요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                        Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        /**
         * 퍼미션이 허용되거나 거부되었을 때 하는 액션 메소드
         * */

    }//onCreate




    /**
     * initView - 버튼에 대한 리스너를 등록한다.
     */
    private void initView() {
        for (int btnMapView : mArrayMapButton) {
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
                if (!markerlist.isEmpty()) {
                    TMapMarkerItem item = markerlist.get(markerlist.size() - 1);
                    ImageView photoFromCameraImageView = findViewById(R.id.photoFromServer);
                    photoFromCameraImageView.bringToFront();
                    Picasso.get().load("http://52.79.234.182:3000/gps/marker/image?markerId=" + item.getID()).into(photoFromCameraImageView);
                    photoFromCameraImageView.setVisibility(View.VISIBLE);
                }

                LogManager.printLog("MainActivity onPressUpEvent " + markerlist.size());
                return true;
            }

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                return false;
            }
        });

        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point) {
                LogManager.printLog("MainActivity onLongPressEvent " + markerlist.size());
            }
        });

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                String strMessage = "";
                strMessage = "ID: " + markerItem.getID() + " " + "Title " + markerItem.getCalloutTitle();
                Common.showAlertDialog(GpsMainActivity.this, "Callout Right Button", strMessage);
            }
        });

        tMapView.setOnClickReverseLabelListener(new TMapView.OnClickReverseLabelListenerCallback() {
            @Override
            public void onClickReverseLabelEvent(TMapLabelInfo findReverseLabel) {
                if(findReverseLabel != null) {
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
        if( tMapGps != null ) {
            tMapGps.CloseGps();
        }
        if(mOverlayList != null){
            mOverlayList.clear();
        }
    }


    /**
     * onClick Event
     */
    public void onClick(View v) {
        switch(v.getId()) {
//            case R.id.btnSetMapType		  :		setMapType(); 			break;
        }
    }


    /**
     * 마커에 포함된 사진 hidden
     * @param view
     * activity_gps_main.xml
     */
    public void clickuserMethod(View view) {
        view.setVisibility(View.GONE);
    }


    /**
     * showMarkerPoint
     * 지도에 마커를 표출한다.
     *
     */ //TODO : 도그워커가 찍은 사진 불러와 마커 생성

    public void showMarkerPoint(int photoId, double photoLatitude, double photoLongitude) {
        TMapMarkerItem markerItem = new TMapMarkerItem();
        markerItem.setTMapPoint(new TMapPoint(photoLatitude, photoLongitude));
        markerItem.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
        tMapView.addMarkerItem("" + photoId, markerItem);
    }



    /**
     * 도그워커의 이동경로를 그리는 메소드
     * */
    public void drawPedestrianPath() {

        //  double startWalkLatitude = startDogwalkerLatitude;
        //  double startWalkLongitude = startDogwalkerLongitude;

//        alTMapPoint.add( new TMapPoint(startDogwalkerLatitude, startDogwalkerLongitude) ); // 도그워커 출발지점
//        alTMapPoint.add( new TMapPoint(endDogwalkerLatitude, endDogwalkerLongitude) ); // 도그워커 끝지점

        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.YELLOW);
        tMapPolyLine.setLineWidth(2);
        for( int i=0; i<alTMapPoint.size(); i++ ) {
            tMapPolyLine.addLinePoint( alTMapPoint.get(i) );
        }
        tMapView.addTMapPolyLine("Line1", tMapPolyLine);
    }


    /**
     * 도그워커의 산책시간 및 이동거리를 표시해주는 메소드
     * */
    public void showDistanceAndTime(){
        String WalkContext = String.format("%.5s",walkDistance);
        txtShowWalkDistance.setText( WalkContext + "m" );

        Long showWalkTime = Long.parseLong(walkTime);
        Date walkdate = new Date(showWalkTime);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(walkdate);
        txtShowWalkTime.setText(formatDate.substring(0,8));
    }


    /**
     * convertToAddress
     * 지도에서 선택한 지점을 주소를 변경요청한다. 
     */
    public void convertToAddress() {
        TMapPoint point = tMapView.getCenterPoint();

        TMapData tmapdata = new TMapData();

        if (tMapView.isValidTMapPoint(point)) {
            tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                @Override
                public void onConvertToGPSToAddress(String strAddress) {
                    LogManager.printLog("선택한 위치의 주소는 " + strAddress);
                }
            });
        }
    }



    private void traceStart() {

        /**산책 시작 시 동작해야할 것
         * 1. 산책시간 측정
         * 2. 자신의 위치 정보 반환
         * 3. 위치 이동을 통해 보행자 길 표시
         * 4. 서버 전송
         * 5. 사진찍기 및 마커생성 동작 가능
         * */

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(GpsMainActivity.this);
        alert_confirm.setMessage("확인 버튼을 눌러 도그워커 추적을 시작합니다.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'

                        getGpsInfo();
                        getLocationInfo();
                        getMarkerInfo();

                    }
                }).setNegativeButton("뒤로 가기",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }



    /**
     * Retrofit Method
     * */

    /**
     * GET GPS INFO
     * 도그워커의 산책정보(시작위치, 끝위치, 이동거리, 시작시간, 끝시간 등)을 서버로부터 받아온다.
     */
    public void getGpsInfo() {
        GpsService gpsService = ServiceBuilder.create(GpsService.class);
        Call<GpsVo> call = gpsService.doGetGpsInfo(78); // TODO : 동적 할당이 되도록 만들기
        call.enqueue(new Callback<GpsVo>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<GpsVo> call, @NonNull Response<GpsVo> response) {
                GpsVo gpsGetVo = response.body();
                if(gpsGetVo != null){
//                    Toast.makeText(getApplicationContext(), "도그워커 위도" + gpsGetVo.getDogwalkerLatitude()
//                            + "도그워커 경도" + gpsGetVo.getDogwalkerLongitude(), Toast.LENGTH_SHORT).show();
                    Log.d("TEST", "onResponse getId: " + gpsGetVo.getId());
                    Log.d("TEST", "onResponse getStartDogwalkerLatitude: " + gpsGetVo.getStartDogwalkerLatitude());
                    Log.d("TEST", "onResponse getStartDogwalkerLongitude: " + gpsGetVo.getStartDogwalkerLongitude());
                    Log.d("TEST", "onResponse getEndDogwalkerLatitude: " + gpsGetVo.getEndDogwalkerLatitude());
                    Log.d("TEST", "onResponse getEndDogwalkerLongitude: " + gpsGetVo.getEndDogwalkerLongitude());
                    Log.d("TEST", "onResponse getWalkDistance: " + gpsGetVo.getWalkDistance());
                    Log.d("TEST", "onResponse getStart_time: " + gpsGetVo.getStart_time());
                    Log.d("TEST", "onResponse getEnd_time: " + gpsGetVo.getEnd_time());
                    Log.d("TEST", "onResponse getWalkTime: " + gpsGetVo.getWalkTime());


                    /**서버로부터 받은 데이터를 저장*/
                    startDogwalkerLatitude = gpsGetVo.getStartDogwalkerLatitude();
                    startDogwalkerLongitude = gpsGetVo.getStartDogwalkerLongitude();
                    endDogwalkerLatitude = gpsGetVo.getEndDogwalkerLatitude();
                    endDogwalkerLongitude = gpsGetVo.getEndDogwalkerLongitude();
                    walkDistance = gpsGetVo.getWalkDistance();
                    start_time = gpsGetVo.getStart_time();
                    end_time = gpsGetVo.getEnd_time();
                    walkTime = gpsGetVo.getWalkTime();

                    showDistanceAndTime();
                    alTMapPoint.add( new TMapPoint(startDogwalkerLatitude, startDogwalkerLongitude) ); // 도그워커 출발지점
                    alTMapPoint.add( new TMapPoint(endDogwalkerLatitude,endDogwalkerLongitude)); //도그워커 마지막지점
                }
                Log.d("TEST", "도그워커 산책정보 받기 성공");
            }
            @Override
            public void onFailure(@NonNull Call<GpsVo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패 : 도그워커 산책 정보",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패 : 도그워커 산책 정보");
            }
        });
    }


    /**
     * GET MARKER INFO
     * 도그워커가 촬영한 사진 및 마커 데이터를 서버로부터 받아온다.
     */
    public void getMarkerInfo() {
        GpsMarkerService gpsMarkerService = ServiceBuilder.create(GpsMarkerService.class);
        Call<List<PhotoVO>> call = gpsMarkerService.doGetMarkerInfo(78); //TODO : 동적 할당이 되도록 만들기
        call.enqueue(new Callback <List<PhotoVO>>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<List<PhotoVO>> call, @NonNull Response<List<PhotoVO>> response) {
                List<PhotoVO> gpsPhotoVo = response.body();
                if(gpsPhotoVo != null){
                    //배열로 받아와야한다.
                    for(int i = 0; i < gpsPhotoVo.size(); i++){
                        int photoId = gpsPhotoVo.get(i).getId();
                        double photoLatitude = gpsPhotoVo.get(i).getPhotoLatitude();
                        double photoLongitude = gpsPhotoVo.get(i).getPhotoLongitude();

                        showMarkerPoint(photoId, photoLatitude, photoLongitude); //사진Id를 이용해 마커 생성
                    }
//                    Toast.makeText(getApplicationContext(), "사진 위도" + gpsGetMarkerVo.getPhotoLatitude()
//                            + "사진 경도" + gpsGetMarkerVo.getPhotoLongitude(), Toast.LENGTH_SHORT).show();
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getId());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getPhotoLatitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getPhotoLongitude());

                }
                Log.d("TEST", "도그워커 사진정보 받기 성공");
            }
            @Override
            public void onFailure(@NonNull Call<List<PhotoVO>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패 : 사진 및 마커 정보",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패 : 사진 및 마커 정보");
            }
        });
    }


    /**
     * GET LOCATION INFO
     * 도그워커의 현재 위치를 서버로부터 받아온다.
     */
    public void getLocationInfo() {
        GpsDogwalkerLocationService gpsDogwalkerLocationService = ServiceBuilder.create(GpsDogwalkerLocationService.class);
        Call<List<GpsLocationVo>> call = gpsDogwalkerLocationService.doGetLocationInfo(78); //TODO : 동적 할당이 되도록 만들기
        call.enqueue(new Callback<List<GpsLocationVo>>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<List<GpsLocationVo>> call, @NonNull Response<List<GpsLocationVo>> response) {
                List<GpsLocationVo> gpsGetLocationVo = response.body();
                if(gpsGetLocationVo != null){

                    //배열로 받아와야한다.
                    for (int i = 0 ; i < gpsGetLocationVo.size() ; i++){

                        double dogwalkerLatitude = gpsGetLocationVo.get(i).getDogwalkerLatitude();
                        double dogwalkerLongitude = gpsGetLocationVo.get(i).getDogwalkerLongitude();

                        /**서버로부터 받은 데이터를 저장*/
                        alTMapPoint.add(new TMapPoint(dogwalkerLatitude, dogwalkerLongitude));
                        drawPedestrianPath();
                    }

                }
                Log.d("TEST", "도그워커 현재위치 받기 성공");
            }
            @Override
            public void onFailure(@NonNull Call<List<GpsLocationVo>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패 : 도그워커 현재위치 정보",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패 : 도그워커 현재위치 정보");
            }
        });
    }


}//GpsMainActivity

