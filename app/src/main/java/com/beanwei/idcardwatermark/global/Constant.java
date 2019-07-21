package com.beanwei.idcardwatermark.global;

import com.beanwei.idcardwatermark.utils.FileUtils;

import java.io.File;

public class Constant {
    public static final String APP_NAME = "IDCardWatermark";
    public static final String BASE_DIR = APP_NAME + File.separator;
    public static final String DIR_ROOT = FileUtils.getRootPath() + File.separator + BASE_DIR;

    // Code
    public static final int CAMERA_REQUEST_CODE = 0x00;
    public static final int ALBUM_REQUEST_CODE = 0x01;
    public static final int PERMISSION_CODE_ONAPPCREATE = 0x12;

    // Tips
    public static final String SAVE_WATERMARK_IAMGE_SUCCESS = "已成功保存到本地相册";
    public static final String SAVE_WATERMARK_IAMGE_FAIL = "保存失败";

}
