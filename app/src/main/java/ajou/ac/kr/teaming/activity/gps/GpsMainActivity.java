package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.skt.Tmap.TMapLabelInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.LogManager;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.gps.GpsDogwalkerLocationService;
import ajou.ac.kr.teaming.service.gps.GpsMarkerService;
import ajou.ac.kr.teaming.service.gps.GpsService;
import ajou.ac.kr.teaming.service.login.MyPetService;
import ajou.ac.kr.teaming.vo.GpsLocationVo;
import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.PhotoVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.ServiceVO;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    private ArrayList<String> mArrayLineID;
    private static int mLineID;
    private int photoMarkerId;
    private ServiceVO ServiceVO;
    private String walkStatus;
    private TextView txtShowWalkStatus;
    private RegisterVO RegisterVO;
    private MyPetVO myPetVO;
    private String dogName;

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

        txtShowWalkStatus = (TextView) findViewById(R.id.txtWalkStatus);




        mArrayMarkerID = new ArrayList<String>();;
        photoMarkerId = 0;
        mArrayLineID  = new ArrayList<String>();
        mLineID = 0;


        Intent intent = getIntent();
        ServiceVO = (ServiceVO) intent.getSerializableExtra("ServiceVo");
        RegisterVO = (RegisterVO) intent.getSerializableExtra("RegisterVo");
        gpsId = ServiceVO.getGpsId();
        System.out.println(gpsId);
        System.out.println(RegisterVO);


        if(RegisterVO != null && ServiceVO != null) {
            traceStart();
        }

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
     */

    public void showMarkerPoint(int photoId, double photoLatitude, double photoLongitude) {
        TMapMarkerItem markerItem = new TMapMarkerItem();
        markerItem.setTMapPoint(new TMapPoint(photoLatitude, photoLongitude));
        markerItem.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
        tMapView.addMarkerItem("" + photoId, markerItem);
        String strID = String.format("%d", photoMarkerId++);
        mArrayMarkerID.add(strID);
    }
    /**
     * showMarkerPoint
     * 지도에 마커를 표출한다.
     *
     */



    /**
     * 도그워커의 이동경로를 그리는 메소드
     * */
    public void drawPedestrianPath() {
        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.YELLOW);
        tMapPolyLine.setLineWidth(2);
        for( int i = 0; i < alTMapPoint.size(); i++ ) {
            tMapPolyLine.addLinePoint( alTMapPoint.get(i) );
        }
        String strID = String.format("%d", mLineID++);
        tMapView.addTMapPolyLine(strID, tMapPolyLine);
        mArrayLineID.add(strID);
    }


    /**
     * 도그워커의 산책시간 및 이동거리를 표시해주는 메소드
     * */
    public void showDistanceAndTime(){
        String WalkContext = String.format("%.6s",walkDistance);
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

        getMyPetInfo();
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(GpsMainActivity.this);
        alert_confirm.setMessage("확인 버튼을 눌러 도그워커 추적을 시작합니다.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
//                        getGpsInfo();
//                        getLocationInfo();
//                        getMarkerInfo();
//                        drawPedestrianPath();
                        Toast.makeText(getApplicationContext(), "도그워커의 현재 위치를 찾는 중입니다.\n잠시 기다려 주세요.", Toast.LENGTH_LONG).show();
                        traceThread.start();
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


    //갱신을 위한 스레드
    Thread traceThread = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(5000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tMapView.setZoomLevel(19);
                            tMapView.setCenterPoint(dogwalkerLongitude, dogwalkerLatitude,true);


                            ImageView photoFromCameraImageView = findViewById(R.id.photoFromServer);
                            clickuserMethod(photoFromCameraImageView); //갱신시 기존의 띄워진 사진이 없어지게 한다.

                            alTMapPoint.clear();
                            mArrayLineID.clear();


                            if(alTMapPoint.size() == 0 && mArrayLineID.size() == 0) {
                                getGpsInfo();
                                getLocationInfo();
                                getMarkerInfo();
                                drawPedestrianPath();
                            }
                            traceEnd();
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };




    /**
     * Retrofit Method
     * */

    /**
     * GET GPS INFO
     * 도그워커의 산책정보(시작위치, 끝위치, 이동거리, 시작시간, 끝시간 등)을 서버로부터 받아온다.
     */
    public void getGpsInfo() {
        GpsService gpsService = ServiceBuilder.create(GpsService.class);
        Call<GpsVo> call = gpsService.doGetGpsInfo(gpsId);
        call.enqueue(new Callback<GpsVo>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<GpsVo> call, @NonNull Response<GpsVo> response) {
                GpsVo gpsGetVo = response.body();
                if(gpsGetVo != null){
                    Log.d("TEST", "onResponse getId: " + gpsGetVo.getId());
                    Log.d("TEST", "onResponse getStartDogwalkerLatitude: " + gpsGetVo.getStartDogwalkerLatitude());
                    Log.d("TEST", "onResponse getStartDogwalkerLongitude: " + gpsGetVo.getStartDogwalkerLongitude());
                    Log.d("TEST", "onResponse getEndDogwalkerLatitude: " + gpsGetVo.getEndDogwalkerLatitude());
                    Log.d("TEST", "onResponse getEndDogwalkerLongitude: " + gpsGetVo.getEndDogwalkerLongitude());
                    Log.d("TEST", "onResponse getWalkDistance: " + gpsGetVo.getWalkDistance());
                    Log.d("TEST", "onResponse getStart_time: " + gpsGetVo.getStart_time());
                    Log.d("TEST", "onResponse getEnd_time: " + gpsGetVo.getEnd_time());
                    Log.d("TEST", "onResponse getWalkTime: " + gpsGetVo.getWalkTime());
                    Log.d("TEST", "onResponse getWalkStatus: " + gpsGetVo.getWalkStatus());


                    /**서버로부터 받은 데이터를 저장*/
                    startDogwalkerLatitude = gpsGetVo.getStartDogwalkerLatitude();
                    startDogwalkerLongitude = gpsGetVo.getStartDogwalkerLongitude();
                    endDogwalkerLatitude = gpsGetVo.getEndDogwalkerLatitude();
                    endDogwalkerLongitude = gpsGetVo.getEndDogwalkerLongitude();
                    walkDistance = gpsGetVo.getWalkDistance();
                    start_time = gpsGetVo.getStart_time();
                    end_time = gpsGetVo.getEnd_time();
                    walkTime = gpsGetVo.getWalkTime();
                    walkStatus = gpsGetVo.getWalkStatus(); //산책 상태 추가

                    showDistanceAndTime();

                }
                txtShowWalkStatus.setText(walkStatus);
                Log.d("TEST", "도그워커 산책정보 받기 성공");
            }
            @Override
            public void onFailure(@NonNull Call<GpsVo> call, @NonNull Throwable t) {
       //         Toast.makeText(getApplicationContext(),"Retrofit 통신 실패 : 도그워커 산책 정보",Toast.LENGTH_SHORT).show();
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
        Call<List<PhotoVO>> call = gpsMarkerService.doGetMarkerInfo(gpsId);
        call.enqueue(new Callback <List<PhotoVO>>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<List<PhotoVO>> call, @NonNull Response<List<PhotoVO>> response) {
                List<PhotoVO> gpsPhotoVo = response.body();
                if(gpsPhotoVo != null){
                    //배열로 받아와야한다.
                    for(int i = 0; i < gpsPhotoVo.size(); i++){
                        int photoId = gpsPhotoVo.get(i).getMarkerId();
                        double photoLatitude = gpsPhotoVo.get(i).getPhotoLatitude();
                        double photoLongitude = gpsPhotoVo.get(i).getPhotoLongitude();

                        showMarkerPoint(photoId, photoLatitude, photoLongitude); //사진Id를 이용해 마커 생성
                    }

                }
                Log.d("TEST", "도그워커 사진정보 받기 성공");
            }
            @Override
            public void onFailure(@NonNull Call<List<PhotoVO>> call, @NonNull Throwable t) {
        //        Toast.makeText(getApplicationContext(),"Retrofit 통신 실패 : 사진 및 마커 정보",Toast.LENGTH_SHORT).show();
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
        Call<List<GpsLocationVo>> call = gpsDogwalkerLocationService.doGetLocationInfo(gpsId);
        call.enqueue(new Callback<List<GpsLocationVo>>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<List<GpsLocationVo>> call, @NonNull Response<List<GpsLocationVo>> response) {
                List<GpsLocationVo> gpsGetLocationVo = response.body();
                if(gpsGetLocationVo != null){
                    //배열로 받아와야한다.
                    for (int i = 0 ; i < gpsGetLocationVo.size() ; i++){

                        dogwalkerLatitude = gpsGetLocationVo.get(i).getDogwalkerLatitude();
                        dogwalkerLongitude = gpsGetLocationVo.get(i).getDogwalkerLongitude();

                        /**서버로부터 받은 데이터를 저장*/
                        alTMapPoint.add(new TMapPoint(dogwalkerLatitude, dogwalkerLongitude));
                    }
                }
                Log.d("TEST", "도그워커 현재위치 받기 성공");
                drawPedestrianPath();
                tMapView.setCenterPoint(dogwalkerLongitude, dogwalkerLatitude,true);
            }
            @Override
            public void onFailure(@NonNull Call<List<GpsLocationVo>> call, @NonNull Throwable t) {
       //         Toast.makeText(getApplicationContext(),"Retrofit 통신 실패 : 도그워커 현재위치 정보",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패 : 도그워커 현재위치 정보");
            }
        });
    }

    /**
     * GET MyPet INFO
     * 나의 펫 정보를 가져온다
     * 이름을 가져오기 위함
     */
    public void getMyPetInfo() {
        MyPetService myPetService = ServiceBuilder.create(MyPetService.class);

        String userId = RegisterVO.getUserID();

        Call<List<MyPetVO>> call = myPetService.getMyPetInfo(userId);
        call.enqueue(new Callback<List<MyPetVO>>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<List<MyPetVO>> call, @NonNull Response<List<MyPetVO>> response) {
                List<MyPetVO> myPetVO = response.body();
                for (int i = 0 ; i < myPetVO.size() ; i++){

                    dogName = myPetVO.get(i).getDog_name();
                    /**서버로부터 받은 데이터를 저장*/

                }
                Log.d("TEST", "나의 펫 이름 받기 성공");
            }
            @Override
            public void onFailure(@NonNull Call<List<MyPetVO>> call, @NonNull Throwable t) {
       //         Toast.makeText(getApplicationContext(),"Retrofit 통신 실패 : 나의 펫 이름 받기",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패 : 나의 펫 이름 받기");
            }
        });
    }



    public void traceEnd () { //도그워커 추적 종료 메소드
        if(walkStatus == null || walkStatus.equals("산책 중")){ //산책 상태가
          return;

        } else if (walkStatus.equals("산책 종료")) { //산책 종료가 된다면
            traceThread.interrupt();
            txtShowWalkStatus.setText(walkStatus);


            String WalkDistanceContext = String.format("%.6s",walkDistance);

            Long showWalkTime = Long.parseLong(walkTime);
            Date walkdate = new Date(showWalkTime);
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
            // nowDate 변수에 값을 저장한다.
            String formatDate = sdfNow.format(walkdate);

            new AlertDialog.Builder(GpsMainActivity.this)
                    .setTitle("산책 종료")
                    .setMessage("도그워커가 산책을 완료하였습니다.\n\n" +
                            "서비스 ID : " + ServiceVO.getId() +
                            "\n강아지 이름 : " + dogName +
                            //TODO : long 타입의 시작시간과 끝시간, walkTime, 걸은 거리를 보기 좋게 바꾸기
                        //    "\n시작 시각 : " + start_time + //TODO 값 전달이 안됨
                        //   "\n종료 시각 : " + end_time + //TODO 값 전달이 안됨
                            "\n총 산책 시각 : " + formatDate +
                            "\n산책 거리 : " + WalkDistanceContext + "m")
                    .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show(); // 팝업창 보여줌
        }
    }


}//GpsMainActivity

