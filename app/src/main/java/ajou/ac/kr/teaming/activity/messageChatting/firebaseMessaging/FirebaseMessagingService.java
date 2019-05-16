package ajou.ac.kr.teaming.activity.messageChatting.firebaseMessaging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ajou.ac.kr.teaming.activity.messageChatting.MessageAdapter;

public class FirebaseMessagingService {

    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference =firebaseDatabase.getReference("message");
    private ChildEventListener childEventListener;
    private String message;

    public FirebaseMessagingService(String message) {
        this.message = message;
    }

    public void initFirebaseDatabase(MessageAdapter messageAdapter) {

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String message = dataSnapshot.getValue(String.class);
                messageAdapter.add(message, 1);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String message=dataSnapshot.getValue(String.class);
         /*       messageAdapter.remove(message);*/
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

    public void onClick(View v){
        if(!TextUtils.isEmpty(message)){
            databaseReference.push().setValue(message);
        }
    }
}
