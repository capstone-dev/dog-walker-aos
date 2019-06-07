
package ajou.ac.kr.teaming.activity.messageChatting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import ajou.ac.kr.teaming.R;

import ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging.FirebaseMessagingService;
import ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging.NotificationModel;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.fcmToken.FcmService;
import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.FcmVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.ServiceVO;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageChattingMainActivity extends Activity {

    private FcmService fcmService = ServiceBuilder.create(FcmService.class);

    ListView messageListView;
    MessageAdapter messageAdapter;
    private RegisterVO registerVO;
    private DogwalkerListVO dogwalkerListVO;
    private ServiceVO serviceVO;
    private DogwalkerVO dogwalkerVO;
    private UserCommunityContentCommentVO userCommunityContentCommentVO;
    private UserCommunityThreadVO userCommunityThreadVO;
    private TextView userIdTextView;
    private String activityName;
    private Button submitService;
    private String oppenentId;
    private String oppenentToken;
    private String inputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chatting_main);

        //해당 사용자 등록 정보
        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");
        activityName = intent.getExtras().getString("activityName");

        submitService = findViewById(R.id.submit_service);

        //커뮤니티에서 메시지 연결시 해당 커뮤니티 게시글 댓글 사용자 ID 받아옴
        userIdTextView = (TextView) findViewById(R.id.user_id);

        if (activityName.equals("사용자커뮤니티")) {
            userCommunityContentCommentVO = (UserCommunityContentCommentVO) intent.getSerializableExtra("UserCommunityContentCommentVO");
            userCommunityThreadVO = (UserCommunityThreadVO) intent.getSerializableExtra("UserCommunityThreadVO");

            submitService.setVisibility(View.GONE);
            if (registerVO.getUserID().equals(userCommunityThreadVO.getUser_UserID())) {
                userIdTextView.setText(userCommunityContentCommentVO.getUser_UserID() + "님과의 채팅");
            } else {
                userIdTextView.setText(userCommunityThreadVO.getUser_UserID() + "님과의 채팅");
            }

        } else if (activityName.equals("실시간도그워커")) {
            dogwalkerListVO = (DogwalkerListVO) intent.getSerializableExtra("DogwalkerListVO");

            submitService.setVisibility(View.VISIBLE);
            if (registerVO.getUserID().equals(dogwalkerListVO.getDogwalkerID())) {
                userIdTextView.setText(dogwalkerListVO.getSelected() + "님과의 채팅");
            } else /*if (registerVO.getUserID().equals(dogwalkerListVO.getSelected()))*/ {
                userIdTextView.setText(dogwalkerListVO.getDogwalkerID() + "님과의 채팅");
            }
        } else if(activityName.equals("도그워커예약")){
            dogwalkerVO = (DogwalkerVO) intent.getSerializableExtra("DogwalkerVO");

            submitService.setVisibility(View.VISIBLE);
            if (registerVO.getUserID().equals(dogwalkerVO.getUserID())) {
                userIdTextView.setText( "님과의 채팅");
            } else /*if (registerVO.getUserID().equals(dogwalkerListVO.getSelected()))*/ {
                userIdTextView.setText(registerVO.getUserID() + "님과의 채팅");
            }
        } else if(activityName.equals("나의서비스")){
            serviceVO = (ServiceVO) intent.getSerializableExtra("ServiceVO");

            submitService.setVisibility(View.GONE);
            if (registerVO.getUserID().equals(serviceVO.getUser_UserID())) {
                userIdTextView.setText(serviceVO.getUser_DogwalkerID() + "님과의 채팅");
            } else /*if (registerVO.getUserID().equals(dogwalkerListVO.getSelected()))*/ {
                userIdTextView.setText(serviceVO.getUser_UserID() + "님과의 채팅");
            }
        }

        // 커스텀 어댑터 생성
        messageAdapter = new MessageAdapter(this, 0);
        messageListView = findViewById(R.id.listView1);
        messageListView.setAdapter(messageAdapter);

        //파이어 베이스 메시징 서비스 이벤트 처리
        if (activityName.equals("사용자커뮤니티")) {
            FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
            firebaseMessagingService.initFirebaseDatabase(messageAdapter, registerVO.getUserID(), userCommunityContentCommentVO.getCommentId());
            // <p > 메시지 전송 표시</p >
            findViewById(R.id.send_message).setOnClickListener(v -> {
                        EditText editText = (EditText) findViewById(R.id.message);
                        inputValue = editText.getText().toString();
                        editText.setText("");
                        //메시지 추가
                        if (registerVO.getUserID().equals(userCommunityThreadVO.getUser_UserID())) {
                            oppenentId = userCommunityContentCommentVO.getUser_UserID();
                            firebaseMessagingService.onClick(v, registerVO.getToken(), inputValue, registerVO.getUserID(),
                                    userCommunityContentCommentVO.getUser_UserID(), userCommunityContentCommentVO.getCommentId());
                        } else {
                            oppenentId = userCommunityThreadVO.getUser_UserID();
                            firebaseMessagingService.onClick(v, registerVO.getToken(), inputValue, registerVO.getUserID(),
                                    userCommunityThreadVO.getUser_UserID(), userCommunityContentCommentVO.getCommentId());
                        }
                        sendFcm(oppenentId);
                    }
            );
        } else if (activityName.equals("실시간도그워커")) {
            FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
            firebaseMessagingService.initFirebaseDatabase(messageAdapter, registerVO.getUserID(), dogwalkerListVO.getDogwalkerID());
            //메시지 전송 표시
            findViewById(R.id.send_message).setOnClickListener(v -> {
                        EditText editText = (EditText) findViewById(R.id.message);
                        String inputValue = editText.getText().toString();
                        editText.setText("");
                        //메시지 추가
                        if (registerVO.getUserID().equals(dogwalkerListVO.getDogwalkerID())) {
                            oppenentId = dogwalkerListVO.getSelected();
                            firebaseMessagingService.onClick(v, registerVO.getToken(), inputValue, registerVO.getUserID(),
                                    dogwalkerListVO.getSelected(), dogwalkerListVO.getSelected());
                        } else {
                            oppenentId = dogwalkerListVO.getDogwalkerID();
                            firebaseMessagingService.onClick(v, registerVO.getToken(), inputValue, registerVO.getUserID(),
                                    dogwalkerListVO.getDogwalkerID(), dogwalkerListVO.getDogwalkerID());
                        }
                        sendFcm(oppenentId);
                    }
            );
        }else if(activityName.equals("도그워커예약")){
            FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
            firebaseMessagingService.initFirebaseDatabase(messageAdapter, registerVO.getUserID(), dogwalkerVO.getUserID());//메시지 전송 표시
            findViewById(R.id.send_message).setOnClickListener(v -> {
                        EditText editText = (EditText) findViewById(R.id.message);
                        String inputValue = editText.getText().toString();
                        editText.setText("");
                        //메시지 추가
                        if (registerVO.getUserID().equals(dogwalkerVO.getUserID())) {
                            oppenentId = serviceVO.getUser_UserID();
                            firebaseMessagingService.onClick(v, registerVO.getToken(), inputValue, registerVO.getUserID(),
                                    serviceVO.getUser_UserID(), serviceVO.getUser_UserID());
                        } else {
                            oppenentId = serviceVO.getUser_DogwalkerID();
                            firebaseMessagingService.onClick(v, registerVO.getToken(), inputValue, registerVO.getUserID(),
                                    serviceVO.getUser_DogwalkerID(), serviceVO.getUser_DogwalkerID());
                        }
                        sendFcm(oppenentId);
                    }
            );
        } else if(activityName.equals("나의서비스")){
            FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
            firebaseMessagingService.initFirebaseDatabase(messageAdapter, registerVO.getUserID(), serviceVO.getUser_DogwalkerID());//메시지 전송 표시
            findViewById(R.id.send_message).setOnClickListener(v -> {
                        EditText editText = (EditText) findViewById(R.id.message);
                        String inputValue = editText.getText().toString();
                        editText.setText("");
                        //메시지 추가
                        if (registerVO.getUserID().equals(serviceVO.getUser_DogwalkerID())) {
                            oppenentId = serviceVO.getUser_UserID();
                            firebaseMessagingService.onClick(v, registerVO.getToken(), inputValue, registerVO.getUserID(),
                                    serviceVO.getUser_UserID(), serviceVO.getUser_UserID());
                        } else {
                            oppenentId = serviceVO.getUser_DogwalkerID();
                            firebaseMessagingService.onClick(v, registerVO.getToken(), inputValue, registerVO.getUserID(),
                                    serviceVO.getUser_DogwalkerID(), serviceVO.getUser_DogwalkerID());
                        }
                        sendFcm(oppenentId);
                    }
            );
        }
    }

    /**
     * 서비스 최종 에약 승인 클릭 이벤트 확인
     *
     * @param view
     */
    public void onClickServiceSubmit(View view) {
        Intent intent = new Intent(MessageChattingMainActivity.this, ServiceSubmitActivity.class);
        intent.putExtra("RegisterVO", registerVO);
        if (activityName.equals("사용자커뮤니티")) {
            intent.putExtra("userCommunityThreadVO", userCommunityThreadVO);
            intent.putExtra("UserCommunityContentCommentVO", userCommunityContentCommentVO);
            intent.putExtra("activityName", "사용자커뮤니티");
        } else if (activityName.equals("실시간도그워커")) {
            intent.putExtra("DogwalkerListVO", dogwalkerListVO);
            intent.putExtra("activityName", "실시간도그워커");
        }
        startActivity(intent);
    }

    /**
     * 상대반 토큰을 구해서 알림 보내주는 기능 이벤트 handle
     * @param oppenentId =상대방 아이디
     */
    public void sendFcm(String oppenentId) {

        //fcm token 받아와서 저장
        Call<FcmVO> call = fcmService.getFcmToken(oppenentId);
        call.enqueue(new Callback<FcmVO>() {
            @Override
            public void onResponse(Call<FcmVO> call, Response<FcmVO> response) {
                if (response.isSuccessful()) {
                    FcmVO fcmVO = response.body();
                    oppenentToken = fcmVO.getToken();
                    if (fcmVO != null) {
                        Log.d("TEST", oppenentToken);
                    }
                }
            }
            @Override
            public void onFailure(Call<FcmVO> call, Throwable t) {
                t.printStackTrace();
                Log.d("TEST", "통신 실패");

            }
        });

        Gson gson = new Gson();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = oppenentToken;
        notificationModel.notification.title = "Dog Walker";
        notificationModel.notification.text = inputValue;

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf8"),
                gson.toJson(notificationModel));

        Request request=new Request.Builder()
                .header("Content-Type","application/json")
                .addHeader("Authorization","key=AIzaSyByyaL03kFKh_rvZZ4S0cVqHD8Np7pWWOQ")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient=new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("TEST", "FCM 알림onFailure: ");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d("TEST", "FCM 알림 성공: ");

            }
        });
    }

    //뒤로가기 버튼
    public void onClickBackButton(View view) {
        finish();
    }
}
