package org.chinasafety.liu.anjiantong.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.chinasafety.liu.anjiantong.view.activity.SplashActivity.AFT_Cache;

/**
 * 图片下载工具类
 *
 * @author gaozhibin
 */
public class BitmapUtil {


    /**
     * 获取图片的存储目录，在有sd卡的情况下为 “/sdcard/apps_images/本应用包名/cach/images/”
     * 没有sd的情况下为“/data/data/本应用包名/cach/images/”
     *
     * @param context 上下文
     * @return 本地图片存储目录
     */
    public static String getPath(Context context) {
        String path = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        String packageName = context.getPackageName() + "/cach/images/";
        if (hasSDCard) {
            path = "/sdcard/apps_images/" + packageName;
        } else {
            path = "/sdcard/data/" + packageName;
        }
        File file = new File(path);
        boolean isExist = file.exists();
        if (!isExist) {

            file.mkdirs();

        }
        return file.getPath();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * private static Bitmap getImageThumbnail(String imagePath, int width,
     * int height) {
     * Bitmap bitmap = null;
     * BitmapFactory.Options options = new BitmapFactory.Options();
     * options.inJustDecodeBounds = true;
     * // 获取这个图片的宽和高，注意此处的bitmap为null
     * bitmap = BitmapFactory.decodeFile(imagePath, options);
     * options.inJustDecodeBounds = false; // 设为 false
     * // 计算缩放比
     * int h = options.outHeight;
     * int w = options.outWidth;
     * int beWidth = w / width;
     * int beHeight = h / height;
     * int be = 1;
     * if (beWidth < beHeight) {
     * be = beWidth;
     * } else {
     * be = beHeight;
     * }
     * if (be <= 0) {
     * be = 1;
     * }
     * options.inSampleSize = be;
     * // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
     * bitmap = BitmapFactory.decodeFile(imagePath, options);
     * // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
     * bitmap = ThumbnailUtils.extractThumbnail(bitmap, w, h,
     * ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
     * return bitmap;
     * }
     * <p>
     * public static void deleteFile(File f) {
     * File[] files = f.listFiles();
     * for (File file : files) {
     * if (file.isDirectory()) {
     * deleteFile(file);
     * } else {
     * file.delete();
     * }
     * }
     * }
     * <p>
     * /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap imageZoom(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//        图片允许最大空间   单位：KB

        double maxSize = 30;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        double mid = data.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));
        }
        return bitmap;
    }


    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    public static Bitmap imageZoom(Bitmap bmp, int iWidth, int iHeight) {
        Bitmap newBmp = null;
        if (bmp != null) {
            int imageHeight = bmp.getHeight();
            int imageWidth = bmp.getWidth();
            float scaleW = 1;
            float scaleH = 1;
            if (imageHeight >= imageWidth) {// 如果高度比宽度长，则按高度缩放
                double zoomRate = (float) iHeight / imageHeight;
                Log.v("zoomRate=", zoomRate + "");
                scaleW = (float) (scaleW * zoomRate);
                scaleH = (float) (scaleH * zoomRate);
            } else {
                double zoomRate = (float) iWidth / imageWidth;
                Log.v("zoomRate=", zoomRate + "");
                scaleW = (float) (scaleW * zoomRate);
                scaleH = (float) (scaleH * zoomRate);
            }
            /*
             * double scalex = (float)iWidth/imageWidth; double scaley =
			 * (float)iHeight/imageHeight; scaleW = (float)(scaleW * scalex); scaleH
			 * = (float)(scaleH * scaley);
			 */
            Matrix matrix = new Matrix();
            matrix.postScale(scaleW, scaleH);
            newBmp = Bitmap.createBitmap(bmp, 0, 0, imageWidth, imageHeight,
                    matrix, true);
        }
        return newBmp;
    }


    /**
     * 将拍下来的照片存放在SD卡中
     *
     * @throws IOException
     */

    public static String saveToSDCard(Bitmap bm) throws IOException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(date) + ".jpg";
        String path = Environment.getExternalStorageDirectory() + AFT_Cache;
        boolean b = StringUtils.isFolderExists(path);
        if (b) {
            Matrix m = new Matrix();
            int width = bm.getWidth();
            int height = bm.getHeight();
            m.setRotate(90); // 旋转angle度
            bm = Bitmap.createBitmap(bm, 0, 0, width, height, m, true);// 从新生成图片
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            File jpgFile = new File(path, filename);
            FileOutputStream out = new FileOutputStream(jpgFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return jpgFile.getAbsolutePath();
        } else {
            return "";
        }

    }

    /**
     * 根据路径获得图片并压缩返回bitmap
     *
     * @return
     */
    public static Bitmap getSmallBitmap(byte[] data, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     *
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
    //     */
//    public static String reviewPicRotate(Bitmap bitmap, String path) {
//        String newPath = null;
//        try {
//            int degree = getPicRotate(path);
//            if (degree != 0) {
//                Matrix m = new Matrix();
//                int width = bitmap.getWidth();
//                int height = bitmap.getHeight();
//                m.setRotate(degree); // 旋转angle度
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
//            }
////            newPath = saveToSDCard(bitmap);
//            new File(path).delete();
//        } catch (IOException pE) {
//            pE.printStackTrace();
//        }
//        return newPath;
//    }

    /**
     * 读取图片文件旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    public static int getPicRotate(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}