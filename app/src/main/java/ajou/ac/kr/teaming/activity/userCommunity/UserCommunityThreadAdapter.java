package ajou.ac.kr.teaming.activity.userCommunity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;

/**
 * 사용자 커뮤니티 게시글 리스트 recycler view
 */

public class UserCommunityThreadAdapter extends RecyclerView.Adapter<UserCommunityThreadAdapter.UserCommunityThreadViewHolder> {

    private ArrayList<String> threadList=new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    /**
     * 커뮤니티 내 게시판 하나 리스트 클릭시 발생 이벤트 처리 handle
     */
    public interface OnItemClickListener{
        void showThreadContentEvent(View view, String threadTitle);

    }

    public UserCommunityThreadAdapter(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public UserCommunityThreadAdapter.UserCommunityThreadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserCommunityThreadViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_community_thread,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommunityThreadAdapter.UserCommunityThreadViewHolder userCommunityThreadViewHolder, int i) {
        userCommunityThreadViewHolder.setContext().setText(threadList.get(i));
        userCommunityThreadViewHolder.setContext().setOnClickListener(
                v->onItemClickListener.showThreadContentEvent(v,threadList.get(i)));
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    class UserCommunityThreadViewHolder extends RecyclerView.ViewHolder{

        public UserCommunityThreadViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView setContext(){
            return itemView.findViewById(R.id.user_thread_title);
        }
    }
}
