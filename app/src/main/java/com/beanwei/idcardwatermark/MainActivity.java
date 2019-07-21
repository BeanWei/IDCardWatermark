package com.beanwei.idcardwatermark;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.beanwei.idcardwatermark.global.Constant;
import com.beanwei.idcardwatermark.utils.FileUtils;
import com.beanwei.idcardwatermark.utils.ImageUtils;
import com.beanwei.idcardwatermark.utils.PermissionUtils;
import com.beanwei.idcardwatermark.watermark.AddWatermark;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private boolean isToast = true;
    public Uri photoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 权限申请
        boolean checkPerOnAppCreate = PermissionUtils.checkPermissionOnAppCreate(this, Constant.PERMISSION_CODE_ONAPPCREATE,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
//        if (checkPerOnAppCreate) {
//
//        }

        // android 6.0 拍照
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        // 调用系统相机拍照
        Button button_camera = this.findViewById(R.id.btn_camera);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileUtils.createOrExistsDir(Constant.DIR_ROOT)) {
                    StringBuffer buffer = new StringBuffer();
                    String imagePath = buffer.append(Constant.DIR_ROOT).append(Constant.APP_NAME).append(ImageUtils.defaultImageName()).toString();

                    Intent intenToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoUri = Uri.fromFile(new File(imagePath));
                    intenToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                    startActivityForResult(intenToTakePhoto, Constant.CAMERA_REQUEST_CODE);
                }
            }
        });

        // 从相册选择图片
        Button button_album = this.findViewById(R.id.btn_album);
        button_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAlbum = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToAlbum, Constant.ALBUM_REQUEST_CODE);
            }
        });
    }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intentToAddWatermark;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.CAMERA_REQUEST_CODE:
                    if (photoUri != null) {
                        File img = new File(FileUtils.getFilePathByUri(this, photoUri));
                        if (!img.exists()) {
                            photoUri = null;
                        }

                    }
                    break;
                case Constant.ALBUM_REQUEST_CODE:
                    photoUri = data.getData();
                    break;
                default:
                    break;
            }
            if (photoUri != null) {
                intentToAddWatermark = new Intent(MainActivity.this, AddWatermark.class);
                // 将图片路径传递给目标activity
                intentToAddWatermark.putExtra("photo_uri", photoUri.toString());
                startActivity(intentToAddWatermark);
            }
        }
     }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        boolean isPermissions = true;
//        for (int i = 0; i < permissions.length; i++) {
//            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                isPermissions = false;
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) { //用户选择了"不再询问"
//                    if (isToast) {
//                        Toast.makeText(this, "请手动打开该应用需要的权限", Toast.LENGTH_SHORT).show();
//                        isToast = false;
//                    }
//                }
//            }
//        }
//        isToast = true;
//        if (isPermissions) {
//            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "允许所有权限");
//        } else {
//            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "有权限不允许");
//            finish();
//        }
//    }
}
