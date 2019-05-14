/*package ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging;

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
        String title=remoteMessage.getData().get()
    }
}*/
