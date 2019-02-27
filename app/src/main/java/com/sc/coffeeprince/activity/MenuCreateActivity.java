package com.sc.coffeeprince.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.model.GroupMenu;
import com.sc.coffeeprince.model.Menus;
import com.sc.coffeeprince.model.ResponseResult;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.repository.GroupMenuRepository;
import com.sc.coffeeprince.repository.MenusRepository;
import com.sc.coffeeprince.repository.UserRepository;
import com.sc.coffeeprince.sqlite.UserHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;

public class MenuCreateActivity extends AppCompatActivity {

    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imageViewMenuImageInsert)
    ImageView imageViewMenuImageInsert;

    @BindView(R.id.inputMenuName)
    EditText inputMenuName;

    @BindView(R.id.inputMenuPrice)
    EditText inputMenuPrice;

    @BindView(R.id.inputMenuContent)
    EditText inputMenuContent;

    @BindView(R.id.spinnerGroupMenu)
    Spinner spinnerGroupMenu;

    @BindView(R.id.btnMenuCreate)
    Button btnMenuCreate;

    private File file;
    private GroupMenuRepository groupMenuRepository = new GroupMenuRepository();
    private MenusRepository menusRepository = new MenusRepository();
    private UserRepository userRepository = new UserRepository();
    private List<GroupMenu> groupMenus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_create);
        ButterKnife.bind(this);
        setTitle("메뉴 등록");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initSpinner();
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


    @OnClick(R.id.btnMenuCreate)
    void menuCreate(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        ResponseResult responseResult = getResponseResult();
        Snackbar.make(view,responseResult.getRespMsg(),Snackbar.LENGTH_SHORT).show();
        if (responseResult.getRespCode() == SUCCESS){
            String resultImage = menusRepository.addImage(file);
            Menus menus = new Menus();
            menus.setGroupmenusId(groupMenus.get(spinnerGroupMenu.getSelectedItemPosition()).getId());
            menus.setName(inputMenuName.getText().toString());
            menus.setPrice(Integer.parseInt(inputMenuPrice.getText().toString()));
            menus.setContent(inputMenuContent.getText().toString());
            menus.setImage(resultImage);
            menusRepository.add(menus);
        }
    }

    @OnClick(R.id.imageViewMenuImageInsert)
    void setImageViewMenuImageInsert() {
        permissionCheck();
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        try {
                            file = new File(uri.getPath());
                            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageViewMenuImageInsert.setImageBitmap(bm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());
    }

    private ResponseResult getResponseResult() {
        ResponseResult responseResult = null;
        if (file == null){
            responseResult = new ResponseResult().setRespMsg("사진을 등록해 주세요").setRespCode(FAIL);
        }
        else if (inputMenuName.getText().toString().equals("")) {
            responseResult = new ResponseResult().setRespMsg("메뉴 이름을 입력하세요").setRespCode(FAIL);
        }
        else if (inputMenuPrice.getText().toString().equals("")) {
            responseResult = new ResponseResult().setRespMsg("메뉴 가격을 입력하세요").setRespCode(FAIL);
        }
        else if (isNumber(inputMenuPrice.getText().toString())) {
            responseResult = new ResponseResult().setRespMsg("메뉴 가격에 숫자를 입력하세요").setRespCode(FAIL);
        }
        else if (inputMenuContent.getText().toString().equals("")) {
            responseResult = new ResponseResult().setRespMsg("메뉴 소개를 입력하세요").setRespCode(FAIL);
        }else {
            responseResult = new ResponseResult().setRespMsg("메뉴 등록이 완료 되었습니다").setRespCode(SUCCESS);
        }
        return responseResult;
    }

    private void initSpinner() {
        int cafeId = getCafeId();
        if (cafeId <= 0) {

        }
        groupMenus = groupMenuRepository.finds(cafeId);
        if(groupMenus == null){
            Snackbar.make(getCurrentFocus(),"메뉴 분류를 생성해주세요",Snackbar.LENGTH_SHORT);
            this.finish();
        }
        List<String> groupMenuName = new ArrayList<>();
        for (GroupMenu groupMenu : groupMenus) {
            groupMenuName.add(groupMenu.getName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, groupMenuName);
        spinnerGroupMenu.setAdapter(arrayAdapter);
        spinnerGroupMenu.setSelection(0);
    }

    private int getCafeId(){
        UserHelper userHelper = new UserHelper(this);
        User user = userRepository.find(userHelper.selectUser().getId());
        userHelper.close();
        return user.getCafeList().get(0).getId();
    }

    private boolean isNumber(String num){
        try {
            Integer.parseInt(num);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
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
                .setRationaleMessage("구글 로그인을 하기 위해서는 주소록 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
