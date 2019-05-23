package ajou.ac.kr.teaming.activity.messageChatting;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.ChatDataVO;

public class MessageAdapter extends ArrayAdapter<ChatDataVO> {

    private ArrayList<ChatDataVO> messageList=new ArrayList<>();
    private String systemUserId;

    public MessageAdapter(Context context, int resource) {
        super(context,resource);
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(ChatDataVO chatDataVO,String systemUserId) {
        messageList.add(chatDataVO);
        this.systemUserId=systemUserId;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public ChatDataVO getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView text;
        TextView idLeft;
        TextView idRight;
        TextView timeLeft;
        TextView timeRight;
        CustomHolder holder;
        LinearLayout layout;
        View viewRight;
        View viewLeft;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if (convertView == null) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chatting_message, parent, false);

            layout = (LinearLayout) convertView.findViewById(R.id.layout);
            text = (TextView) convertView.findViewById(R.id.text);
            viewRight = (View) convertView.findViewById(R.id.imageViewright);
            viewLeft = (View) convertView.findViewById(R.id.imageViewleft);
            idLeft=(TextView)convertView.findViewById(R.id.user_id_left);
            idRight=(TextView)convertView.findViewById(R.id.user_id_right);
            timeLeft=(TextView)convertView.findViewById(R.id.time_left);
            timeRight=(TextView)convertView.findViewById(R.id.time_right);


            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.m_TextView = text;
            holder.m_userId_Left=idLeft;
            holder.m_userId_Right=idRight;
            holder.m_time_Left=timeLeft;
            holder.m_time_Right=timeRight;
            holder.layout = layout;
            holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
            text = holder.m_TextView;
            idLeft=holder.m_userId_Left;
            timeLeft=holder.m_time_Left;
            timeRight=holder.m_time_Right;
            idRight=holder.m_userId_Right;
            layout = holder.layout;
            viewRight = holder.viewRight;
            viewLeft = holder.viewLeft;
        }

        // Text 등록
        text.setText(messageList.get(position).message);
        idLeft.setText(messageList.get(position).userId);
        idRight.setText(messageList.get(position).userId);
        timeLeft.setText(messageList.get(position).time);
        timeRight.setText(messageList.get(position).time);

        if (!messageList.get(position).userId.equals(systemUserId)) {
            text.setBackgroundResource(R.drawable.inbox2);
            idRight.setVisibility(View.GONE);
            timeRight.setVisibility(View.GONE);
            layout.setGravity(Gravity.LEFT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        } else if (messageList.get(position).userId.equals(systemUserId)) {
            text.setBackgroundResource(R.drawable.outbox2);
            layout.setGravity(Gravity.RIGHT);
            idLeft.setVisibility(View.GONE);
            timeLeft.setVisibility(View.GONE);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        } else  {
            text.setBackgroundResource(R.drawable.datebg);
            layout.setGravity(Gravity.CENTER);
            viewRight.setVisibility(View.VISIBLE);
            viewLeft.setVisibility(View.VISIBLE);
        }

        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(v -> {
            // 터치 시 해당 아이템 이름 출력
        });

        // 리스트 아이템을 길게 터치 했을때 이벤트 발생
        convertView.setOnLongClickListener(v -> {
            // 터치 시 해당 아이템 이름 출력
            return true;
        });

        return convertView;
    }
}


class CustomHolder {
    TextView m_TextView;
    TextView m_userId_Left;
    TextView m_userId_Right;
    TextView m_time_Left;
    TextView m_time_Right;
    LinearLayout layout;
    View viewRight;
    View viewLeft;
}
