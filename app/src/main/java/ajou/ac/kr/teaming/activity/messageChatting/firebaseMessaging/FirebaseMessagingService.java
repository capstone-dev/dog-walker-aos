package ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ajou.ac.kr.teaming.activity.messageChatting.MessageAdapter;
import ajou.ac.kr.teaming.vo.ChatDataVO;

public class FirebaseMessagingService {

    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference =firebaseDatabase.getReference("message");
    private ChildEventListener childEventListener;

    public void initFirebaseDatabase(MessageAdapter messageAdapter,String systemUserId) {

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatDataVO chatDataVO=dataSnapshot.getValue(ChatDataVO.class);
                chatDataVO.firebaseKey=dataSnapshot.getKey();

                messageAdapter.add(chatDataVO,systemUserId);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String firebaseKey=dataSnapshot.getKey();
                int count=messageAdapter.getCount();

                //메시지에 같은 firebasekey가 중복으로 있으면 삭제제
               for(int i=0;i<count;i++){
                    if(messageAdapter.getItem(i).firebaseKey.equals(firebaseKey)){
                        messageAdapter.remove(messageAdapter.getItem(i));
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    public void onClick(View v,String message,String userName){
        if(!TextUtils.isEmpty(message)){
            ChatDataVO chatDataVO=new ChatDataVO();
            chatDataVO.userName=userName;
            chatDataVO.message=message;
            chatDataVO.time=System.currentTimeMillis();
            databaseReference.push().setValue(chatDataVO);
        }
    }
}
