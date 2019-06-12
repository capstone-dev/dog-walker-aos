package ajou.ac.kr.teaming.activity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.vo.RegisterVO;

public class PetView extends AppCompatActivity {

    TextView nameText;
    TextView typeText;
    TextView ageText;
    Button MainButton;
    ImageView dogimage;
    Bitmap bitmap;
    RegisterVO registerVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);
        dogimage=(ImageView)findViewById(R.id.dogimage);

        nameText=(TextView) findViewById(R.id.nameText);
        typeText=(TextView)findViewById(R.id.typeText);
        ageText=(TextView)findViewById(R.id.ageText);
        MainButton=(Button)findViewById(R.id.MainButton);





        Intent intent =getIntent();

        registerVO=(RegisterVO) intent.getSerializableExtra("registerVO");
        String dogName=intent.getStringExtra("DogName");
        String dogtype=intent.getStringExtra("DogType");
        String dogage=intent.getStringExtra("DogAge");
        Bitmap dogimage=intent.getParcelableExtra("image");

        nameText.setText(dogName);
        typeText.setText(dogtype);
        ageText.setText(dogage);


        MainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(PetView.this, MainActivity.class);
                loginIntent.putExtra("registerVO",registerVO);


                PetView.this.startActivity(loginIntent);


            }
        });



    }
}
