package ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging;

public class NotificationModel {

    public String to;

    public Notification notification;

    public static class Notification{
        public String title;
        public String text;
    }
}
