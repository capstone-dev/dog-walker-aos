package ajou.ac.kr.teaming.activity.gps;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.DogwalkerVo;

/**
 * 사용자 커뮤니티 게시글 리스트 recycler view
 */

public class RealTimeDogwalkerListAdapter extends RecyclerView.Adapter<RealTimeDogWalkerViewHolder> {

    private ArrayList<DogwalkerListVO> dogwalkerVoArrayList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;


    /**
     * 커뮤니티 내 게시판 하나 리스트 클릭시 발생 이벤트 처리 handle
     */
    public interface OnItemClickListener {
        void chooseDogWalkerEvent(View view, DogwalkerListVO dogwalkerListVO);

    }

    public RealTimeDogwalkerListAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RealTimeDogWalkerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("TEST", "onCreate thread : ");
        return new RealTimeDogWalkerViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.realtime_dogwalker, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RealTimeDogWalkerViewHolder realTimeDogWalkerViewHolder, int i) {

        DogwalkerListVO dogwalkerListVo = dogwalkerVoArrayList.get(i);
        /**
         * 데이터 바인딩
         */
        realTimeDogWalkerViewHolder.dogwalkerName.setText(dogwalkerListVo.getDogwalkerID());
        realTimeDogWalkerViewHolder.bigCity.setText(dogwalkerListVo.getDogwalkerBigcity());
        realTimeDogWalkerViewHolder.smallCity.setText(dogwalkerListVo.getDogwalkerSmallcity());
        realTimeDogWalkerViewHolder.gender.setText(dogwalkerListVo.getDogWalkerGender());
        //해당 게시글 constraintLayout 클릭시 발생 event handle
        if(!dogwalkerListVo.getSelect().equals("0")){
            realTimeDogWalkerViewHolder.constraintLayout.setBackgroundColor(Color.rgb(139,137,137));
        }

        realTimeDogWalkerViewHolder.constraintLayout.setOnClickListener(v ->{
                onItemClickListener.chooseDogWalkerEvent(v, dogwalkerVoArrayList.get(i));
                realTimeDogWalkerViewHolder.constraintLayout.setBackgroundColor(Color.rgb(139,137,137));
        });
    }

    @Override
    public int getItemCount() {
        return dogwalkerVoArrayList.size();
    }

    public void addThread(ArrayList<DogwalkerListVO> dogwalkerVoList) {
        Log.d("TEST", "addThread: ");
        dogwalkerVoArrayList.addAll(dogwalkerVoList);
        notifyDataSetChanged();
    }
}


/**
 * <p>vidw holder에 게시글 form에 적합한 값들을 적용 </p>
 */
class RealTimeDogWalkerViewHolder extends RecyclerView.ViewHolder {

    TextView dogwalkerName;
    TextView bigCity;
    TextView smallCity;
    TextView gender;
    ConstraintLayout constraintLayout;
    String selected;

    public RealTimeDogWalkerViewHolder(@NonNull View itemView) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.dog_walker);
        dogwalkerName = itemView.findViewById(R.id.dog_walker_name);
        bigCity = itemView.findViewById(R.id.big_city);
        smallCity = itemView.findViewById(R.id.small_city);
        gender = itemView.findViewById(R.id.dogwalker_gender);
    }
}
