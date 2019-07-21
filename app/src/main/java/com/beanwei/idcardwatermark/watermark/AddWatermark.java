package com.beanwei.idcardwatermark.watermark;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beanwei.idcardwatermark.R;
import com.beanwei.idcardwatermark.global.Constant;
import com.beanwei.idcardwatermark.utils.ImageUtils;
import com.watermark.androidwm_light.WatermarkBuilder;
import com.watermark.androidwm_light.bean.WatermarkText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
public class AddWatermark extends AppCompatActivity {

    private Bitmap photoBitmap = null;

    private ImageView bgImgView;
    private ImageView watermarkImgView;
    private TextView editText;
    private Button btnAddText;
    private Button btnClearWatermark;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_watermark);

        bgImgView = findViewById(R.id.imageView);
        watermarkImgView = findViewById(R.id.imageView_watermark);
        editText = findViewById(R.id.editText);
        btnAddText = findViewById(R.id.btn_add_text);
        btnClearWatermark = findViewById(R.id.btn_clear_watermark);
        btnSave = findViewById(R.id.btn_save);

        // 获取上一个activity传来的图片uri
        Uri photoUri = Uri.parse(getIntent().getStringExtra("photo_uri"));
        // Uri2Bitmap
        try {
            photoBitmap = ImageUtils.getBitmapFromUri(AddWatermark.this, photoUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bgImgView.setImageBitmap(photoBitmap);


        btnAddText.setOnClickListener((View v) -> {
            WatermarkText watermarkText = new WatermarkText(editText.getText().toString())
                    .setPositionX(0.5)          // 横轴坐标
                    .setPositionY(0.5)          // 纵轴坐标
                    .setRotation(30.00)         // 水印旋转角度
                    .setTextAlpha(120)           // 透明度
                    .setTextColor(Color.DKGRAY)   // 文字颜色
                    .setTextShadow(0.1f, 3, 3, Color.GRAY);

            WatermarkBuilder.create(this, bgImgView)
                    .setTileMode(true)
                    .loadWatermarkText(watermarkText)
                    .getWatermark()
                    .setToImageView(bgImgView);
        });

        // clear watermark
        btnClearWatermark.setOnClickListener((View v) -> {
            bgImgView.setImageBitmap(photoBitmap);
            watermarkImgView.setVisibility(View.GONE);
        });

        // save watermark image
        btnSave.setOnClickListener((View v) -> {
            Bitmap watermarkImage = WatermarkBuilder
                    .create(this, bgImgView)
                    .getWatermark()
                    .getOutputImage();
            String save_img_path = ImageUtils.localGalleryPath() + ImageUtils.defaultImageName();
            Log.d("test", save_img_path);
            boolean saved = ImageUtils.save(watermarkImage, save_img_path, Bitmap.CompressFormat.JPEG);
            if (saved) {
                Toast.makeText(this, Constant.SAVE_WATERMARK_IAMGE_SUCCESS, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, Constant.SAVE_WATERMARK_IAMGE_FAIL, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
