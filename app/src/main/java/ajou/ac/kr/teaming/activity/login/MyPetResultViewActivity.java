package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.vo.MyPetVO;

public class MyPetResultViewActivity extends AppCompatActivity {




    MyPetVO myPetVO;
    TextView nameText;
    TextView typeText;
    TextView ageText;
    ImageView PetImageText;
    Bitmap bitmap;
    Button MainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pet_result_view);

        nameText=(TextView)findViewById(R.id.nameText);
        typeText=(TextView)findViewById(R.id.typeText);
        ageText=(TextView)findViewById(R.id.ageText);
        PetImageText=(ImageView)findViewById(R.id.DogwalkerImageText);
        MainButton=(Button)findViewById(R.id.MainButton);


        Intent vintent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        PetImageText.setImageBitmap(bitmap);
        myPetVO = (MyPetVO) vintent.getSerializableExtra("MyPetVO");
        nameText.setText(myPetVO.getDog_name());
        typeText.setText(myPetVO.getDog_species());
        ageText.setText(myPetVO.getDog_age());





MainButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent loginIntent = new Intent(MyPetResultViewActivity.this, MainActivity.class);
        MyPetResultViewActivity.this.startActivity(loginIntent);
    }
});










    }
}
