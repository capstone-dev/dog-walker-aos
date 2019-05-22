package ajou.ac.kr.teaming.activity.userCommunity.UserCommunityContent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;

/**
 * 특정 게시글에 대한 댓글 어댑터
 */
public class UserCommunityContentCommentAdapter extends RecyclerView.Adapter<UserCommunityContentCommentViewHolder>{

    private OnItemClickListener onItemClickListener;
    private ArrayList<UserCommunityContentCommentVO> userCommunityContentCommentVOArrayList=new ArrayList<>();
    private int type;

    /**
     * 커뮤니티 내 게시판 댓글 연결 하나 클릭시 발생 이벤트 처리 handle
     */
    public interface OnItemClickListener{
        void matchMessageUserEvent(View view, UserCommunityContentCommentVO userCommunityContentCommentVO);

    }

    public UserCommunityContentCommentAdapter(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @NonNull
    @Override
    public UserCommunityContentCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserCommunityContentCommentViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_community_comment,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommunityContentCommentViewHolder userCommunityContentCommentViewHolder, int i) {
        UserCommunityContentCommentVO userCommunityContentCommentVO=userCommunityContentCommentVOArrayList.get(i);

        userCommunityContentCommentViewHolder.userId.setText(userCommunityContentCommentVO.getUser_UserID());
        userCommunityContentCommentViewHolder.commentContent.setText(userCommunityContentCommentVO.getCommentContent());
        userCommunityContentCommentViewHolder.commentDate.setText(userCommunityContentCommentVO.getCommentDate().substring(0,10)+" "+
                userCommunityContentCommentVO.getCommentDate().substring(11,16));

        //테스트
        userCommunityContentCommentViewHolder.connectMessage.setVisibility(View.VISIBLE);
       /* if(type==1) {
            userCommunityContentCommentViewHolder.connectMessage.setVisibility(View.VISIBLE);
        }
        else{
            userCommunityContentCommentViewHolder.connectMessage.setVisibility(View.INVISIBLE);
        }*/

        //해당 댓글 연결 버튼 클릭시 발생 event handle
        userCommunityContentCommentViewHolder.connectMessage.setOnClickListener(v ->
                onItemClickListener.matchMessageUserEvent(v, userCommunityContentCommentVOArrayList.get(i)));
    }

    @Override
    public int getItemCount() {
        return userCommunityContentCommentVOArrayList.size();
    }

    //댓글 리스트 adapter에 적용
    public void addCommentList(ArrayList<UserCommunityContentCommentVO> userCommunityContentCommentVOList, int type) {
        this.type=type;
        Log.d("TEST", "addThreadList: ");
        userCommunityContentCommentVOArrayList.clear();
        userCommunityContentCommentVOArrayList.addAll(userCommunityContentCommentVOList);
        notifyDataSetChanged();
    }
}

/**
 * <p>vidw holder에 댓글 form에 적합한 값들을 적용 </p>
 */
class UserCommunityContentCommentViewHolder extends RecyclerView.ViewHolder{

    TextView userId;
    TextView commentContent;
    TextView commentDate;
    Button connectMessage;

    public UserCommunityContentCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        userId=itemView.findViewById(R.id.user_id);
        commentContent=itemView.findViewById(R.id.user_comment_content);
        commentDate=itemView.findViewById(R.id.user_comment_date);
        connectMessage=itemView.findViewById(R.id.message_button);
    }
}
