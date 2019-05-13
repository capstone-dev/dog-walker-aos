package ajou.ac.kr.teaming.activity.gps;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;

public class GpsMainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    private final String TAG = "GpsMainActivity";
    private final String TMAP_API_KEY = "78f4044b-3ca4-439d-8d0e-10135941f054";
    private boolean m_bTrackingMode = true;
    private Context mContext;
    private TMapView tmapview = null;


    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_main);
        mContext = this;
        Button buttonMove1 = (Button)findViewById(R.id.buttonMove1);
        Button buttonMove2 = (Button)findViewById(R.id.buttonMove2);
        Button buttonZoomIn = (Button)findViewById(R.id.buttonZoomIn);
        Button buttonZoomOut = (Button)findViewById(R.id.buttonZoomOut);
        Button buttonZoomLevel10 = (Button)findViewById(R.id.buttonZoomLevel10);




        LinearLayout mapView = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(TMAP_API_KEY);
        mapView.addView(tMapView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            Log.d("locationTest","동의알림");
        }

//        tMapTapi.setSKTMapAuthentication(TMAP_API_KEY);
////        tMapTapi.setOnAuthenticationListener(new TMapTapi.OnAuthenticationListenerCallback() {
////            @Override
////            public void SKTMapApikeySucceed() {
////                Log.i(TAG, "성공");
////                tMapTapi.invokeTmap();
////            }
////            @Override
////            public void SKTMapApikeyFailed(String errorMsg) {
////                Log.e(TAG, "실패: " + errorMsg);
//            }
//        });

        /**
         * 클릭 이벤트 설정 구간
         * Toast시. MapEvent.this가 아닌 getApplicationContext()를 사용할 것.
         * **/
        // 클릭 이벤트 설정
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
            //    Toast.makeText(getApplicationContext(), "onPress~!", Toast.LENGTH_SHORT).show();
                /**
                 * 위치를 누르면 마커가 생성되도록 하기
                 * */
                //마커 생성
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                TMapPoint tMapPoint1 = new TMapPoint(37.570841, 126.985302); // SKT타워       //String startGpsPosition
                // 마커 아이콘
                Bitmap gpsMarker = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.gpsmarker);
                //  resizeBitmap(gpsMarker); //이미지 크기 조절

                markerItem1.setIcon(gpsMarker); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                markerItem1.setName("SKT타워"); // 마커의 타이틀 지정
                tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                tMapView.setCenterPoint( 126.985302, 37.570841 );
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
        // "N서울타워" 버튼 클릭
        buttonMove1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 3번째 파라미터 생략 == 지도 이동 Animation 사용안함
                tMapView.setCenterPoint(126.988205, 37.551135);
            }
        });

        // "경복궁" 버튼 클릭
        buttonMove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3번째 파라미터 true == 지도 이동 Animation 사용
                tMapView.setCenterPoint(126.976998, 37.579600, true);
            }
        });
        // "확대" 버튼 클릭
        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomIn();
            }
        });
        // "축소" 버튼 클릭
        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomOut();
            }
        });
        // "ZoomLevel=10" 버튼 클릭
        buttonZoomLevel10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.setZoomLevel(10);
            }
        });

        /**
         * Dogwalker의 경로가 표시되도록 만들기.
         * */
        TMapPoint tMapPointStart = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)
        TMapPoint tMapPointEnd = new TMapPoint(37.551135, 126.988205); // N서울타워(목적지)
        //출발지를 받고, 목적지를 계속 받아 갱신하면서 그리는 쪽으로 하기.

        try {
            TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
            tMapPolyLine.setLineColor(Color.GREEN);
            tMapPolyLine.setLineWidth(2);
            tMapView.addTMapPolyLine("Line1", tMapPolyLine);
        }catch(Exception e) {
            e.printStackTrace();
        }


        /**
         * 풍선뷰 만들기
         * */
        TMapPoint tpoint = new TMapPoint(37.566413, 126.985003);

        mContext = this;
        MarkerOverlay marker = new MarkerOverlay(mContext, "custom", "marker");
        String strID = "TMapMarkerItem2";

        marker.setPosition(0.2f,0.2f);
        marker.getTMapPoint();
        marker.setID(strID);
        marker.setTMapPoint(new TMapPoint(tpoint.getLatitude(), tpoint.getLongitude()));

        tMapView.addMarkerItem2(strID, marker);


    }//onCreate


}//GpsMainActivity

