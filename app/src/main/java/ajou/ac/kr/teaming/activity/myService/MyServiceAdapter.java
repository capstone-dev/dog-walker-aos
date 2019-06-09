package ajou.ac.kr.teaming.activity.myService;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.vo.ServiceVO;

public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceViewHolder> {

    private ArrayList<ServiceVO> serviceVOArrayList=new ArrayList<>();
    private OnDeleteItemClickListener onDeleteItemClickListener;
    private OnMessageItemClickListener onMessageItemClickListener;
    private OnMyServiceClickListener onMyServiceClickListener;

    /**
     * 사용자 서비스 하나 리스트 클릭시 발생 이벤트 처리 handle
     */
    public interface OnDeleteItemClickListener{
        void deleteMyServiceEvent(View view, ServiceVO serviceVO);
    }


    public interface OnMessageItemClickListener{
        void contactMessageEvent(View view, ServiceVO serviceVO);
    }

    public interface OnMyServiceClickListener{
        void clickMyServiceEvent(View view, ServiceVO serviceVO);
    }

    public MyServiceAdapter(OnDeleteItemClickListener onDeleteItemClickListener,OnMessageItemClickListener onMessageItemClickListener,OnMyServiceClickListener onMyServiceClickListener){
        this.onDeleteItemClickListener=onDeleteItemClickListener;
        this.onMessageItemClickListener=onMessageItemClickListener;
        this.onMyServiceClickListener=onMyServiceClickListener;
    }

    @NonNull
    @Override
    public MyServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyServiceViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_service,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceViewHolder myServiceViewHolder, int i) {
        ServiceVO serviceVO=serviceVOArrayList.get(i);
        /**
         * 데이터 바인딩
         */
        myServiceViewHolder.dogWalkerId.setText(serviceVO.getUser_DogwalkerID());
        myServiceViewHolder.serviceLocation.setText(serviceVO.getServiceLocation());
        myServiceViewHolder.serviceWalkingTime.setText(serviceVO.getWalkingTime());

        myServiceViewHolder.constraintLayout.setOnClickListener(v ->
                onMyServiceClickListener.clickMyServiceEvent(v, serviceVOArrayList.get(i)));
        //해당 서비스  삭제 버튼 클릭시 발생 event handle
        myServiceViewHolder.deleteService.setOnClickListener(v ->
                onDeleteItemClickListener.deleteMyServiceEvent(v, serviceVOArrayList.get(i)));

        //해당 서비스 채팅 버튼 클릭시 발생 event handle
        myServiceViewHolder.contactMessage.setOnClickListener(v ->
                onMessageItemClickListener.contactMessageEvent(v, serviceVOArrayList.get(i)));
    }

    @Override
    public int getItemCount() {
        return serviceVOArrayList.size();
    }

    public void addService(ArrayList<ServiceVO> serviceVOS) {
        serviceVOArrayList.addAll(serviceVOS);
        notifyDataSetChanged();
    }

    public void updateService(){
        serviceVOArrayList.clear();
        notifyDataSetChanged();
    }
}

/**
 * <p>vidw holder에 게시글 form에 적합한 값들을 적용 </p>
 */
class MyServiceViewHolder extends RecyclerView.ViewHolder{

    ConstraintLayout constraintLayout;
    TextView dogWalkerId;
    TextView serviceLocation;
    TextView serviceWalkingTime;
    ImageButton deleteService;
    ImageButton contactMessage;

    public MyServiceViewHolder(@NonNull View itemView) {
        super(itemView);
        constraintLayout=itemView.findViewById(R.id.service);
        dogWalkerId=itemView.findViewById(R.id.dog_walker_id);
        serviceLocation=itemView.findViewById(R.id.service_location);
        serviceWalkingTime=itemView.findViewById(R.id.service_walking_time);
        deleteService=itemView.findViewById(R.id.delete_service_button);
        contactMessage=itemView.findViewById(R.id.message_service_button);
    }

}
