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
import java.util.List;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;

/**
 * 특정 게시글에 대한 댓글 어댑터
 */
public class UserCommunityContentCommentAdapter extends RecyclerView.Adapter<UserCommunityContentCommentViewHolder> {

    private RegisterVO registerVO;
    private UserCommunityThreadVO userCommunityThreadVO;
    private OnItemClickListener onItemClickListener;
    private ArrayList<UserCommunityContentCommentVO> userCommunityContentCommentVOArrayList = new ArrayList<>();
    int buttonTurn;

    /**
     * 커뮤니티 내 게시판 댓글 연결 하나 클릭시 발생 이벤트 처리 handle
     */
    public interface OnItemClickListener {
        void matchMessageUserEvent(View view, UserCommunityContentCommentVO userCommunityContentCommentVO);

    }

    public UserCommunityContentCommentAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public UserCommunityContentCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserCommunityContentCommentViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_community_comment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommunityContentCommentViewHolder userCommunityContentCommentViewHolder, int i) {
        UserCommunityContentCommentVO userCommunityContentCommentVO = userCommunityContentCommentVOArrayList.get(i);

        userCommunityContentCommentViewHolder.userId.setText(userCommunityContentCommentVO.getUser_UserID());
        userCommunityContentCommentViewHolder.commentContent.setText(userCommunityContentCommentVO.getCommentContent());
        userCommunityContentCommentViewHolder.commentDate.setText(userCommunityContentCommentVO.getCommentDate().substring(0, 10) + " " +
                userCommunityContentCommentVO.getCommentDate().substring(11, 16));

        userCommunityContentCommentViewHolder.type = buttonTurn;

        //로그인한 사용자 정보와 해당 클릭한 게시글 작성자 정보가 동알할 시 댓글 사용자와 메시지 할 수 있는 버튼 visible
        if (registerVO.getUserID().equals(userCommunityThreadVO.getUser_UserID()) || registerVO.getUserID().equals(
                userCommunityContentCommentVO.getUser_UserID())) {
            userCommunityContentCommentViewHolder.connectMessage.setVisibility(View.VISIBLE);
        }  //동일
        else {
            userCommunityContentCommentViewHolder.connectMessage.setVisibility(View.INVISIBLE);
        }   //다를 때


        //테스트
        /* userCommunityContentCommentViewHolder.connectMessage.setVisibility(View.VISIBLE);*/
       /* if (userCommunityContentCommentViewHolder.type == 1) {
            userCommunityContentCommentViewHolder.connectMessage.setVisibility(View.VISIBLE);
        } else if (buttonTurn == 0) {
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
    public void addCommentList(ArrayList<UserCommunityContentCommentVO> userCommunityContentCommentVOList, RegisterVO registerVO,
                               UserCommunityThreadVO userCommunityThreadVO) {

        this.registerVO = registerVO;
        this.userCommunityThreadVO = userCommunityThreadVO;

        userCommunityContentCommentVOArrayList.addAll(userCommunityContentCommentVOList);
        notifyDataSetChanged();
    }
}

/**
 * <p>vidw holder에 댓글 form에 적합한 값들을 적용 </p>
 */
class UserCommunityContentCommentViewHolder extends RecyclerView.ViewHolder {

    TextView userId;
    TextView commentContent;
    TextView commentDate;
    Button connectMessage;
    int type;

    public UserCommunityContentCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        userId = itemView.findViewById(R.id.user_id);
        commentContent = itemView.findViewById(R.id.user_comment_content);
        commentDate = itemView.findViewById(R.id.user_comment_date);
        connectMessage = itemView.findViewById(R.id.message_button);
    }
}
