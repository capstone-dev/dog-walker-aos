package ajou.ac.kr.teaming.activity.login;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.DogwalkerVO;


public class DogwalkerThreadAdater extends RecyclerView.Adapter<DogwalkerThreadViewHolder> {



    private ArrayList<DogwalkerVO> DogwalkerVOArrayList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;


    /**
     * 커뮤니티 내 게시판 하나 리스트 클릭시 발생 이벤트 처리 handle
     */

    public interface OnItemClickListener {

        void showThreadContentEvent(View view, DogwalkerVO dogwalkerVO);

    }


    public DogwalkerThreadAdater(DogwalkerThreadAdater.OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;

    }




    @NonNull
    @Override
    public DogwalkerThreadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DogwalkerThreadViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_result_listview_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DogwalkerThreadViewHolder dogwalkerThreadViewHolder, int i) {
        DogwalkerVO dogwalkerVO=DogwalkerVOArrayList.get(i);
        dogwalkerThreadViewHolder.user_name.setText(dogwalkerVO.getUserID());
        dogwalkerThreadViewHolder.user_info.setText(dogwalkerVO.getUserInfo());
        dogwalkerThreadViewHolder.constraintLayout.setOnClickListener(view -> onItemClickListener.showThreadContentEvent(view,DogwalkerVOArrayList.get(i)));

    }

    @Override
    public int getItemCount() {
        return DogwalkerVOArrayList.size();
    }



    public void addThread(ArrayList<DogwalkerVO> dogwalkerVOList) {
        DogwalkerVOArrayList.addAll(dogwalkerVOList);
        notifyDataSetChanged();
    }
}




        class DogwalkerThreadViewHolder extends RecyclerView.ViewHolder {

            TextView user_name;
            TextView user_info;
            ConstraintLayout constraintLayout;

            public DogwalkerThreadViewHolder(@NonNull View itemView) {
                super(itemView);
                constraintLayout = itemView.findViewById(R.id.Dogwalker_list);
                user_name = itemView.findViewById(R.id.user_name);
                user_info = itemView.findViewById(R.id.user_info);
            }
        }