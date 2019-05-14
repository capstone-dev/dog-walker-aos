package ajou.ac.kr.teaming.activity.userCommunity.UserCommunityContent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;

/**
 * 특정 게시글에 대한 댓글 어댑터
 */
public class UserCommunityContentCommentAdapter extends RecyclerView.Adapter<UserCommunityContentCommentViewHolder>{

    private ArrayList<UserCommunityContentCommentVO> userCommunityContentCommentVOArrayList=new ArrayList<>();

    @NonNull
    @Override
    public UserCommunityContentCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserCommunityContentCommentViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_community_comment,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommunityContentCommentViewHolder userCommunityContentCommentViewHolder, int i) {
        UserCommunityContentCommentVO userCommunityContentCommentVO=userCommunityContentCommentVOArrayList.get(i);

        userCommunityContentCommentViewHolder.userId.setText(userCommunityContentCommentVO.getUserId());
        userCommunityContentCommentViewHolder.commentContent.setText(userCommunityContentCommentVO.getCommentContent());
        userCommunityContentCommentViewHolder.commentDate.setText(userCommunityContentCommentVO.getCommentDate().substring(0,10)+" "+
                userCommunityContentCommentVO.getCommentDate().substring(11,16));
    }

    @Override
    public int getItemCount() {
        return userCommunityContentCommentVOArrayList.size();
    }

    //댓글 리스트 adapter에 적용
    public void addCommentList(ArrayList<UserCommunityContentCommentVO> userCommunityContentCommentVOList) {
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

    public UserCommunityContentCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        userId=itemView.findViewById(R.id.user_id);
        commentContent=itemView.findViewById(R.id.user_comment_content);
        commentDate=itemView.findViewById(R.id.user_comment_date);
    }
}
