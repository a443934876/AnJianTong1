package org.chinasafety.liu.anjiantong.model.provider;

import android.graphics.Bitmap;

/**
 * 项目名称：Anfutong1
 * 创建时间：2018/1/17 17:41
 * 注释说明：
 */

public    class ImageInfo   {
    private String imagePath;
    private Bitmap bitmap;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
