package ajou.ac.kr.teaming.activity.userCommunity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;

/**
 * 사용자 커뮤니티 게시글 리스트 recycler view
 */

public class UserCommunityThreadAdapter extends RecyclerView.Adapter<UserCommunityThreadAdapter.UserCommunityThreadViewHolder> {

    private ArrayList<UserCommunityThreadVO> userCommunityThreadVOArrayList=new ArrayList<>();
    private OnItemClickListener onItemClickListener;


    /**
     * 커뮤니티 내 게시판 하나 리스트 클릭시 발생 이벤트 처리 handle
     */
    public interface OnItemClickListener{
        void showThreadContentEvent(View view, UserCommunityThreadVO threadTitle);

    }

    public UserCommunityThreadAdapter(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public UserCommunityThreadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("TEST", "onCreate thread : ");
        return new UserCommunityThreadViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_community_thread,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommunityThreadViewHolder userCommunityThreadViewHolder, int i) {
        Log.d("TEST", "bind thread : ");

        UserCommunityThreadVO userCommunityThreadVO=userCommunityThreadVOArrayList.get(i);

        /**
         * 데이터 바인딩
         */
        userCommunityThreadViewHolder.userId.setText(userCommunityThreadVO.getUserId());
        userCommunityThreadViewHolder.threadTitle.setText(userCommunityThreadVO.getThreadTitle());
        userCommunityThreadViewHolder.userLocation.setText(userCommunityThreadVO.getUserLocation());
        userCommunityThreadViewHolder.threadNumber.setText(Integer.toString(userCommunityThreadVO.getThreadNumber()));
        userCommunityThreadViewHolder.threadDate.setText(userCommunityThreadVO.getThreadDate());


    }


    @Override
    public int getItemCount() {
        return userCommunityThreadVOArrayList.size();
    }

    public void addThread(ArrayList<UserCommunityThreadVO> userCommunityThreadVOList) {
        Log.d("TEST", "addThread: ");
        userCommunityThreadVOArrayList.addAll(userCommunityThreadVOList);
        notifyDataSetChanged();
    }

    class UserCommunityThreadViewHolder extends RecyclerView.ViewHolder{

        TextView userId;
        TextView threadTitle;
        TextView userLocation;
        TextView threadNumber;
        TextView threadDate;

        public UserCommunityThreadViewHolder(@NonNull View itemView) {
            super(itemView);
            threadTitle=itemView.findViewById(R.id.user_thread_title);
            userId=itemView.findViewById(R.id.user_id);
            userLocation=itemView.findViewById(R.id.user_location);
            threadNumber=itemView.findViewById(R.id.user_thread_number);
            threadDate=itemView.findViewById(R.id.user_thread_date);
        }

    }
}
