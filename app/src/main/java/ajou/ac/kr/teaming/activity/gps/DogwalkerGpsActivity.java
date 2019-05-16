package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ajou.ac.kr.teaming.R;

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
 *
 * ----------------------------------------------------------------------------
 *
 * 도그워커의 위치 정보를 Tmap위치정보로 치환해야함.
 *
 * */


public class DogwalkerGpsActivity extends AppCompatActivity{

    /***
     * 버튼 아이디 정리
     */
    private static final int[] mArrayMapDogwalkerButton = {
            R.id.btnCallToUser,
            R.id.btnChatToUser,
            R.id.btnPhotoAndMarker,
            R.id.btnSetDogwalkerLocationPoint,
            R.id.btnGetDogWalkerLocationPoint,
            R.id.btnWalkDistance,
            R.id.btnWalkEnd,
    };

    private PermissionManager permissionManager = null;
    private Button btnShowLocation;
    private TextView txtLat;
    private TextView txtLon;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;


    // GPSTracker class
    private DogWalkerGpsInfo gps;



    /**
     * 단말의 위치탐색 메소드
     *
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
    */

    /**
     * onCreate
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalker_gps);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLon = (TextView) findViewById(R.id.txtLon);

        initView();



        // 버튼 클릭시 GPS 정보를 보여주기 위한 이벤트 클래스 등록
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // 권한 요청을 해야 함
                if (!isPermission) {
                    callPermission();
                    return;
                }

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
            }
        });
        callPermission();  // 권한 요청을 해야 함
    }//onCreate


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
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
      //      case R.id.btnGetDogWalkerLocationPoint		  : 	getDogwalkerLocationPoint(); 	break;
            case R.id.btnWalkDistance		              : 	walkDistance(); 			    break;
            case R.id.btnWalkEnd		                  : 	walkEnd(); 			            break;
            //    case R.id.btnSetDogwalkerLocationPoint       :    setDogwalkerLocation();         break;
        }
    }

    /*
    private void setDogwalkerLocation() {
        double 	Latitude  = 37.5077664;
        double Longitude = 126.8805826;

        LogManager.printLog("setLocationPoint " + Latitude + " " + Longitude);
        tMapView.setLocationPoint(Longitude, Latitude);
        String strResult = String.format("현재위치의 좌표의 위도 경도를 설정\n Latitude = %f Longitude = %f", Latitude, Longitude);
        Common.showAlertDialog(this, "", strResult);


    }

    */

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

    /*
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
    */

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




