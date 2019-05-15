/*
package ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token){
        super.onNewToken(token);
        Log.e("TEST", "onNewToken: "+token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        if (remoteMessage!=null&&remoteMessage.getData().size()>0){
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String title=remoteMessage.getData().get("title");
        String message=remoteMessage.getData().get("message");


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            String channel="채널";
            String channelName="채널명";


            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel=new NotificationChannel(channel,channelName,NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("채널에 대한 설명");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(false);
            notificationChannel.setVibrationPattern(new long[]{100,200,100,200});
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}
*/
