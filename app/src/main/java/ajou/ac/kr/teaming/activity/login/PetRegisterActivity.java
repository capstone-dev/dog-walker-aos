package ajou.ac.kr.teaming.activity.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.R;
import ajou.ac.kr.teaming.activity.MainActivity;
import ajou.ac.kr.teaming.activity.gps.PermissionManager;
import ajou.ac.kr.teaming.service.common.ServiceBuilder;
import ajou.ac.kr.teaming.service.login.LoginService;
import ajou.ac.kr.teaming.service.login.MyPetService;
import ajou.ac.kr.teaming.service.login.RegisterService;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetRegisterActivity extends AppCompatActivity {

    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private Uri imgUri, photoURI, albumURI;
    private String mCurrentPhotoPath;



    ImageView PetImage;
    TextView idText;
    EditText dognameText;
    EditText typeText;
    EditText dogageText;
    Button petRegisterButton;
    RegisterVO registerVO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_register);


        idText=(TextView) findViewById(R.id.idText);
        PetImage=(ImageView) findViewById(R.id.PetImage);
        dognameText=(EditText)findViewById(R.id.dognameText);
        typeText =(EditText)findViewById(R.id.typeText);
        dogageText=(EditText)findViewById(R.id.dogageText);
        petRegisterButton=(Button) findViewById(R.id.petRegisterButton);

        MyPetService myPetService = ServiceBuilder.create(MyPetService.class);


        Intent intent =getIntent();
        registerVO=(RegisterVO) intent.getSerializableExtra("RegisterVO");
        idText.setText(registerVO.getUserID());


        petRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid = registerVO.getUserID();
                String dogname = dognameText.getText().toString();
                String dog_species = typeText.getText().toString();
                String dog_age = dogageText.getText().toString();





                ImageView Mypet = findViewById(R.id.MyPetImage);

                Bitmap bitmap = ((BitmapDrawable) PetImage.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataArray = baos.toByteArray();


                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), dataArray);

                Map<String, RequestBody> mypet = new HashMap<>();
                mypet.put("dog_imagefile\"; filename=\"PetImage.png", fileBody);
                mypet.put("UserID", RequestBody.create(MediaType.parse("Text"), userid));
                mypet.put("dog_species", RequestBody.create(MediaType.parse("Text"), dog_species));
                mypet.put("dog_age", RequestBody.create(MediaType.parse("Text"), dog_age));
                mypet.put("dog_name", RequestBody.create(MediaType.parse("Text"), dogname));

                Call<MyPetVO> call=myPetService.myPet(mypet);
                call.enqueue(new Callback<MyPetVO>() {
                    @Override
                    public void onResponse(Call<MyPetVO> call, Response<MyPetVO> response) {

                        MyPetVO myPetVO = response.body();
                        Log.d("TEST", "통신 성공");


                        Intent intent = new Intent(PetRegisterActivity.this, MyPet2Activity.class);
                        intent.putExtra("MyPetVO", (Serializable) myPetVO);
                        intent.putExtra("image",dataArray);
                        startActivity(intent);



                    }

                    @Override
                    public void onFailure(Call<MyPetVO> call, Throwable t) {

                        Log.d("TEST", "통신 실패");


                    }
                });



            }
        });





        PermissionListener permissionListener=new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                Toast.makeText(PetRegisterActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                Toast.makeText(PetRegisterActivity.this, "권한실패" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("정상서비스를 받으시려면 권한을 승인해 주세요")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


        PetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    makeDialog();
            }
        });



    }



    public void makeDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(PetRegisterActivity.this, R.style.popupTheme);
        alt_bld.setTitle("사진업로드").setIcon(R.drawable.ic_local_see_black_24dp).setCancelable(false).setPositiveButton("사진촬영",

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        // 사진 촬영 클릭

                        Log.v("알림", "다이얼로그 > 사진촬영 선택");

                        takePhoto();

                    }

                }).setNeutralButton("앨범선택",

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int id) {

                        Log.v("알림", "다이얼로그 > 앨범선택 선택");

                        //앨범에서 선택

                        selectAlbum();

                    }

                }).setNegativeButton("취소   ",

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        Log.v("알림", "다이얼로그 > 취소 선택");

                        // 취소 클릭. dialog 닫기.

                        dialog.cancel();

                    }

                });

        AlertDialog alert = alt_bld.create();

        alert.show();

    }

    public void takePhoto() {

        // 촬영 후 이미지 가져옴

        String state = Environment.getExternalStorageState();

        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if (Environment.MEDIA_MOUNTED.equals(state)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (intent.resolveActivity(getPackageManager()) != null) {

                File photoFile = null;

                try {

                    photoFile = createImageFile();

                } catch (IOException e) {

                    e.printStackTrace();

                }

                if (photoFile != null) {

                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                    imgUri = providerURI;

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);

                    startActivityForResult(intent, FROM_CAMERA);

                }

            }

        } else {

            Log.v("알림", "저장공간에 접근 불가능");

            return;

        }


    }


    public File createImageFile() throws IOException {

        String imgFileName = System.currentTimeMillis() + ".jpg";

        File imageFile = null;

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ireh");


        if (!storageDir.exists()) {

            Log.v("알림", "storageDir 존재 x " + storageDir.toString());

            storageDir.mkdirs();

        }

        Log.v("알림", "storageDir 존재함 " + storageDir.toString());

        imageFile = new File(storageDir, imgFileName);

        mCurrentPhotoPath = imageFile.getAbsolutePath();


        return imageFile;

    }


    //앨범 선택 클릭

    public void selectAlbum() {

        //앨범에서 이미지 가져옴


        //앨범 열기

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);


        intent.setType("image/*");


        startActivityForResult(intent, FROM_ALBUM);

    }


    public void galleryAddPic() {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File f = new File(mCurrentPhotoPath);

        Uri contentUri = Uri.fromFile(f);

        mediaScanIntent.setData(contentUri);

        sendBroadcast(mediaScanIntent);

        Toast.makeText(this, "사진이 저장되었습니다", Toast.LENGTH_SHORT).show();

    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_OK) {

            return;

        }

        switch (requestCode) {

            case FROM_ALBUM: {

                //앨범에서 가져오기

                if (data.getData() != null) {

                    try {

                        File albumFile = null;

                        albumFile = createImageFile();


                        photoURI = data.getData();

                        albumURI = Uri.fromFile(albumFile);


                        galleryAddPic();

                       PetImage.setImageURI(photoURI);

                        //cropImage();

                    } catch (Exception e) {

                        e.printStackTrace();

                        Log.v("알림", "앨범에서 가져오기 에러");

                    }

                }

                break;

            }

            case FROM_CAMERA: {

                //카메라 촬영

                try {

                    Log.v("알림", "FROM_CAMERA 처리");

                    galleryAddPic();

                   PetImage.setImageURI(imgUri);

                } catch (Exception e) {

                    e.printStackTrace();

                }

                break;

            }

        }

    }






}





