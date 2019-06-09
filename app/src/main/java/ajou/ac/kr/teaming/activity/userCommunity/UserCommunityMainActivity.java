package ajou.ac.kr.teaming.activity.userCommunity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ajou.ac.kr.teaming.activity.userCommunity.UserCommunityContent.UserCommunityContentActivity;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.service.userCommunity.UserCommunityService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p> 사용자 커뮤니티 intent Mainactivity </p>
 */
public class UserCommunityMainActivity extends AppCompatActivity implements
        UserCommunityThreadAdapter.OnItemClickListener, UserCommunityThreadAdapter.OnModifyThreadClickListener {

    private UserCommunityService userCommunityService = ServiceBuilder.create(UserCommunityService.class);
    private RecyclerView userthreadView;
    private UserCommunityThreadAdapter userCommunityThreadAdapter;
    private RegisterVO registerVO;
    private Spinner communitySelect;
    private String searchForm;

    private ArrayAdapter<CharSequence> communitySelectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_community_main);

        userthreadView = findViewById(R.id.user_community_thread_list);
        userthreadView.setLayoutManager(new LinearLayoutManager(this));

        userCommunityThreadAdapter = new UserCommunityThreadAdapter(this::showThreadContentEvent,this::modifiyThreadEvent);
        userthreadView.setAdapter(userCommunityThreadAdapter);
        setUserthreadList();

        communitySelectAdapter = ArrayAdapter.createFromResource(this, R.array.community_select, R.layout.spinner_item);
        communitySelectAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        communitySelect = (Spinner) findViewById(R.id.search_text);
        communitySelect.setAdapter(communitySelectAdapter);

        Intent intent = getIntent();
        registerVO = (RegisterVO) intent.getSerializableExtra("RegisterVO");
    }

    /**
     * <p> 서버로부터 커뮤니티 게시글 목록을 읽어들여 리스트에 저장 후 adapter에 적용 </p>
     */
    public void setUserthreadList() {

        Call<List<UserCommunityThreadVO>> request = userCommunityService.getThread();
        request.enqueue(new Callback<List<UserCommunityThreadVO>>() {
            @Override
            public void onResponse(Call<List<UserCommunityThreadVO>> call, Response<List<UserCommunityThreadVO>> response) {

                List<UserCommunityThreadVO> userCommunityThreadVOs = response.body();
                // 성공시
                ArrayList<UserCommunityThreadVO> userCommunityThreadList = new ArrayList<>();
                if (userCommunityThreadVOs != null) {
                    for (UserCommunityThreadVO userCommunityThreadVO : userCommunityThreadVOs) {
                        //서버로 부터 읽어드린 게시글 리스트를 전부 adapter 에 저장
                        userCommunityThreadList.add(userCommunityThreadVO);
                        Log.d("TEST", "onResponse: " + userCommunityThreadVO.getThreadTitle());
                    }
                    userCommunityThreadAdapter.addThread(userCommunityThreadList,registerVO.getUserID());
                }
                Log.d("TEST", "게시글 통신 성공");
            }

            @Override
            public void onFailure(Call<List<UserCommunityThreadVO>> call, Throwable t) {
                //실패시
                Log.d("TEST", "게시글 통신 실패");
            }
        });
    }

    /**
     * <p>
     * 특정 커뮤니티 게시글을 클릭하게 되면
     * 해당 게시글에 내한 내용을 확인할 수 있게 되는 eventhandler </p>
     * @param view 현재 사용자 커뮤니티 MainActivity
     * @Param userCommunityThreadVO 해당 게시글에 대한 정보를 포함하고 있는 객체
     * 팝업 창으로 게시글 아이디 넘겨준다.
     */
    public void showThreadContentEvent(View view, UserCommunityThreadVO userCommunityThreadVO) {

        Intent intent = new Intent(UserCommunityMainActivity.this, UserCommunityContentActivity.class);
        intent.putExtra("userCommunityThreadVO", userCommunityThreadVO);
        intent.putExtra("registerVO", registerVO);

        startActivity(intent);
    }

    /**
     * 등록하게 되면 넘어가는 팝업 창 생성
     * @param view 현재 사용자 커뮤니티 MainActivity
     */
    public void onClickThreadRegister(View view) {
        Intent intent = new Intent(UserCommunityMainActivity.this, UserCommunityThreadRegisterActivity.class);
        intent.putExtra("work","등록");
        intent.putExtra("registerVO", registerVO);
        startActivity(intent);
    }

    /**
     * 특정 검색어로 게시글 조회 시 Event handle
     * @param view
     */
    public void onClickSearch(View view) {

        searchForm = ((EditText) findViewById(R.id.thread_search_edit_text)).getText().toString();
        String condition = communitySelect.getSelectedItem().toString();
        if(checkSearchForm(condition,searchForm)) {
            Call<List<UserCommunityThreadVO>> request = null;

            if (condition.equals("아이디")) {
                request = userCommunityService.getUser_UserIDThread(searchForm);
                Log.e("TEST", "onClickSearch: " + searchForm);
            } else if (condition.equals("제목")) {
                request = userCommunityService.getThreadTitleThread(searchForm);
            } else if (condition.equals("위치")) {
                request = userCommunityService.getUserLocationThread(searchForm);
            }

            request.enqueue(new Callback<List<UserCommunityThreadVO>>() {
                @Override
                public void onResponse(Call<List<UserCommunityThreadVO>> call, Response<List<UserCommunityThreadVO>> response) {
                    List<UserCommunityThreadVO> userCommunityThreadVOs = response.body();
                    // 성공시
                    ArrayList<UserCommunityThreadVO> userCommunityThreadList = new ArrayList<>();
                    if (userCommunityThreadVOs != null) {
                        userCommunityThreadAdapter.deleteThread();
                        for (UserCommunityThreadVO userCommunityThreadVO : userCommunityThreadVOs) {
                            //서버로 부터 읽어드린 게시글 리스트를 전부 adapter 에 저장
                            userCommunityThreadList.add(userCommunityThreadVO);
                            Log.d("TEST", "onResponse: " + userCommunityThreadVO.getThreadTitle());
                        }
                        userCommunityThreadAdapter.addThread(userCommunityThreadList, registerVO.getUserID());
                    }
                    Log.d("TEST", "게시글 통신 성공");
                }

                @Override
                public void onFailure(Call<List<UserCommunityThreadVO>> call, Throwable t) {
                    //실패시
                    Log.d("TEST", "게시글 통신 실패");
                }
            });
        }
    }

    /**
     * 게시글 수정 버튼을 누르면 수정 이벤트 handle
     * @param view
     * @param userCommunityThreadVO 수정을 하겠다고 선택한 게시글
     */
    @Override
    public void modifiyThreadEvent(View view, UserCommunityThreadVO userCommunityThreadVO) {
        Call<UserCommunityThreadVO> request = userCommunityService.selectThread(userCommunityThreadVO.getThreadId());
        request.enqueue(new Callback<UserCommunityThreadVO>() {
            @Override
            public void onResponse(Call<UserCommunityThreadVO> call, Response<UserCommunityThreadVO> response) {
                // 성공시
                if (response.isSuccessful()) {
                    UserCommunityThreadVO userCommunityThreadVO1 = response.body();
                    //테스트 확인 log값
                    if (userCommunityThreadVO1 != null) {
                        Log.d("TEST", userCommunityThreadVO1.getContent());
                    }
                }
                Log.d("TEST", "수정 성공 ");
            }
            @Override
            public void onFailure(Call<UserCommunityThreadVO> call, Throwable t) {
                //실패시
                Log.d("TEST", "수정 실패"+t.toString());
            }
        });
        Intent intent = new Intent(UserCommunityMainActivity.this, UserCommunityThreadRegisterActivity.class);
        intent.putExtra("UserCommunityThreadVO",userCommunityThreadVO);
        intent.putExtra("registerVO", registerVO);
        intent.putExtra("work","수정");
        startActivity(intent);
    }

    /**
     * 등록 폼이 형식에 맞는지 검증하는 작업
     * 형식에 맞다면 1반환
     * 틀리다면 0 반환
     * @return
     */
    private Boolean checkSearchForm(String condition,String searchForm){

        if(condition.equals("검색어")){
            Toast.makeText(this, "조건을 선택하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(searchForm.length()==0){
            Toast.makeText(this, "검색어에 1자 이상 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
