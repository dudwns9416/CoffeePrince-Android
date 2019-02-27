package com.sc.coffeeprince.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.model.Cafe;
import com.sc.coffeeprince.model.ResponseResult;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.repository.CafeRepository;
import com.sc.coffeeprince.repository.UserRepository;
import com.sc.coffeeprince.sqlite.UserHelper;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;

import static com.sc.coffeeprince.util.WsConfig.GET_IMAGE;

public class CafeUpdateActivity extends AppCompatActivity {
    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imageViewCafeImageInsert)
    ImageView imageViewCafeImageInsert;

    @BindView(R.id.btnCafeInfoSendToServer)
    Button btnCafeInfoSendToServer;

    @BindView(R.id.inputCafeName)
    EditText inputCafeName;

    @BindView(R.id.inputCafeContent)
    EditText inputCafeContent;

    @BindView(R.id.inputCafeAddress)
    EditText inputCafeAddress;

    @BindView(R.id.inputCafeTel)
    EditText inputCafeTel;

    @BindView(R.id.ckbSetHide)
    CheckBox ckbSetHide;

    private File file;
    private CafeRepository cafeRepository = new CafeRepository();
    private UserRepository userRepository = new UserRepository();
    private Cafe cafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_create);
        ButterKnife.bind(this);

        setTitle("카페 수정");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCafeInfoSendToServer.setText("수정하기");
        initCafe();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @OnClick(R.id.imageViewCafeImageInsert)
    public void showImagePicker() {
        permissionCheck();
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        try {
                            file = new File(uri.getPath());
                            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageViewCafeImageInsert.setImageBitmap(bm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());
    }

    @OnClick(R.id.btnCafeInfoSendToServer)
    public void sendCafe(View view) {
        ResponseResult responseResult = registerCafe();
        Snackbar.make(view, responseResult.getRespMsg(), Snackbar.LENGTH_SHORT).show();
        if (responseResult.getRespCode().equals(SUCCESS)) {
            if (file==null) {
                cafeRepository.update(updateCafe(cafe.getImage()));
            } else {
                String fileName = cafeRepository.addImage(file);
                cafeRepository.update(updateCafe(fileName));
            }
        }

    }

    private void initCafe() {
        User user = userRepository.find(getUserId());
        cafe = user.getCafeList().get(0);
        inputCafeName.setText(cafe.getName());
        inputCafeContent.setText(cafe.getContent());
        inputCafeAddress.setText(cafe.getAddress());
        inputCafeTel.setText(cafe.getTel());
        ckbSetHide.setChecked(cafe.isHide());
        Glide.with(this).load(GET_IMAGE + cafe.getImage())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewCafeImageInsert);
    }

    private ResponseResult registerCafe() {
        ResponseResult responseResult = null;

        if (inputCafeName.getText().toString().equals("")) {
            responseResult = new ResponseResult().setRespMsg("카페 이름을 입력하세요.").setRespCode(FAIL);
        } else if (inputCafeContent.getText().toString().equals("")) {
            responseResult = new ResponseResult().setRespMsg("카페 소개를 입력하세요.").setRespCode(FAIL);
        } else if (inputCafeAddress.getText().toString().equals("")) {
            responseResult = new ResponseResult().setRespMsg("카페 주소를 입력하세요").setRespCode(FAIL);
        } else if (inputCafeTel.getText().toString().equals("")) {
            responseResult = new ResponseResult().setRespMsg("카페 전화번호를 입력하세요").setRespCode(FAIL);
        } else {
            responseResult = new ResponseResult().setRespMsg("카페 등록이 완료 되었습니다").setRespCode(SUCCESS);
        }
        return responseResult;
    }

    private Cafe updateCafe(String fileName) {
        cafe.setName(inputCafeName.getText().toString());
        cafe.setContent(inputCafeContent.getText().toString());
        cafe.setBirth("2017");
        cafe.setAddress(inputCafeAddress.getText().toString());
        cafe.setTel(inputCafeTel.getText().toString());
        if (ckbSetHide.isChecked()) {
            cafe.setHide(true);
        } else {
            cafe.setHide(false);
        }
        cafe.setImage(fileName);
        return cafe;
    }

    private String getUserId() {
        UserHelper userHelper = new UserHelper(this);
        User user = userHelper.selectUser();
        return user.getId();
    }

    private void permissionCheck() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("권한이 필요합니다")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
