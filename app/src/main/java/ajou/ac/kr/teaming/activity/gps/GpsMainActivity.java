package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapLabelInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.LogManager;
import ajou.ac.kr.teaming.service.gps.GpsService;
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
 * 도그워커와 통화하기 및 채팅하기
 * ----------------------------------------------------------------------------
 *
 * - 생성해야할 메소드
 *
 * 1. 마커 클릭
 * 1-1.풍선뷰 안의 사진정보와 위치정보 확인
 * 2. 풍선뷰 확인
 * 3. 도그워커 위치 추적 (fab)
 * 4. 도그워커의 이동경로 지도에 표시
 * 5. 도그워커가 이동한 거리 및 산책시간 확인
 *
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


    /***
     * 버튼 아이디 정리
     */
    private static final int[] mArrayMapButton = {
            R.id.btnSetMapType,
            R.id.btnGetLocationPoint,
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
            R.id.btnCapture,
            R.id.btnMarkerPoint2
    };

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
        setContentView(R.layout.activity_gps_main);

        mContext = this;
        permissionManager = new PermissionManager(this); // 권한요청 관리자


        /**지도 생성*/
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        apiKeyMapView(); //T MAP API 서버키 인증
        linearLayoutTmap.addView( tMapView );

        initView(); //리스너 실행



        /***
         * FAB버튼
         *강아지 주인의 현재 위치를 추적하는 아이콘으로 만들 생각
         */

        btnTrackDogWalkerFAB = (FloatingActionButton) findViewById(R.id.btnFloatingActionButton);
        btnTrackDogWalkerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.setTrackingMode(true);
                tMapView.setSightVisible(true);
                tMapView.setZoomLevel(15);
                tMapView.setIconVisibility(true);
                setGps();
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
            case R.id.btnSetMapType		  :		setMapType(); 			break;
            case R.id.btnGetDogwalkerLocation: 	getDogwalkerLocation(); 	break;
            case R.id.btnSetCompassMode	  : 	setCompassMode();		break;
            case R.id.btnGetIsCompass     :	getIsCompass();			break;
            case R.id.btnSetSightVisible  : 	setSightVisible();		break;
            case R.id.btnSetTrackIngMode  : 	setTrackingMode();		break;
            case R.id.btnGetIsTracking	  : 	getIsTracking();		break;
            case R.id.btnRemoveMarker     : 	removeMarker(); 		break;
            case R.id.btnMapPath		  : 	drawMapPath();			break;
            case R.id.btnRemoveMapPath    :     removeMapPath(); 		break;
            case R.id.btnDisplayMapInfo   :     displayMapInfo(); 		break;
            case R.id.btnPedestrian_Path  :     drawPedestrianPath(); break;
            case R.id.btnGetCenterPoint   :     getCenterPoint();		break;
            case R.id.btnConvertToAddress :    convertToAddress(); 	break;
            case R.id.btnTileType		  : 	setTileType();			break;
            case R.id.btnCapture		  :     captureImage(); 		break;
            case R.id.btnMarkerPoint2    :    showMarkerPoint2();     break;
        }
    }

    public TMapPoint randomTMapPoint() {

        /**
         * 서울 특별시 내부
         * */

        double latitude = ((double)Math.random() ) * (37.575113-37.483086) + 37.483086;
        double longitude = ((double)Math.random() ) * (127.027359-126.878357) + 126.878357;

        latitude = Math.min(37.575113, latitude);
        latitude = Math.max(37.483086, latitude);
        longitude = Math.min(127.027359, longitude);
        longitude = Math.max(126.878357, longitude);

        LogManager.printLog("randomTMapPoint" + latitude + " " + longitude);

        TMapPoint point = new TMapPoint(latitude, longitude);

        return point;
    }


    /**
     * setMapType
     * Map의 Type을 설정한다.
     */
    public void setMapType() {
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Select MAP Type")
                .setSingleChoiceItems(R.array.a_maptype, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        LogManager.printLog("Set Map Type " + item);
                        dialog.dismiss();

                        tMapView.setMapType(item);
                    }
                }).show();
    }


    /**
     * setLocationPoint
     * 현재위치로 표시될 좌표의 위도,경도를 설정한다.
     */
    public void setLocationPoint() {

        double 	Latitude  = 37.5077664;
        double Longitude = 126.8805826;

        LogManager.printLog("setLocationPoint " + Latitude + " " + Longitude);
        tMapView.setLocationPoint(Longitude, Latitude);
        String strResult = String.format("현재위치의 좌표의 위도 경도를 설정\n Latitude = %f Longitude = %f", Latitude, Longitude);
        Common.showAlertDialog(this, "", strResult);
    }

    /**
     * setMapIcon
     * 현재위치로 표시될 아이콘을 설정한다.
     */

    /*
    public void setMapIcon() {
        m_bShowMapIcon = !m_bShowMapIcon;

        if (m_bShowMapIcon) {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_launcher);
            tMapView.setIcon(bitmap);
        }
        tMapView.setIconVisibility(m_bShowMapIcon);
    }
    */

    /**
     * setCompassMode
     * 단말의 방항에 따라 움직이는 나침반모드로 설정한다.
     */
    public void setCompassMode() {
        tMapView.setCompassMode(!tMapView.getIsCompass());
    }

    /**
     * getIsCompass
     * 나침반모드의 사용여부를 반환한다.
     */
    public void getIsCompass() {
        Boolean bGetIsCompass = tMapView.getIsCompass();
        Common.showAlertDialog(this, "", "현재 나침반 모드는 : " + bGetIsCompass.toString() );
    }


    /**
     * setSightVisible
     * 시야표출여부를 설정한다.
     */
    public void setSightVisible() {
        m_bSightVisible = !m_bSightVisible;
        tMapView.setSightVisible(m_bSightVisible);
    }

    /**
     * setTrackingMode
     * 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
     */
    public void setTrackingMode() {
        m_bTrackingMode = !m_bTrackingMode;
        tMapView.setTrackingMode(m_bTrackingMode);
    }

    /**
     * getIsTracking
     * 트래킹모드의 사용여부를 반환한다.
     */
    public void getIsTracking() {
        Boolean bIsTracking = tMapView.getIsTracking();
        Common.showAlertDialog(this, "", "현재 트래킹모드 사용 여부  : " + bIsTracking.toString() );
    }


    /**
     * showMarkerPoint
     * 지도에 마커를 표출한다.

     *
     * 현재 클릭시 오류 발생중
     *
     */
    /*
    public void showMarkerPoint() {
        Bitmap bitmap = null;

        TMapPoint point = new TMapPoint(37.570841, 126.985302); // SKT타워

        TMapMarkerItem item1 = new TMapMarkerItem();


        //마커 아이콘
        bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.i_location);

        item1.setTMapPoint(point);
        item1.setName("SKT타워");
        item1.setVisible(item1.VISIBLE);
        item1.setIcon(bitmap);
        LogManager.printLog("bitmap " + bitmap.getWidth() + " " + bitmap.getHeight());



        bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.i_location);
        item1.setCalloutTitle("SKT타워");
        item1.setCalloutSubTitle("을지로입구역 500M");
        item1.setCanShowCallout(true);
        item1.setAutoCalloutVisible(true);


        Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);

        item1.setCalloutRightButtonImage(bitmap_i);

        String strID = String.format("pmarker%d", mMarkerID++);

        tMapView.addMarkerItem(strID, item1);
        mArrayMarkerID.add(strID);


        point = new TMapPoint(37.55102510077652, 126.98789834976196);
        TMapMarkerItem item2 = new TMapMarkerItem();

        item2.setTMapPoint(point);
        item2.setName("N서울타워");
        item2.setVisible(item2.VISIBLE);
        item2.setCalloutTitle("청호타워 4층");

        item2.setCanShowCallout(true);

        bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
        item2.setCalloutRightButtonImage(bitmap_i);

        bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.pin_tevent);
        item2.setIcon(bitmap);

        strID = String.format("pmarker%d", mMarkerID++);

        tMapView.addMarkerItem(strID, item2);
        mArrayMarkerID.add(strID);


        point = new TMapPoint(37.58102510077652, 126.98789834976196);
        item2 = new TMapMarkerItem();

        item2.setTMapPoint(point);
        item2.setName("N서울타워");
        item2.setVisible(item2.VISIBLE);
        item2.setCalloutTitle("창덕궁 청호타워 4층");

        item2.setCalloutSubTitle("을지로입구역 500M");
        item2.setCanShowCallout(true);


        bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
        item2.setCalloutRightButtonImage(bitmap_i);

        bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gpsmarker);
        item2.setIcon(bitmap);

        strID = String.format("pmarker%d", mMarkerID++);

        tMapView.addMarkerItem(strID, item2);
        mArrayMarkerID.add(strID);

        point = new TMapPoint(37.58102510077652, 126.99789834976196);
        item2 = new TMapMarkerItem();

        item2.setTMapPoint(point);
        item2.setName("N서울타워");
        item2.setVisible(item2.VISIBLE);
        item2.setCalloutTitle("대학로 혜화역111111");

        item2.setCanShowCallout(true);

        item2.setCalloutLeftImage(bitmap);

        bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
        item2.setCalloutRightButtonImage(bitmap_i);


        bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.end);
        item2.setIcon(bitmap);

        strID = String.format("pmarker%d", mMarkerID++);

        tMapView.addMarkerItem(strID, item2);
        mArrayMarkerID.add(strID);

        for(int i = 4; i < 10; i++) {
            TMapMarkerItem item3 = new TMapMarkerItem();

            item3.setID(strID);
            item3.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.gpsmarker));

            item3.setTMapPoint(randomTMapPoint());
            item3.setCalloutTitle(">>>>" + strID + "<<<<<");
            item3.setCanShowCallout(true);

            strID = String.format("pmarker%d", mMarkerID++);

            tMapView.addMarkerItem(strID, item2);
            mArrayMarkerID.add(strID);
        }
    }
    */

    public void showMarkerPoint2() {
        ArrayList<Bitmap> markerList = null;
        for(int i = 0; i < 20; i++) {

            MarkerOverlay marker1 = new MarkerOverlay(this, tMapView);
            String strID = String.format("%02d", i);

            marker1.setID(strID);
            marker1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.gpsmarker));
            marker1.setTMapPoint(randomTMapPoint());

            if (markerList == null) {
                markerList = new ArrayList<Bitmap>();
            }

            markerList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.gpsmarker));
            markerList.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.end));

            marker1.setAnimationIcons(markerList);
            tMapView.addMarkerItem2(strID, marker1);
        }

        tMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {

            @Override
            public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
                LogManager.printLog("ClickEvent " + " id " + id + " \n" + markerItem2.latitude + " " +  markerItem2.longitude);

                String strMessage = "ClickEvent " + " id " + id + " \n" + markerItem2.latitude + " " +  markerItem2.longitude;
                Common.showAlertDialog(GpsMainActivity.this, "TMapMarker2", strMessage);
            }
        });
    }



    public void removeMarker() {
        if(mArrayMarkerID.size() <= 0 )
            return;

        String strMarkerID = mArrayMarkerID.get(mArrayMarkerID.size() - 1);
        tMapView.removeMarkerItem(strMarkerID);
        mArrayMarkerID.remove(mArrayMarkerID.size() - 1);
    }



    /**
     * displayMapInfo()
     * POI들이 모두 표시될 수 있는 줌레벨 결정함수와 중심점리턴하는 함수
     */
    public void displayMapInfo() {	
		/*
		TMapPoint point1 = mMapView.getCenterPoint();		
		TMapPoint point2 = randomTMapPoint();
		*/
        TMapPoint point1 = new TMapPoint(37.541642248630524, 126.99599611759186);
        TMapPoint point2 = new TMapPoint(37.541243493556976, 126.99659830331802);
        TMapPoint point3 = new TMapPoint(37.540909826755524, 126.99739581346512);
        TMapPoint point4 = new TMapPoint(37.541080713272095, 126.99874675273895);

        ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();

        point.add(point1);
        point.add(point2);
        point.add(point3);
        point.add(point4);

        TMapInfo info = tMapView.getDisplayTMapInfo(point);

        String strInfo = "Center Latitude" + info.getTMapPoint().getLatitude() + "Center Longitude" + info.getTMapPoint().getLongitude() +
                "Level " + info.getTMapZoomLevel();

        Common.showAlertDialog(this, "", strInfo );
    }



    /**
     * removeMapPath
     * 경로 표시를 삭제한다. 
     */
    public void removeMapPath() {
        tMapView.removeTMapPath();
    }

    /**
     * drawMapPath
     * 지도에 시작-종료 점에 대해서 경로를 표시한다.
     */
    public void drawMapPath() {
        TMapPoint point1 = tMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathData(point1, point2, new TMapData.FindPathDataListenerCallback() {

            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                tMapView.addTMapPath(polyLine);
            }
        });
    }


    public void drawPedestrianPath() {
        TMapPoint point1 = tMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                tMapView.addTMapPath(polyLine);
            }
        });
    }

    /**
     * getCenterPoint
     * 지도의 중심점을 가지고 온다. 
     */
    public void getCenterPoint() {
        TMapPoint point = tMapView.getCenterPoint();

        Common.showAlertDialog(this, "", "지도의 중심 좌표는 " + point.getLatitude() + " " + point.getLongitude() );
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
    /**
     * getBizCategory
     * 업종별 category를 요청한다. 

    public void getBizCategory() {
        TMapData tmapdata = new TMapData();

        tmapdata.getBizCategory(new TMapData.BizCategoryListenerCallback() {
            @Override
            public void onGetBizCategory(ArrayList<BizCategory> poiItem) {
                for (int i = 0; i < poiItem.size(); i++) {
                    BizCategory item = poiItem.get(i);
                    LogManager.printLog("UpperBizCode " + item.upperBizCode + " " + "UpperBizName " + item.upperBizName );
                    LogManager.printLog("MiddleBizcode " + item.middleBizCode + " " + "MiddleBizName " + item.middleBizName);
                }
            }
        });
    }
    */


    public void setTileType() {
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Select MAP Tile Type")
                .setSingleChoiceItems(R.array.a_tiletype, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        LogManager.printLog("Set Map Tile Type " + item);
                        dialog.dismiss();
//				mMapView.setTileType(item);

                        Resources res = getResources();
                        String[] arrTileType = res.getStringArray(R.array.a_tiletype);
                        switch (arrTileType[item]) {
                            case "NORMALTILE":
                                tMapView.setTileType(TMapView.TILETYPE_NORMALTILE);
                                break;
                            case "HDTILE":
                                tMapView.setTileType(TMapView.TILETYPE_HDTILE);
                                break;
                        }
                    }
                }).show();
    }



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
    }


    /**
     * Retrofit Method
     * */

    /**
     * GET DOGWALKER LOCATION
     * 도그워커의 좌표의 위도, 경도를 서버로부터 받아온다.
     */
    public void getDogwalkerLocation() {

        GpsService gpsService = GpsService.retrofit.create(GpsService.class);
        Call<GpsVo> call = gpsService.doGetDogwalkerLocation(m_Latitude,m_Longitude);
        call.enqueue(new Callback<GpsVo>() { //비동기적 호출
            @Override
            public void onResponse(@NonNull Call<GpsVo> call, @NonNull Response<GpsVo> response) {
                GpsVo GpsVos = response.body();
                if(GpsVos != null){

                    Toast.makeText(getApplicationContext(), "도그워커 위도" + GpsVos.getDogwalkerLatitude()
                                                                + "도그워커 경도" + GpsVos.getDogwalkerLongitude(), Toast.LENGTH_SHORT).show();

                    tMapView.setLocationPoint(GpsVos.getDogwalkerLatitude(), GpsVos.getDogwalkerLongitude());
                    String strResult = String.format("현재위치의 좌표의 위도 경도를 설정\n " +
                            "Latitude = %f Longitude = %f", GpsVos.getDogwalkerLatitude(), GpsVos.getDogwalkerLongitude());
                    Common.showAlertDialog(GpsMainActivity.this, "", strResult);
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



}//GpsMainActivity

