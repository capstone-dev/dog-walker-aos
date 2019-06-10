package ajou.ac.kr.teaming.activity.messageChatting.messageList;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.ChatDataVO;
import ajou.ac.kr.teaming.vo.MessageVO;

public class MessageListAdapter extends RecyclerView.Adapter<UserMessageViewHolder> {

    private ArrayList<MessageVO> messageVOArrayList = new ArrayList<>();
    private OnMessageClickEventListener onMessageClickEventListener;
    private OnDeleteMessageClickListener onDeleteMessageClickListener;

    //메시지 함 내 메시지 하나 리스트 클릭시 발생 이벤트 처리 handle
    public interface OnMessageClickEventListener {
        void clickMessageEvent(View view, MessageVO messageVO);
    }

    //메시지 함 내 메시지 하나 리스트 삭제 버튼 발생 이벤트 처리 handle
    public interface OnDeleteMessageClickListener {
        void deleteMessageEvent(View view, MessageVO messageVO);

    }

    public MessageListAdapter(OnMessageClickEventListener onMessageClickEventListener,OnDeleteMessageClickListener onDeleteMessageClickListener) {
        this.onMessageClickEventListener = onMessageClickEventListener;
        this.onDeleteMessageClickListener=onDeleteMessageClickListener;
    }

    @NonNull
    @Override
    public UserMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserMessageViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_message, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserMessageViewHolder userMessageViewHolder, int i) {

        MessageVO messageVO = messageVOArrayList.get(i);
        //데이터 바인딩

        userMessageViewHolder.user_name.setText(messageVO.getUser_name());
        userMessageViewHolder.user_message.setText(messageVO.getUser_message());
        //userCommunityThreadViewHolder.userLocation.setText(userCommunityThreadVO.getUserLocation());

        userMessageViewHolder.user_message_date.setText(messageVO.getUser_message_date().substring(0, 10));

        //해당 게시글 constraintLayout 클릭시 발생 event handle
        userMessageViewHolder.constraintLayout.setOnClickListener(v ->
                onMessageClickEventListener.clickMessageEvent(v, messageVOArrayList.get(i)));

        //해당 게시글 삭제 클릭시 발생 event handle
        userMessageViewHolder.deleteButton.setOnClickListener(v ->
                onDeleteMessageClickListener.deleteMessageEvent(v, messageVOArrayList.get(i)));
    }

    @Override
    public int getItemCount() { return messageVOArrayList.size(); }

    //시스템 아이디와 chatDataVO.OPPONENT 아이디와 똑같을때 리스트에 추가
    public void add(ChatDataVO chatDataVO) {
        MessageVO messageVO = new MessageVO();
        messageVO.setUser_name(chatDataVO.userId);
        messageVO.setUser_message(chatDataVO.message);
        messageVO.setUser_message_date(chatDataVO.time);
        messageVOArrayList.add(messageVO);
        notifyDataSetChanged();
    }
}

class UserMessageViewHolder extends RecyclerView.ViewHolder {

    TextView user_name;
    TextView user_message;
    TextView user_message_date;
    ConstraintLayout constraintLayout;
    ImageButton deleteButton;

    public UserMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.message);
        user_name = itemView.findViewById(R.id.user_name);
        user_message = itemView.findViewById(R.id.user_message);
        user_message_date = itemView.findViewById(R.id.user_message_date);
        deleteButton = itemView.findViewById(R.id.delete_message_button);
    }

}
