package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.LogManager;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.gps.GpsService;
import ajou.ac.kr.teaming.vo.GpsVo;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;*/

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
 * 4. 위치 정보 받기
 * 5. 사진 정보 기록 (각 단말의 기본 앱 사용)
 * 6. 산책거리 계산 및 반영
 * 7. 산책시간 계산 및 반영 (타이머?)
 * 8. 산책 종료시 코멘트 화면 이동
 *
 * ----------------------------------------------------------------------------
 *
 * 도그워커의 이동경로는 도그워커 좌표의 지속적 변경을 계속 기록하고 선을 이어 경로를 생성
 * 이를 응용해 거리를 계산하고, 이 데이터를 서버로 전송
 *
 * 사진찍기 시, 핸드폰 기본앱(카메라)를 통해 사진을 찍음.
 * 이를 도그워커 앱으로 가져와서 업로드.
 * 그 이후 이 사진 정보또한 서버로 전송
 *
 * 사용자는 서버로부터 위의 데이터를 받고 지도에 표시한다.
 *
 * -------------------------------------------------------
 *
 * gpsId
 * markerId
 * photoLatitude
 * photoLongitude 설정해야함
 *
 *
 * */


public class DogwalkerGpsActivity extends AppCompatActivity{

    private static final String TAG = "DogwalkerGpsActivity";
    private GpsService gpsService = ServiceBuilder.create(GpsService.class);

    /*********Field Part***********/

    /***
     * 버튼 아이디 정리
     */
    private static final int[] mArrayMapDogwalkerButton = {
//            R.id.btnWalkStart,
//            R.id.btnPhotoAndMarker, //사진찍기 및 마커생성
//            R.id.btnWalkDistance, // 산책거리계산 및 표시
//            R.id.btnWalkEnd, //산책 종료
/*            R.id.btnShowLocation,*/
//            R.id.btnPostDogwalkerLocation,
            R.id.btnImageUploadSample, // 파일 업로드 샘플
    };


    /**텍스트뷰 **/
    private TextView txtLat;
    private TextView txtLon;
    private TextView txtShowWalkDistance;
    private TextView txtWalkTime;
    private TextView txtCurrentTime;
    private TextView txtStartTime;
    private TextView txtgongback;
    private TextView txtgongback2;
    private TextView txtWalkDistance;


    private ImageView iconCompass;


    private final String TMAP_API_KEY = "78f4044b-3ca4-439d-8d0e-10135941f054";  //Tmap 인증키
    private Context mContext;
    private TMapView tMapView = null;

    TMapGpsManager tMapGps = null;
    PermissionManager permissionManager = null; // 권한요청 관리자
    Fragment currentFragment;


    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;


    private boolean isCompassmode = false;
    private boolean m_bSightVisible = false;
    private boolean m_bTrackingMode = false;
    private boolean m_bOverlayMode = false;
    private int m_nCurrentZoomLevel = 0;
    private ArrayList<Bitmap> mOverlayList;



    private static int 	mMarkerID;

    ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();

    // GPSTracker class
    private TMapGpsManager gps;

    private long currentTime;
    private long nine = 32400000;



    public static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;

   private static final int REQUEST_IMAGE_CAPTURE = 672;
   private String imageFilePath;
   private Uri photoUri;
   private ImageView resultImage;
   private double recentWalkDistance;



   private int gpsId;
   private int markerId;
   private byte[] photoData;
   private double photoLatitude;
   private double photoLongitude;
   private double dogwalkerLatitude;
   private double dogwalkerLongitude;
   private double startDogwalkerLatitude;
   private double startDogwalkerLongitude;
   private double endDogwalkerLatitude;
   private double endDogwalkerLongitude;
   private double walkDistance;
   private long start_time;
   private long end_time;
   private long walkTime;


   private boolean isWalkStatus;
   private int markTime = 0;
   private ImageView iconPhoto;
   private ImageView iconWalkEnd;
   private boolean isSetGps = false;
   private ImageView bubblePicture;
   private Bitmap bitmapSample1;




   ArrayList<String> mArrayMarkerID;





/*****************************************************************************/

    /**
     * setSKTMapApiKey()에 ApiKey를 입력 한다.
     */
    private void apiKeyMapView() {
        tMapView.setSKTMapApiKey(TMAP_API_KEY);
    }

/*    *//**
     * 권한 요청 관리자*//*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        final int SUCCESS = 200;
        if (requestCode != SUCCESS) {
            Log.e(TAG, "fail");
        }

        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "denied");
        }
    }*/


    //갱신을 위한 스레드
    Thread walkingThread = new Thread() {
        @Override
        public void run() {
            start_time = System.currentTimeMillis();
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateCurrentTime();
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };




    ///////////////////
    private void updateCurrentTime() {

        currentTime = System.currentTimeMillis();
        walkTime = currentTime - start_time - nine;
        Date walkdate = new Date(walkTime);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(walkdate);

        txtWalkTime = (TextView) findViewById(R.id.txtWalkTime);
        txtWalkTime.setText(formatDate.substring(0,8));    // TextView 에 현재 시간 문자열 할당

/*

        Date date = new Date(currentTime);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow1 = new SimpleDateFormat("HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate1 = sdfNow1.format(date);

        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        txtCurrentTime.setText(formatDate1.substring(0,8));    // TextView 에 현재 시간 문자열 할당

        Date startDate = new Date(start_time);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow2 = new SimpleDateFormat("HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate2 = sdfNow2.format(startDate);

        txtStartTime = (TextView) findViewById(R.id.txtStartTime);
        txtStartTime.setText(formatDate2.substring(0,8));    // TextView 에 현재 시간 문자열 할당
*/

    }




    /**
     * onCreate
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dogwalker_gps);

        mContext = this;
        permissionManager = new PermissionManager(this); // 권한요청 관리자

        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLon = (TextView) findViewById(R.id.txtLon);
        txtgongback = (TextView) findViewById(R.id.txtgongback2);

        iconCompass = (ImageView) findViewById(R.id.compassIcon);
        iconCompass.bringToFront() ;
        iconCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCompassmode != true) {
                    isCompassmode = true;
                    tMapView.setCompassMode(true);
                    Toast.makeText(getApplicationContext(), "나침반 모드를 실행합니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    isCompassmode = false;
                    tMapView.setCompassMode(false);
                    Toast.makeText(getApplicationContext(), "나침반 모드를 종료합니다.", Toast.LENGTH_LONG).show();
                }
            }
        });


        iconPhoto = (ImageView) findViewById(R.id.photoIcon);
        iconPhoto.bringToFront() ;
        iconPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPhotoAndMarker();
            }
        });

        iconWalkEnd = (ImageView) findViewById(R.id.walkEndIcon);
        iconWalkEnd.bringToFront() ;
        iconWalkEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkEnd();
            }
        });




        LinearLayout linearLayoutDogwalkerTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        apiKeyMapView(); //T MAP API 서버키 인증
        linearLayoutDogwalkerTmap.addView(tMapView);


        initView(); //리스너 실행
        tMapView.setTrackingMode(true);
        tMapView.setZoomLevel(15);
        tMapView.setIconVisibility(true);
        Toast.makeText(getApplicationContext(), "현재 위치를 찾는 중입니다.\n잠시 기다려 주세요.", Toast.LENGTH_LONG).show();

        gps = new TMapGpsManager(DogwalkerGpsActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
        setGps();

        tMapGps = new TMapGpsManager(DogwalkerGpsActivity.this);
        tMapGps.setMinTime(1000);
        tMapGps.setMinDistance(5);
        tMapGps.setProvider(tMapGps.GPS_PROVIDER);//gps를 이용해 현 위치를 잡는다.
        tMapGps.OpenGps();

//        tMapGps.setProvider(tMapGps.NETWORK_PROVIDER);//연결된 인터넷으로 현 위치를 잡는다.
//        tMapGps.OpenGps();

        if(isSetGps =! false){
            walkStart();
        }


        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.e(TAG, "Permission Granted");
                Toast.makeText(DogwalkerGpsActivity.this, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Log.e(TAG, "Permission Denied");
                Toast.makeText(DogwalkerGpsActivity.this,"권한이 거부되었습니다."+ deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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




        isWalkStatus = false;

    }//onCreate


    /*

    /**
     * 현재 위치정보 받기
     * LocationListener와 setGps를 통해 현재위치를 gps를 통해 받아온다.
     * */
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            isSetGps = true;

            dogwalkerLatitude = location.getLatitude();
            dogwalkerLongitude = location.getLongitude();
            //위도 경도 텍스트뷰에 보여주기
            txtLat.setText(String.valueOf(dogwalkerLatitude));
            txtLon.setText(String.valueOf(dogwalkerLongitude));


            //위치정보 모니터링 제거
            //locationManager.removeUpdates(DogwalkerGpsActivity.this);

            //도그워커 이동경로 그리기
            //위치가 바뀔 때마다 현재 위치에 새로운 포인트를 추가

            if( isWalkStatus == true){
                //위치가 바뀔때마다 다음 역할을 수행
                calculateWalkDistance();
                addPedestrianPoint();
                drawPedestrianPath();
            }



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

        Log.e(TAG,"setGps Activated");
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                1000 * 5, // 통지사이의 최소 시간간격 (miliSecond)
                10, // 통지사이의 최소 변경거리 (m)
                mLocationListener);

//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
//                1000 * 5, // 통지사이의 최소 시간간격 (miliSecond)
//                10, // 통지사이의 최소 변경거리 (m)
//                mLocationListener);
    }





    /**
     * 버튼 리스너 등록
     * */
    private void initView() {
        for (int btnMapView : mArrayMapDogwalkerButton) {
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
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
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
            public void onLongPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point) {
                LogManager.printLog("MainActivity onLongPressEvent " + markerlist.size());
            }
        });

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                String strMessage = "";
                strMessage = "ID: " + markerItem.getID() + " " + "Title " + markerItem.getCalloutTitle();
                Common.showAlertDialog(DogwalkerGpsActivity.this, "Callout Right Button", strMessage);
            }
        });
//
//        tMapView.setOnClickReverseLabelListener(new TMapView.OnClickReverseLabelListenerCallback() {
//            @Override
//            public void onClickReverseLabelEvent(TMapLabelInfo findReverseLabel) {
//                if(findReverseLabel != null) {
//                    LogManager.printLog("MainActivity setOnClickReverseLabelListener " + findReverseLabel.id + " / " + findReverseLabel.labelLat
//                            + " / " + findReverseLabel.labelLon + " / " + findReverseLabel.labelName);
//
//                }
//            }
//        });

        m_nCurrentZoomLevel = -1;
        isCompassmode = false;
        m_bSightVisible = false;
        m_bTrackingMode = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
//            case R.id.btnWalkStart                       :     walkStart();                     break;
//            case R.id.btnCallToUser		                  : 	callToUser(); 			        break;
//            case R.id.btnChatToUser		                  : 	chatToUser(); 			        break;
//            case R.id.btnPhotoAndMarker	               	  : 	alertPhotoAndMarker(); 			break;
//            case R.id.btnWalkEnd		                  : 	walkEnd(); 			            break;
//            case R.id.btnPostDogwalkerLocation           :    postDogwalkerLocation();          break;
            case R.id.btnImageUploadSample               : 	imageUploadSample(); 			    break;
        }
    }

    private void walkStart() {

        /**산책 시작 시 동작해야할 것
         * 1. 산책시간 측정
         * 2. 자신의 위치 정보 반환
         * 3. 위치 이동을 통해 보행자 길 표시
         * 4. 서버 전송
         * 5. 사진찍기 및 마커생성 동작 가능
         * */

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DogwalkerGpsActivity.this);
        alert_confirm.setMessage("확인 버튼을 눌러 산책을 시작합니다.\n※ GPS 신호 수신이 불량한 지역에서는 위치 오류가 발생할 수 있으며, 거리 계산이 정확하지 않을 수 있습니다.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        if(isWalkStatus == false) {
                            isWalkStatus = true;
                            walkingThread.start();
                            startDogwalkerLatitude = dogwalkerLatitude;
                            startDogwalkerLongitude = dogwalkerLongitude;

                            alTMapPoint.add( new TMapPoint(startDogwalkerLatitude, startDogwalkerLongitude) ); // 도그워커 출발지점
                            alTMapPoint.add( new TMapPoint(dogwalkerLatitude, dogwalkerLongitude) ); // 도그워커 끝지점


                        } else {
                            Log.e(TAG,"WalkStatus Error");
                        }
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


    /********** onClickEvent Method **************/
 /*   private void showLocation() {
            gps = new DogWalkerGpsInfo(DogwalkerGpsActivity.this);
            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                txtLat.setText(String.valueOf(latitude));
                txtLon.setText(String.valueOf(longitude));

                Toast.makeText(
                        getApplicationContext(),
                        "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                        Toast.LENGTH_LONG).show();
            } else {
                // GPS 를 사용할수 없으므로
                gps.showSettingsAlert();
            }
    }*/

    /*
    private void doSetDogwalkerLocation() {
        double 	dogwalkerLatitude  = 37.5077664;
        double dogwalkerLongitude = 126.8805826;

        LogManager.printLog("setLocationPoint " + dogwalkerLatitude + " " + dogwalkerLongitude);
        tMapView.setLocationPoint(dogwalkerLongitude, dogwalkerLatitude);
        String strResult = String.format("현재위치의 좌표의 위도 경도를 설정\n dogwalkerLatitude = %f dogwalkerLongitude = %f", dogwalkerLatitude, dogwalkerLongitude);
        Common.showAlertDialog(this, "", strResult);
    }

    */
    /**
     * 강아지 주인과 통화하기
     * */
//    private void callToUser() {
//
//        //통화하기 샘플
//        Uri phoneNumber = Uri.parse("tel:01057461715");
//        Intent callMe = new Intent(Intent.ACTION_VIEW, phoneNumber);
//        startActivity(callMe);
//
//    }

//    /**
//     * 강아지 주인과 채팅하기
//     * */
//    private void chatToUser(){
//    }

    /**
     * 사진 찍기 시 마커 생성
     * */
    private void alertPhotoAndMarker() {
        /**산책 종료 확인 창*/
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DogwalkerGpsActivity.this);
        alert_confirm.setMessage("사진을 촬영하시겠습니까?\n사진 촬영 시 현재 위치에 마커가 생성됩니다.").setCancelable(false).
                setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 'YES'
                makePhotoAndMarker();
            }
        }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }


    /**
     * 카메라를 통해 이미지 가져옴
     * */
    public void makePhotoAndMarker(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) { }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    /**
     * 사진 촬영 이후 실행되는 메소드
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(imageFilePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {

                    ExifInterface ei = new ExifInterface(imageFilePath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    int degree = exifOrientationToDegress(orientation);

                    Bitmap rotatedBitmap = rotate(bitmap, degree);

                    ((ImageView)findViewById(R.id.compassIcon)).setImageBitmap(rotatedBitmap);
                    showMarkerPoint(); //사진찍은 위치에 마커생성
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
           /* Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);
            } else {
                exifDegree = 0;
            }

            //사진 표시 방법을 생각
            //resultImage.setImageBitmap(rotate(bitmap,exifDegree));
            showMarkerPoint(); //사진찍은 위치에 마커생성
           // imageUploadSample(); //이미지 업로드 메소드*/
        }
    }
    /**
     * 카메라 사진 회전 메소드
     * */
    private int exifOrientationToDegress(int exifOrientation) {
        if  (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap bitmap, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }


    public void showMarkerPoint() {

        for(int i = 0; i < 20; i++){

            Log.d("TEST","마커생성");
            //도그워커의 현재위치에 마커 생성
            TMapPoint markerPoint = new TMapPoint(dogwalkerLatitude, dogwalkerLongitude);
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            String strID = String.format("%02d", i);
            // 마커 아이콘 지정
            markerItem1.getTMapPoint();
            markerItem1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
            // 마커의 좌표 지정
            markerItem1.setTMapPoint(markerPoint);
            markerItem1.setCanShowCallout(true);
            markerItem1.setCalloutTitle("테스트"+i);
            //지도에 마커 추가
            tMapView.addMarkerItem(strID+i, markerItem1);

            //풍선뷰 왼쪽에 사용될 이미지
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.Icon);
//            tItem.setCalloutLeftImage(bitmap);
        }
/*
        ArrayList<Bitmap> markerList = null;
        for(int i = 0; i < 20; i++) {

            //마커 객체 생성
            MarkerOverlay marker1 = new MarkerOverlay(DogwalkerGpsActivity.this, tMapView);
            String strID = String.format("%02d", i);

            //	String MarkerID = marker1.getID();  마커 아이디 반환
            // 	marker1.setID("id"); //마커 아이디 설정
            //   마커의 애니메이션 이미지 설정
            //   ArrayList list = null;
            //   list.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_red));
            //   list.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.end));
            //   tMapMarkerItem2.setAnimationIcons(list);

            //마커의 애니메이션 실행
            //tMapMarkerItem2.startAnimation();

            //마커의 중심좌표 설정
            marker1.getTMapPoint();
            marker1.setID(strID);
            //마커이미지 설정
            marker1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
            //마커위치 설정
            marker1.setTMapPoint(markerPoint);

            if (markerList == null) {
                markerList = new ArrayList<Bitmap>();
            }
            markerList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_red));
            markerList.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.end));

            //지도에 마커 추가
            tMapView.addMarkerItem2(strID, marker1);

            markerId = Integer.parseInt(strID); //마커아이디 저장
            //마커생성위치가 곧 사진찍은 위치가 된다.
            photoLatitude = dogwalkerLatitude;
            photoLongitude = dogwalkerLongitude;
        }*/



        markTime += 1;

        tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {
            @Override
            public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
                LogManager.printLog("ClickEvent " + " id " + id + " \n" + markerItem2.latitude + " " +  markerItem2.longitude);
                String strMessage = "ClickEvent " + " id " + id + " \n" + markerItem2.latitude + " " +  markerItem2.longitude;
                Common.showAlertDialog(DogwalkerGpsActivity.this, "사진 상세정보", strMessage);
            }
        });
    }


    /** * image url을 받아서 bitmap을 생성하고 리턴한다.
     * @param url 얻고자 하는 image url
     * @return 생성된 bitmap */
    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        Bitmap retBitmap = null;
        try {
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true); //url로 input받는 flag 허용
            connection.connect(); //연결
            is = connection.getInputStream(); // getinputstream
            retBitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }




    /**
    * 보행자의 이동경로 메소드
     * */
    public void drawPedestrianPath() {

    //  double startWalkLatitude = startDogwalkerLatitude;
    //  double startWalkLongitude = startDogwalkerLongitude;

//        alTMapPoint.add( new TMapPoint(startDogwalkerLatitude, startDogwalkerLongitude) ); // 도그워커 출발지점
//        alTMapPoint.add( new TMapPoint(dogwalkerLatitude, dogwalkerLongitude) ); // 도그워커 끝지점

        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.YELLOW);
        tMapPolyLine.setLineWidth(2);
        for( int i=0; i<alTMapPoint.size(); i++ ) {
            tMapPolyLine.addLinePoint( alTMapPoint.get(i) );
        }
        tMapView.addTMapPolyLine("Line1", tMapPolyLine);


//        double startWalkLatitude = startDogwalkerLatitude;
//        double startWalkLongitude = startDogwalkerLongitude;
//
//        TMapPoint startPoint = new TMapPoint(startWalkLatitude,startWalkLongitude);
//        TMapPoint currentPoint = new TMapPoint(dogwalkerLatitude,dogwalkerLongitude);
//
//        TMapData tmapdata = new TMapData();
//
//        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, currentPoint, new TMapData.FindPathDataListenerCallback() {
//            @Override
//            public void onFindPathData(TMapPolyLine polyLine) {
//                polyLine.setLineColor(Color.GREEN);
//                tMapView.addTMapPath(polyLine);
//            }
//        });

    }


    /**
     * 위치가 바뀔때마다 (onLocationChanged)
     * 현재위치에 새로운 포인트를 추가
     * */
    public void addPedestrianPoint(){
        alTMapPoint.add( new TMapPoint(dogwalkerLatitude, dogwalkerLongitude) );
    }



    /**
     * 도그워커의 이동거리 계산
     * */
    private void calculateWalkDistance() {
        //도그워커의 시작위치는 고정, 도그워커의 현재 위치는 계속 바뀜.
        // 킬로미터(Kilo Meter) 단위

        double distanceMeter = distance(startDogwalkerLatitude, startDogwalkerLongitude, dogwalkerLatitude, dogwalkerLongitude, "meter");
        recentWalkDistance = recentWalkDistance + distanceMeter;
        txtWalkDistance = (TextView) findViewById(R.id.txtWalkDistance);
        String WalkContext = String.format("%.2f",recentWalkDistance);
        txtWalkDistance.setText( WalkContext + "m" );

}


    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     * @return dist
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
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
     * 도그워커 산책 종료
     **/
    private void walkEnd() {
        if(isWalkStatus = true){
            isWalkStatus = false;
            /**산책 종료 확인 창*/
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DogwalkerGpsActivity.this);
            alert_confirm.setMessage("산책을 종료 하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 'YES'
                    walkingThread.interrupt(); //스레드 종료
                    isWalkStatus = false;

                    //산책이 끝난 현위치를 마지막 위치로 설정
                    endDogwalkerLatitude = dogwalkerLatitude;
                    endDogwalkerLongitude = dogwalkerLongitude;

                    //산책이 끝날 때 까지 누적된 거리를 저장;
                    walkDistance = recentWalkDistance;
                    //산책이 끝난 시점의 시간을 end_time에 저장
                    end_time = currentTime;


                    Long totalWalkTime = walkTime;


                    Intent intent = new Intent(DogwalkerGpsActivity.this, DogwalkerGpsResult.class);

                    intent.putExtra("totalWalkDistance",walkDistance); /*송신*/
                    intent.putExtra("totalWalkTime",totalWalkTime);
                    intent.putExtra("PhotoTImes",markTime);
                    startActivity(intent);
                }
            }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 'No'
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }else {
            Toast.makeText(getApplicationContext(),"산책 중이 아닙니다..",Toast.LENGTH_LONG).show();
        }



    }//walkEnd();

    /**
     * Retrofit Method
     * 아래부터는 Retrofit이 들어가는 메소드만을 모아두었다.
     *
     * ************/
    /**
     *
     * POST DOGWALKER LOCATION
     * 도그워커의 현재 위치를 서버로 전송한다.
     * */
/*
    private void postDogwalkerLocation() {
        GpsService gpsService = ServiceBuilder.create(GpsService.class);
        */
/**
         * 통신 테스트를 위해 임의의 값을 넣어봄.
         * **//*

        HashMap<String, Object> inputObjectData = new HashMap<>();
        inputObjectData.put("gpsId", 1);
        inputObjectData.put("markerId", 1);
        inputObjectData.put("photoData", "사진");
        inputObjectData.put("photoLatitude", 37.2744762);
        inputObjectData.put("photoLongitude", 127.0342091);
        inputObjectData.put("dogwalkerLatitude", 37.2844762);
        inputObjectData.put("dogwalkerLongitude", 127.0442091);
        inputObjectData.put("startDogwalkerLatitude", 37.2844762);
        inputObjectData.put("startDogwalkerLongitude", 127.0442091);
        inputObjectData.put("endDogwalkerLatitude", 38.2844762);
        inputObjectData.put("endDogwalkerLongitude", 126.0442091);
      inputObjectData.put("calculateWalkDistance", null);
        inputObjectData.put("start_time", null);
        inputObjectData.put("end_time", null);
        inputObjectData.put("walkTime", null);



        Call<GpsVo> call = gpsService.postObjectData(inputObjectData);
        call.enqueue(new Callback<GpsVo>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<GpsVo> call, @NonNull Response<GpsVo> response) {
                GpsVo GpsVos = response.body();
                Log.d("TEST", "onResponseBODY: " + response.body());
                if(GpsVos != null){
                }
                Log.d("TEST", "onResponse:END ");
            }
            @Override
            public void onFailure(@NonNull Call<GpsVo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패\n위치를 전달할 수 없습니다.",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패");
            }
        });

    }
*/



    /**
     * Retrofit Method
     * 안드로이드 파일 업로드 샘플
     *
     * 일단 POST는 잘 되는거 같음. 굿
     *
     * 사진 찍기 이후 사진을 일단 서버에 업로드
     * 마커ID, 사진파일, 사진찍은 위치를 서버에 POST
     *
     */
    private void imageUploadSample() {
        ImageView imageView = findViewById(R.id.compassIcon);

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataArray = baos.toByteArray();

        //RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), data);
        //MultipartBody.Part fileBody = MultipartBody.Part.createFormData("picture", "aa.jpg", requestFile);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), dataArray);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("fileUpload\"; filename=\"photo.png", fileBody); //photoData
        params.put("markerId", RequestBody.create(MediaType.parse("text"),String.valueOf(markerId)));
        params.put("photoLatitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(photoLatitude))));
        params.put("photoLongitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(photoLatitude))));
//        params.put("dogwalkerLatitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(dogwalkerLatitude))));
//        params.put("dogwalkerLongitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(dogwalkerLongitude))));
//        params.put("startDogwalkerLatitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(startDogwalkerLatitude))));
//        params.put("startDogwalkerLongitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(startDogwalkerLongitude))));
//        params.put("endDogwalkerLatitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(endDogwalkerLatitude))));
//        params.put("endDogwalkerLongitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(endDogwalkerLongitude))));
//        params.put("walkDistance", RequestBody.create(MediaType.parse("text"), (String.valueOf(walkDistance))));
//        params.put("start_time", RequestBody.create(MediaType.parse("text"), (String.valueOf(start_time))));
//        params.put("end_time", RequestBody.create(MediaType.parse("text"), (String.valueOf(end_time))));
//        params.put("walkTime", RequestBody.create(MediaType.parse("text"), (String.valueOf(walkTime))));

        Call<GpsVo> call = gpsService.postObjectData(params);
        call.enqueue(new Callback<GpsVo>() {
            @Override
            public void onResponse(Call<GpsVo> call, Response<GpsVo> response) {
                if (response.isSuccessful()) {
                    GpsVo gpsVo = response.body();
                    if (gpsVo!= null) {
                        Log.d("TEST", "통신 성공");
                       /* Log.d("TEST", "" + gpsVo.getId());
                        Log.d("TEST", "" + gpsVo.getGpsId());*/
                        Log.d("TEST", "" + gpsVo.getMarkerId());
                        Log.d("TEST", "" + gpsVo.getPhotoURL());
                        Log.d("TEST", "" + gpsVo.getPhotoLatitude());
                        Log.d("TEST", "" + gpsVo.getPhotoLongitude());
                       /* Log.d("TEST", "" + gpsVo.getDogwalkerLatitude());
                        Log.d("TEST", "" + gpsVo.getDogwalkerLongitude());
                        Log.d("TEST", "" + gpsVo.getStartDogwalkerLatitude());
                        Log.d("TEST", "" + gpsVo.getStartDogwalkerLongitude());
                        Log.d("TEST", "" + gpsVo.getEndDogwalkerLatitude());
                        Log.d("TEST", "" + gpsVo.getEndDogwalkerLongitude());
                        Log.d("TEST", "" + gpsVo.getWalkDistance());
                        Log.d("TEST", "" + gpsVo.getStart_time());
                        Log.d("TEST", "" + gpsVo.getEnd_time());
                        Log.d("TEST", "" + gpsVo.getWalkTime());*/



                        /**
                         * 풍선뷰 안의 이미지를 서버의 주소로 받아와 생성한다.
                         * */
                        bubblePicture = (ImageView) findViewById(R.id.bubble_pictureContext);
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    bitmapSample1 = getBitmap("http://52.79.234.182:3000/gps/marker/image?markerId="+markerId);
                                } catch (Exception e) {
                                } finally {
                                    if (bitmapSample1 != null) {
                                        runOnUiThread(new Runnable() {
                                            @SuppressLint("NewApi")
                                            public void run() {
                                                bubblePicture.setImageBitmap(bitmapSample1);
                                            }
                                        });
                                    }
                                }
                            }
                        }).start();
                    }
                }
            }
            @Override
            public void onFailure(Call<GpsVo> call, Throwable t) {
                Log.d("TEST", "통신 실패");
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }//imageUploadSample


}//DogwalkerGpsActivity