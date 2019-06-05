package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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





public class GpsMainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

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
    ArrayList<TMapPoint> dogwalkerPhotoPoint = new ArrayList<TMapPoint>();


    /***
     * 버튼 아이디 정리
     */
    private static final int[] mArrayMapButton = {
/*            R.id.btnSetMapType,
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
            R.id.btnCapture,
    };
    private TextView txtusergongback;
    private TextView txtShowWalkDistance;
    private TextView txtShowWalkTime;

    /**
     * setSKTMapApiKey()에 ApiKey를 입력 한다.
     */
    private void apiKeyMapView() {
        tMapView.setSKTMapApiKey(TMAP_API_KEY);
    }

    /**
     * 권한 요청 관리자*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionManager.setResponse(requestCode, grantResults); // 권한요청 관리자에게 결과 전달
    }



    /**
     * 단말의 위치탐색 메소드
     * */
    @Override
    public void onLocationChange(Location location) {
        LogManager.printLog("onLocationChange :::> " + location.getLatitude() +
                " " + location.getLongitude() +
                " " + location.getSpeed() +
                " " + location.getAccuracy());
        if(m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
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



        /***
         * FAB버튼
         *강아지 주인의 현재 위치를 추적하는 아이콘으로 만들 생각
         */

        btnTrackDogWalkerFAB = (FloatingActionButton) findViewById(R.id.btnFloatingActionButton);
        btnTrackDogWalkerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.setZoomLevel(15);
                tMapView.setIconVisibility(true);
                setGps();
                Toast.makeText(getApplicationContext(), "도그워커의 현재 위치를 찾는 중입니다.\n잠시 기다려 주세요.", Toast.LENGTH_LONG).show();
                tMapView.setCenterPoint(dogwalkerLatitude, dogwalkerLongitude);
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

        /**
         * PermissionManager 클래스에서 상속
         * 위치정보 허용기능
         * */
        permissionManager.request(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionManager.PermissionListener() {
            @Override
            public void granted() {
                tMapGps = new TMapGpsManager(GpsMainActivity.this);
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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000 * 5, // 통지사이의 최소 시간간격 (miliSecond)
                5, // 통지사이의 최소 변경거리 (m)
                mLocationListener);

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }






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
//            case R.id.btnGetDogwalkerLocation: 	getGpsInfo(); 	break;
//            case R.id.btnSetCompassMode	  : 	setCompassMode();		break;
//            case R.id.btnGetIsCompass     :	getIsCompass();			break;
//            case R.id.btnSetSightVisible  : 	setSightVisible();		break;
//            case R.id.btnSetTrackIngMode  : 	setTrackingMode();		break;
//            case R.id.btnGetIsTracking	  : 	getIsTracking();		break;
//            case R.id.btnRemoveMarker     : 	removeMarker(); 		break;
//            case R.id.btnMapPath		  : 	drawMapPath();			break;
//            case R.id.btnRemoveMapPath    :     removeMapPath(); 		break;
//            case R.id.btnDisplayMapInfo   :     displayMapInfo(); 		break;
//            case R.id.btnPedestrian_Path  :     drawPedestrianPath(); break;
//            case R.id.btnGetCenterPoint   :     getCenterPoint();		break;
//            case R.id.btnConvertToAddress :    convertToAddress(); 	break;
//            case R.id.btnTileType		  : 	setTileType();			break;
//            case R.id.btnMarkerPoint2    :    showMarkerPoint2();     break;
//            case R.id.btnCapture		  :     captureImage(); 		break;
        }
    }





    /**
     * showMarkerPoint
     * 지도에 마커를 표출한다.
     *
     */
    public void showMarkerPoint() {
        for (int i = 0; i < dogwalkerPhotoPoint.size(); i++) {

            Log.d("TEST", "마커생성" + i);

            //도그워커의 현재위치에 마커 생성
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            String strID = String.format("%02d", i);
            // 마커 아이콘 지정
            markerItem1.getTMapPoint();
            markerItem1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
            // 마커의 좌표 지정
            markerItem1.setTMapPoint(dogwalkerPhotoPoint.get(i));
            markerItem1.setCanShowCallout(true);
            markerItem1.setCalloutTitle("테스트" + strID);
            markerItem1.setCalloutSubTitle("안녕하세요" + strID);

            Bitmap retBitmap = getBitmap("http://52.79.234.182:3000/gps/marker/image?markerId=" + i);
            markerItem1.setCalloutLeftImage(retBitmap);

            //지도에 마커 추가
            tMapView.addMarkerItem(strID, markerItem1);
        }
    }


    /*** image url을 받아서 bitmap을 생성하고 리턴한다.
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

        alTMapPoint.add( new TMapPoint(startDogwalkerLatitude, startDogwalkerLongitude) ); // 도그워커 출발지점
        alTMapPoint.add( new TMapPoint(dogwalkerLatitude, dogwalkerLongitude) ); // 도그워커 끝지점

        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.YELLOW);
        tMapPolyLine.setLineWidth(2);
        for( int i=0; i<alTMapPoint.size(); i++ ) {
            tMapPolyLine.addLinePoint( alTMapPoint.get(i) );
        }
        tMapView.addTMapPolyLine("Line1", tMapPolyLine);
    }

    /**
     * 위치가 바뀔때마다 (onLocationChanged)
     * 현재위치에 새로운 포인트를 추가
     *
     * @param lat 위도
     * @param lon 경도
     * */
    public void addPedestrianPoint(double lat, double lon){
        alTMapPoint.add( new TMapPoint(dogwalkerLatitude, dogwalkerLongitude) );
    }


    /**
     * 도그워커의 산책시간 및 이동거리를 표시해주는 메소드
     * */

    public void showDistanceAndTime(){

        String WalkContext = String.format("%.2f",walkDistance);
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

//		    tmapdata.geoCodingWithAddressType("F02", "서울시", "구로구", "새말로", "6", "", new GeoCodingWithAddressTypeListenerCallback() {
//		    	
//				@Override
//				public void onGeoCodingWithAddressType(TMapGeocodingInfo geocodingInfo) {
//					LogManager.printLog(">>> strMatchFlag : " + geocodingInfo.strMatchFlag);
//					LogManager.printLog(">>> strLatitude : " + geocodingInfo.strLatitude);
//					LogManager.printLog(">>> strLongitude : " + geocodingInfo.strLongitude);
//					LogManager.printLog(">>> strCity_do : " + geocodingInfo.strCity_do);
//					LogManager.printLog(">>> strGu_gun : " + geocodingInfo.strGu_gun);
//					LogManager.printLog(">>> strLegalDong : " + geocodingInfo.strLegalDong);
//					LogManager.printLog(">>> strAdminDong : " + geocodingInfo.strAdminDong);
//					LogManager.printLog(">>> strBunji : " + geocodingInfo.strBunji);
//					LogManager.printLog(">>> strNewMatchFlag : " + geocodingInfo.strNewMatchFlag);
//					LogManager.printLog(">>> strNewLatitude : " + geocodingInfo.strNewLatitude);
//					LogManager.printLog(">>> strNewLongitude : " + geocodingInfo.strNewLongitude);
//					LogManager.printLog(">>> strNewRoadName : " + geocodingInfo.strNewRoadName);
//					LogManager.printLog(">>> strNewBuildingIndex : " + geocodingInfo.strNewBuildingIndex);
//					LogManager.printLog(">>> strNewBuildingName : " + geocodingInfo.strNewBuildingName);
//				}
//			});
        }
    }


/*
    public void captureImage() {
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
                                    Toast.makeText(GpsMainActivity.this, "Saved :" + fileCacheItem.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(GpsMainActivity.this, "캡처 이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(GpsMainActivity.this, "캡쳐 디렉터리 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void denied() {

                        }
                    });
                }
            }
        });
    }*/


    /**
     * Retrofit Method
     * */

    /**
     * GET GPS INFO
     * 도그워커의 산책정보(시작위치, 끝위치, 이동거리, 시작시간, 끝시간 등)을 서버로부터 받아온다.
     */
    public void getGpsInfo() {
        GpsService gpsService = ServiceBuilder.create(GpsService.class);
        Call<GpsVo> call = gpsService.doGetGpsInfo();
        call.enqueue(new Callback<GpsVo>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<GpsVo> call, @NonNull Response<GpsVo> response) {
                GpsVo gpsGetVo = response.body();
                if(gpsGetVo != null){
                    Toast.makeText(getApplicationContext(), "도그워커 위도" + gpsGetVo.getDogwalkerLatitude()
                            + "도그워커 경도" + gpsGetVo.getDogwalkerLongitude(), Toast.LENGTH_SHORT).show();

                    Log.d("TEST", "onResponse: " + gpsGetVo.getId());
//                    Log.d("TEST", "onResponse: " + gpsVo.getMarkerId());
//                    Log.d("TEST", "onResponse: " + gpsVo.getPhotoURL());
//                    Log.d("TEST", "onResponse: " + gpsVo.getPhotoLatitude());
//                    Log.d("TEST", "onResponse: " + gpsVo.getPhotoLongitude());
//                    Log.d("TEST", "onResponse: " + gpsVo.getDogwalkerLatitude());
//                    Log.d("TEST", "onResponse: " + gpsVo.getDogwalkerLongitude());
                    Log.d("TEST", "onResponse: " + gpsGetVo.getStartDogwalkerLatitude());
                    Log.d("TEST", "onResponse: " + gpsGetVo.getStartDogwalkerLongitude());
                    Log.d("TEST", "onResponse: " + gpsGetVo.getEndDogwalkerLatitude());
                    Log.d("TEST", "onResponse: " + gpsGetVo.getEndDogwalkerLongitude());
                    Log.d("TEST", "onResponse: " + gpsGetVo.getWalkDistance());
                    Log.d("TEST", "onResponse: " + gpsGetVo.getStart_time());
                    Log.d("TEST", "onResponse: " + gpsGetVo.getEnd_time());
                    Log.d("TEST", "onResponse: " + gpsGetVo.getWalkTime());


                    /**서버로부터 받은 데이터를 저장*/
                    startDogwalkerLatitude = gpsGetVo.getStartDogwalkerLatitude();
                    startDogwalkerLongitude = gpsGetVo.getStartDogwalkerLongitude();
                    endDogwalkerLatitude = gpsGetVo.getEndDogwalkerLatitude();
                    endDogwalkerLongitude = gpsGetVo.getEndDogwalkerLongitude();
                    walkDistance = gpsGetVo.getWalkDistance();
                    start_time = gpsGetVo.getStart_time();
                    end_time = gpsGetVo.getEnd_time();
                    walkTime = gpsGetVo.getWalkTime();


/*                    tMapView.setLocationPoint(gpsGetVo.getDogwalkerLatitude(), gpsGetVo.getDogwalkerLongitude());
                    String strResult = String.format("현재위치의 좌표의 위도 경도를 설정\n " +
                            "Latitude = %f Longitude = %f", gpsGetVo.getDogwalkerLatitude(), gpsGetVo.getDogwalkerLongitude());
                    Common.showAlertDialog(GpsMainActivity.this, "", strResult);*/
                }
                Log.d("TEST", "onResponse:END ");
            }
            @Override
            public void onFailure(@NonNull Call<GpsVo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패\n위치를 전달받을 수 없습니다.",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패");
            }
        });
    }


    /**
     * GET MARKER INFO
     * 도그워커가 촬영한 사진 및 마커 데이터를 서버로부터 받아온다.
     */
    public void getMarkerInfo() {
        GpsMarkerService gpsMarkerService = ServiceBuilder.create(GpsMarkerService.class);
        Call<GpsMarkerVo> call = gpsMarkerService.doGetMarkerInfo();
        call.enqueue(new Callback<GpsMarkerVo>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<GpsMarkerVo> call, @NonNull Response<GpsMarkerVo> response) {
                GpsMarkerVo gpsGetMarkerVo = response.body();
                if(gpsGetMarkerVo != null){
                    Toast.makeText(getApplicationContext(), "사진 위도" + gpsGetMarkerVo.getPhotoLatitude()
                            + "사진 경도" + gpsGetMarkerVo.getPhotoLongitude(), Toast.LENGTH_SHORT).show();

                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getMarkerId());
                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getPhotoData());
                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getPhotoLatitude());
                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getPhotoLongitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getDogwalkerLatitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getDogwalkerLongitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getStartDogwalkerLatitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getStartDogwalkerLongitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getEndDogwalkerLatitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getEndDogwalkerLongitude());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getWalkDistance());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getStart_time());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getEnd_time());
//                    Log.d("TEST", "onResponse: " + gpsGetMarkerVo.getWalkTime());



                    /**서버로부터 받은 데이터를 저장*/
//                    Ot = gpsGetMarkerVo.getStartDogwalkerLatitude();
//                    startDogwalkerLongitude = gpsGetMarkerVo.getStartDogwalkerLongitude();
//                    endDogwalkerLatitude = gpsGetMarkerVo.getEndDogwalkerLatitude();
//                    endDogwalkerLongitude = gpsGetMarkerVo.getEndDogwalkerLongitude();
//                    walkDistance = gpsGetMarkerVo.getWalkDistance();
//                    start_time = gpsGetMarkerVo.getStart_time();
//                    end_time = gpsGetMarkerVo.getEnd_time();
//                    walkTime = gpsGetMarkerVo.getWalkTime();

                }
                Log.d("TEST", "onResponse:END ");
            }
            @Override
            public void onFailure(@NonNull Call<GpsMarkerVo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패\n위치를 전달받을 수 없습니다.",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패");
            }
        });
    }


    /**
     * GET LOCATION INFO
     * 도그워커의 현재 위치를 서버로부터 받아온다.
     */
    public void getLocationInfo() {
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

                    /**서버로부터 받은 데이터를 저장*/
                    addPedestrianPoint(gpsGetLocationVo.getDogwalkerLatitude(), gpsGetLocationVo.getDogwalkerLongitude());

                }
                Log.d("TEST", "onResponse:END ");
            }
            @Override
            public void onFailure(@NonNull Call<GpsLocationVo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Retrofit 통신 실패\n위치를 전달받을 수 없습니다.",Toast.LENGTH_SHORT).show();
                Log.d("TEST", "통신 실패");
            }
        });
    }





}//GpsMainActivity

