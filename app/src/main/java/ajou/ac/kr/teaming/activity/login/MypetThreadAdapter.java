package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class MypetThreadAdapter extends RecyclerView.Adapter<MypetThreadViewHolder>{




    private ArrayList<MyPetVO> myPetVOArrayList = new ArrayList<>();
    private MypetThreadAdapter.OnItemClickListener onItemClickListener;




    @Override
    public void onBindViewHolder(@NonNull MypetThreadViewHolder MypetThreadViewHolder ,int i) {
        MyPetVO myPetVO=myPetVOArrayList.get(i);
        MypetThreadViewHolder.dog_name.setText(myPetVO.getDog_name());
        MypetThreadViewHolder.dog_age.setText(myPetVO.getDog_age());
        MypetThreadViewHolder.dog_type.setText(myPetVO.getDog_species());
        MypetThreadViewHolder.constraintLayout.setOnClickListener(v->
                onItemClickListener.showThreadContentEvent(v,myPetVOArrayList.get(i)));

    }



    /**
     * 커뮤니티 내 게시판 하나 리스트 클릭시 발생 이벤트 처리 handle
     */

    public interface OnItemClickListener {

        void showThreadContentEvent(View view, MyPetVO myPetVO);

    }


    public MypetThreadAdapter(MypetThreadAdapter.OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;

    }




    @NonNull
    @Override
    public MypetThreadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MypetThreadViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mypet_listview_item, viewGroup, false));
    }



    @Override
    public int getItemCount() {
        return myPetVOArrayList.size();
    }



    public void addThread(ArrayList<MyPetVO> MyPetVOList) {
        myPetVOArrayList.addAll(MyPetVOList);
        notifyDataSetChanged();
    }
}




class MypetThreadViewHolder extends RecyclerView.ViewHolder {

    TextView dog_name;
    TextView dog_age;
    TextView dog_type;
    ImageView dogimage;
    ConstraintLayout constraintLayout;


    public MypetThreadViewHolder(@NonNull View itemview) {
        super(itemview);
        constraintLayout = itemview.findViewById(R.id.mypet_thread);
        dogimage=itemview.findViewById(R.id.dogimage);
        dog_name = itemview.findViewById(R.id.dog_name);
        dog_age = itemview.findViewById(R.id.dog_age);
        dog_type=itemview.findViewById(R.id.dog_type);
    }
}