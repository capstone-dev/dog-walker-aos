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
import android.graphics.Matrix;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.LogManager;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.gps.GpsDogwalkerLocationService;
import ajou.ac.kr.teaming.service.gps.GpsMarkerService;
import ajou.ac.kr.teaming.service.gps.GpsService;
import ajou.ac.kr.teaming.vo.GpsLocationVo;
import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.PhotoVO;
import ajou.ac.kr.teaming.vo.ServiceVO;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
//            R.id.btnImageUploadSample, // 파일 업로드 샘플
    };


    /**텍스트뷰 **/
    private TextView txtLat;
    private TextView txtLon;
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


    private boolean isCompassmode = false;
    private boolean m_bSightVisible = false;
    private boolean m_bTrackingMode = false;
    private boolean m_bOverlayMode = false;
    private int m_nCurrentZoomLevel = 0;
    private ArrayList<Bitmap> mOverlayList;


    ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();
    ArrayList<TMapPoint> dogwalkerPhotoPoint = new ArrayList<TMapPoint>();

    // GPSTracker class
    private TMapGpsManager gps;

    private long currentTime;
    private long nine = 32400000;


   private static final int REQUEST_IMAGE_CAPTURE = 672;
   private String imageFilePath;
   private Uri photoUri;
   private ImageView resultImage;
   private double recentWalkDistance;

   private int gpsId;

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
   private Bitmap bitmapSample1;
   private double currentDogwalkerLatitude;
   private double currentDogwalkerLongitude;
   private ImageView imageUploadTest;
   private Bitmap resizedImage;
    private Bitmap captureMapImage;
    private BitmapFactory.Options options;
    private byte[] captureMapDataArray;
    private long dataWalkTime;
    private ServiceVO ServiceVO;
    private int serviceId;


/*****************************************************************************/

    /**
     * setSKTMapApiKey()에 ApiKey를 입력 한다.
     */
    private void apiKeyMapView() {
        tMapView.setSKTMapApiKey(TMAP_API_KEY);
    }


    //갱신을 위한 스레드
    Thread walkTimeThread = new Thread() {
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
        txtgongback = (TextView) findViewById(R.id.txtgongback);
        txtgongback2 = (TextView) findViewById(R.id.txtgongback2);


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


        //ImageUpload 테스트 이미지뷰
        imageUploadTest = (ImageView) findViewById(R.id.imageUploadTestView);
        imageUploadTest.bringToFront() ;
        
        LinearLayout linearLayoutDogwalkerTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        apiKeyMapView(); //T MAP API 서버키 인증
        linearLayoutDogwalkerTmap.addView(tMapView);

        initView(); //리스너 실행
        tMapView.setTrackingMode(true);
        tMapView.setZoomLevel(17);
        tMapView.setIconVisibility(true);
        Toast.makeText(getApplicationContext(), "현재 위치를 찾는 중입니다.\n잠시 기다려 주세요.", Toast.LENGTH_LONG).show();

//        gps = new TMapGpsManager(DogwalkerGpsActivity.this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
//            }
//            return;
//        }
        setGps();


        Intent intent = getIntent();
        ServiceVO = (ServiceVO) intent.getSerializableExtra("ServiceVo");
        serviceId = ServiceVO.getId();

        System.out.println(serviceId);


        tMapGps = new TMapGpsManager(DogwalkerGpsActivity.this);
        tMapGps.setMinDistance(10);
        tMapGps.setProvider(tMapGps.GPS_PROVIDER);//gps를 이용해 현 위치를 잡는다.
        tMapGps.OpenGps();
        tMapGps.setProvider(tMapGps.NETWORK_PROVIDER);//연결된 인터넷으로 현 위치를 잡는다.
        tMapGps.OpenGps();

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
                                Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

        /**
         * 퍼미션이 허용되거나 거부되었을 때 하는 액션 메소드
         * */

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

            walkDistance = recentWalkDistance;
            dataWalkTime = walkTime;

            if( isWalkStatus == true){
                //위치가 바뀔때마다 다음 역할을 수행
                putGpsData();
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
                1000 * 10, // 통지사이의 최소 시간간격 (miliSecond)
                10, // 통지사이의 최소 변경거리 (m)
                mLocationListener);

//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
//                1000 * 10, // 통지사이의 최소 시간간격 (miliSecond)
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
                if (!markerlist.isEmpty()) {
                    TMapMarkerItem item = markerlist.get(markerlist.size() - 1);
                    ImageView photoFromCameraImageView = findViewById(R.id.photoFromCamera);
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
                Common.showAlertDialog(DogwalkerGpsActivity.this, "Callout Right Button", strMessage);
            }
        });

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
//            case R.id.btnWalkStart                     :     walkStart();                     break;
//            case R.id.btnCallToUser		              : 	callToUser(); 			        break;
//            case R.id.btnChatToUser		              : 	chatToUser(); 			        break;
//            case R.id.btnPhotoAndMarker	              : 	alertPhotoAndMarker(); 			break;
//            case R.id.btnWalkEnd		                  : 	walkEnd(); 			            break;
//            case R.id.btnPostDogwalkerLocation              postDogwalkerLocation();          break;
//            case R.id.btnImageUploadSample               : 	imageUploadSample();   			    break; //postGpsData();
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
                            walkTimeThread.start(); //시간을 재는 스레드 시작

                            //현재 위치가 곧 산책 시작 위치가 됨.
                            startDogwalkerLatitude = dogwalkerLatitude;
                            startDogwalkerLongitude = dogwalkerLongitude;

                            currentDogwalkerLatitude = dogwalkerLatitude;
                            currentDogwalkerLongitude = dogwalkerLongitude;

                            //이동경로를 표시하기 위해, 현재 위치를 ArrayList alTmapPoint에 추가
                            alTMapPoint.add( new TMapPoint(startDogwalkerLatitude, startDogwalkerLongitude) ); // 도그워커 출발지점
                            alTMapPoint.add( new TMapPoint(dogwalkerLatitude, dogwalkerLongitude) ); // 도그워커 끝지점

                            postGpsData();

                            isWalkStatus = true;
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
                openCameraView();
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
    public void openCameraView(){

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
     * 마커에 포함된 사진 hidden
     * @param view
     */
    public void clickMethod(View view) {
        view.setVisibility(View.GONE);
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

//                    ((ImageView)findViewById(R.id.imageUploadTestView)).setImageBitmap(rotatedBitmap);
                    photoLatitude = dogwalkerLatitude;
                    photoLongitude = dogwalkerLongitude;


                    imageUploadSample(rotatedBitmap); //사진찍은 현재위치의 ArrayList를 기준으로 이미지를 서버에 POST
                    //savePhotoLocationPoint(); //사진찍은 현재위치를 ArrayList에 추가
                    //showMarkerPoint(); //사진찍은 위치에 마커생성
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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




    public void showMarkerPoint(int photoId) {
        TMapMarkerItem markerItem = new TMapMarkerItem();
        markerItem.setTMapPoint(new TMapPoint(dogwalkerLatitude, dogwalkerLongitude));
        markerItem.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
        tMapView.addMarkerItem("" + photoId, markerItem);
    }

    /**
    * 보행자의 이동경로 메소드
     * */
    public void drawPedestrianPath() {

    //  double startWalkLatitude = startDogwalkerLatitude;
    //  double startWalkLongitude = startDogwalkerLongitude;
//      alTMapPoint.add( new TMapPoint(startDogwalkerLatitude, startDogwalkerLongitude) ); // 도그워커 출발지점
//      alTMapPoint.add( new TMapPoint(dogwalkerLatitude, dogwalkerLongitude) ); // 도그워커 끝지점

        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.YELLOW);
        tMapPolyLine.setLineWidth(2);
        for( int i=0; i<alTMapPoint.size(); i++ ) {
            tMapPolyLine.addLinePoint( alTMapPoint.get(i) );
        }
        tMapView.addTMapPolyLine("Line1", tMapPolyLine);

    } //drawPedestrianPath();


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

        double distanceMeter = distance(currentDogwalkerLatitude, currentDogwalkerLongitude, dogwalkerLatitude, dogwalkerLongitude, "meter");
        recentWalkDistance = recentWalkDistance + distanceMeter;
        txtWalkDistance = (TextView) findViewById(R.id.txtWalkDistance);
        String WalkContext = String.format("%.2f",recentWalkDistance);
        txtWalkDistance.setText( WalkContext + "m" );

        currentDogwalkerLatitude = dogwalkerLatitude;
        currentDogwalkerLongitude = dogwalkerLongitude;
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
            dist = dist * 1609.34; //1000.0
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
                    walkTimeThread.interrupt(); //스레드 종료
                    isWalkStatus = false;

                    //산책이 끝난 현위치를 마지막 위치로 설정
                    endDogwalkerLatitude = dogwalkerLatitude;
                    endDogwalkerLongitude = dogwalkerLongitude;

                    //산책이 끝날 때 까지 누적된 거리를 저장;
                    walkDistance = recentWalkDistance;
                    //산책이 끝난 시점의 시간을 end_time에 저장
                    end_time = currentTime;
                    dataWalkTime = walkTime;

                    Long totalWalkTime = walkTime;

                    putGpsData();
//                    tMapView.getCaptureImage(20, new TMapView.MapCaptureImageListenerCallback() {
//                        @Override
//                        public void onMapCaptureImage(Bitmap bitmap) {
//
//                        }
//                    });
//                    captureMapImage = tMapView.getCaptureImage(); //Bitmap
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    captureMapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    captureMapDataArray = baos.toByteArray();

                    /**
                     * 결과화면으로 데이터를 보내는 것들
                     * @params captureMapimage  //맵 이미지만 캡처
                     * @params totalWalkDistance //총 걸은 거리
                     * @params totalWalkTime //총 산책 시간
                     * @params PhotoTimes //총 사진찍은 횟수
                     * */
                    Intent intent = new Intent(DogwalkerGpsActivity.this, DogwalkerGpsResult.class);
                  //  intent.putExtra("captureMapimage",captureMapDataArray);
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



    public void captureImage() {
        /**
         * void getCaptureImage(int nTimeOut, final MapCaptureImageListenerCallback MapCaptureListener)
         * TMapView의 화면을 캡쳐한다.
         * @params nTimeOut - 캡쳐를 완료할때 사용되는 시간제한
         * @params MapCaptureListener - 캡쳐가 완료되면 호출될 Interface
         * */
        tMapView.getCaptureImage(20, new TMapView.MapCaptureImageListenerCallback() {
            @Override
            public void onMapCaptureImage(final Bitmap bitmap) {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                    // 외부 저장공간이 사용가능하다면
                    permissionManager.request(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionManager.PermissionListener() {
                        @Override
                        public void granted() {
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "TMAPOpenAPI");
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            if (file.exists()) {
                                OutputStream out = null;
                                String fileName = System.currentTimeMillis() + ".png";
                                File fileCacheItem = new File(file.toString() + File.separator + fileName);
                                try {
                                    fileCacheItem.createNewFile();
                                    out = new FileOutputStream(fileCacheItem);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    Toast.makeText(DogwalkerGpsActivity.this, "Saved :" + fileCacheItem.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(DogwalkerGpsActivity.this, "캡처 이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                finally {
                                    if(out != null) {
                                        try {
                                            out.close();
                                        }catch(Exception e1) {}
                                    }
                                }
                            }
                            else {
                                Toast.makeText(DogwalkerGpsActivity.this, "캡쳐 디렉터리 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void denied() {
                        }
                    });
                }
            }
        });
    }


    /**
     * Retrofit Method
     * 아래부터는 Retrofit이 들어가는 메소드만을 모아두었다.
     *
     * ************/
    /**
     *
     * @POST Dogwalker GPS INFO
     * 도그워커의 산책 시작위치, 종료 위치, 이동거리 및 시간을 전송한다.
     * */
    private void postGpsData() {
        GpsService gpsService = ServiceBuilder.create(GpsService.class);
        /**
         * 통신 테스트를 위해 임의의 값을 넣어봄.
         * **/
        HashMap<String, Object> params = new HashMap<>();
        //TODO: 상품아이디 동적으로 변경하기
        //아이디는 서버에서 자동으로 생성
        params.put("walkingServiceId", serviceId);
        params.put("startDogwalkerLatitude", startDogwalkerLatitude);
        params.put("startDogwalkerLongitude",startDogwalkerLongitude);
        params.put("endDogwalkerLatitude", dogwalkerLatitude);
        params.put("endDogwalkerLongitude",dogwalkerLongitude);
        params.put("walkDistance", walkDistance);
        params.put("start_time", start_time);
        params.put("end_time",end_time);
        params.put("walkTime",walkTime);



        Call<GpsVo> call = gpsService.postGpsData(params);
        call.enqueue(new Callback<GpsVo>() { //비동기적 호출
            @Override
            public void onResponse(Call<GpsVo> call, Response<GpsVo> response) {
                if (response.isSuccessful()) {
                    GpsVo gpsVo = response.body();

                    gpsId = gpsVo.getId();
                    Log.d("TEST 서버전송", "GPS INFO 통신 성공");
                    Log.d("TEST", "" + gpsVo.getId());
                    Log.d("TEST", "" + gpsVo.getStartDogwalkerLatitude());
                    Log.d("TEST", "" + gpsVo.getStartDogwalkerLongitude());
                    Log.d("TEST", "" + gpsVo.getEndDogwalkerLatitude());
                    Log.d("TEST", "" + gpsVo.getEndDogwalkerLongitude());
                    Log.d("TEST", "" + gpsVo.getWalkDistance());
                    Log.d("TEST", "" + gpsVo.getStart_time());
                    Log.d("TEST", "" + gpsVo.getEnd_time());
                    Log.d("TEST", "" + gpsVo.getWalkTime());

                    postLocationData();
                    isWalkStatus = true;

                    if (gpsVo != null) {
                    }
                    Log.d("TEST", "onResponse:END ");
                }
            }
            @Override
            public void onFailure(Call<GpsVo> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패\n산책 정보를 전송할 수 없습니다.",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "Retrofit 통신 실패\n산책 정보를 전송할 수 없습니다.");
            }
        });

    }


    /**
     *
     * @PUT Dogwalker GPS INFO
     * 도그워커의 산책 시작위치, 종료 위치, 이동거리 및 시간을 수정한다.
     * */
    private void putGpsData() {
        GpsService gpsService = ServiceBuilder.create(GpsService.class);
        /**
         * 통신 테스트를 위해 임의의 값을 넣어봄.
         * **/
        HashMap<String, Object> params = new HashMap<>();
        //TODO: 동적으로 할당하기
        params.put("walkingServiceId", serviceId);
        params.put("endDogwalkerLatitude", dogwalkerLatitude);
        params.put("endDogwalkerLongitude",dogwalkerLongitude);
        params.put("walkDistance", walkDistance);
        params.put("walkTime",dataWalkTime);


        Call<GpsVo> call = gpsService.putGpsData(serviceId,params);  //TODO: 상품아이디 동적으로 변경하기
        call.enqueue(new Callback<GpsVo>() { //비동기적 호출
            @Override
            public void onResponse(Call<GpsVo> call, Response<GpsVo> response) {
                if (response.isSuccessful()) {
                    GpsVo gpsVo = response.body();

                    Log.d("TEST 서버전송", "GPS INFO 통신 PUT 성공");
                    Log.d("TEST", "" + gpsVo.getId());
                    Log.d("TEST", "" + gpsVo.getEndDogwalkerLatitude());
                    Log.d("TEST", "" + gpsVo.getEndDogwalkerLongitude());
                    Log.d("TEST", "" + gpsVo.getWalkDistance());
                    Log.d("TEST", "" + gpsVo.getWalkTime());

                    postLocationData();
                    if (gpsVo != null) {
                    }
                    Log.d("TEST", "onResponse:END ");
                }
            }
            @Override
            public void onFailure(Call<GpsVo> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패\n산책 정보를 전송할 수 없습니다.",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "Retrofit 통신 실패\n산책 정보를 수정할 수 없습니다.");
            }
        });

    }



    /**
     * Retrofit Method
     * POST DOGWALKER LOCATION
     * 도그워커의 현재 위치를 서버로 전송한다.
     * */
    private void postLocationData() {
        GpsDogwalkerLocationService gpsDogwalkerLocationService = ServiceBuilder.create(GpsDogwalkerLocationService.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("walkingServiceId", serviceId); //gpsId = ?? TODO: 상품아이디 동적으로 변경하기
        params.put("dogwalkerLatitude", dogwalkerLatitude); //double
        params.put("dogwalkerLongitude", dogwalkerLongitude);//double


        Call<GpsLocationVo> call = gpsDogwalkerLocationService.postLocationData(params);
        call.enqueue(new Callback<GpsLocationVo>() { //비동기적 호출
            @Override
            public void onResponse(Call<GpsLocationVo> call, Response<GpsLocationVo> response) {
                if (response.isSuccessful()) {
                    GpsLocationVo gpsLocationVo = response.body();
                    Log.d("TEST 서버전송", "도그워커 현재위치 전송");
                    Log.d("TEST", "" + gpsLocationVo.getGpsId());
                    Log.d("TEST", "" + gpsLocationVo.getDogwalkerLatitude());
                    Log.d("TEST", "" + gpsLocationVo.getDogwalkerLongitude());

                    if (gpsLocationVo != null) {
                    }
                    Log.d("TEST", "onResponse:END ");
                }
            }
            @Override
            public void onFailure(Call<GpsLocationVo> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패\n도그워커의 현재위치 정보를 전송할 수 없습니다..",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패, 도그워커의 현재위치 정보를 전송할 수 없습니다.");
            }
        });
    }


    /**
     * Retrofit Method
     * 안드로이드 파일 업로드 샘플
     *
     * 사진 찍기 이후 사진을 일단 서버에 업로드
     * 마커ID, 사진파일, 사진찍은 위치를 서버에 POST
     *
     */
    private void imageUploadSample(Bitmap rotatedBitmap) {
        GpsMarkerService gpsMarkerService = ServiceBuilder.create(GpsMarkerService.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] dataArray = baos.toByteArray();

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), dataArray);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("fileUpload\"; filename=\"photo.png", fileBody); //photoData   //성공
        params.put("walkingServiceId", RequestBody.create(MediaType.parse("text"),String.valueOf(serviceId))); //문제없음  //gpsId = 33 TODO: 상품아이디 동적으로 변경하기
        params.put("photoLatitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(photoLatitude))));
        params.put("photoLongitude", RequestBody.create(MediaType.parse("text"), (String.valueOf(photoLongitude))));

        Call<PhotoVO> call = gpsMarkerService.postImageUploadData(params);
        call.enqueue(new Callback<PhotoVO>() {
            @Override
            public void onResponse(Call<PhotoVO> call, Response<PhotoVO> response) {
                if (response.isSuccessful()) {
                    PhotoVO photoIdVO = response.body();
                    if (photoIdVO != null) {
                        Log.d("TEST 서버전송", "마커 사진 전송");
                        Log.d("TEST", "" + photoIdVO.getId());
                        Log.d("TEST", "" + photoIdVO.getPhotoLatitude());
                        Log.d("TEST", "" + photoIdVO.getPhotoLongitude());
                        showMarkerPoint(photoIdVO.getId()); //사진Id를 이용해 마커 생성
                    }
                    markTime += 1;
                }
            }
            @Override
            public void onFailure(Call<PhotoVO> call, Throwable t) {
                Log.d("TEST", "이미지 통신 에러");
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }//imageUploadSample


}//DogwalkerGpsActivity