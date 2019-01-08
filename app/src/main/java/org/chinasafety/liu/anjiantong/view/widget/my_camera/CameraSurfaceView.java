package org.chinasafety.liu.anjiantong.view.widget.my_camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.utils.BitmapUtil;
import org.chinasafety.liu.anjiantong.utils.PictureUtil;
import org.chinasafety.liu.anjiantong.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback, IYhdjImageCallBack {

    private static final String TAG = "CameraSurfaceView";

    private Context mContext;
    private SurfaceHolder holder;
    private Camera mCamera;
    private int mScreenWidth;
    private int mScreenHeight;
    private ArrayList<String> mImageData;
    private ITakePhotoListener mTakePhotoListener;
    // 拍照瞬间调用
    private Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.i(TAG, "shutter");
        }
    };
    // 获得没有压缩过的图片数据
    private Camera.PictureCallback raw = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            Log.i(TAG, String.valueOf(data));
        }
    };
    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            zipBitmap(data);
            mCamera.stopPreview();// 关闭预览
            mCamera.startPreview();// 开启预览
//            BufferedOutputStream bos = null;
//            Bitmap bm = null;
//            try {
//            	// 获得图片
//                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    String filePath = "/sdcard/dyk"+ System.currentTimeMillis()+".jpg";//照片保存路径
//                    File file = new File(filePath);
//                    if (!file.exists()){
//                        file.createNewFile();
//                    }
//                    bos = new BufferedOutputStream(new FileOutputStream(file));
//                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中
//
//                }else{
//                    Toast.makeText(mContext,"没有检测到内存卡", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    bos.flush();//输出
//                    bos.close();//关闭
//                    bm.recycle();// 回收bitmap空间
//                    mCamera.stopPreview();// 关闭预览
//                    mCamera.startPreview();// 开启预览
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    };

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getScreenMetrix(context);
        initView();
    }

    public void setTakePhotoListener(ITakePhotoListener pTakePhotoListener) {
        mTakePhotoListener = pTakePhotoListener;
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private void initView() {
        mImageData = new ArrayList<String>();
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = Camera.open();//开启相机
            try {
                mCamera.setPreviewDisplay(holder);//摄像头画面显示在Surface上
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //设置参数并开始预览
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        mCamera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();//停止预览
        mCamera.release();//释放相机资源
        mCamera = null;
        holder = null;
    }

    @Override
    public void onAutoFocus(boolean success, Camera Camera) {
        if (success) {
            Log.i(TAG, "onAutoFocus success=" + success);
        }
    }

    private void zipBitmap(final byte[] data) {
        Observable.
                create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        try {
                            Bitmap smallBitmap = PictureUtil.getSmallBitmap(data);
//                            Bitmap bimap = BitmapUtil.Bytes2Bimap(data);
//                            Bitmap bitmap = BitmapUtil.imageZoom(data);
                            String path = BitmapUtil.saveToSDCard(smallBitmap);
                            PictureUtil.galleryAddPic(getContext(),path);
//                            String newPath =BitmapUtil.reviewPicRotate(bitmap,path);
                            e.onNext(path);
                        } catch (Exception pE) {
                            pE.printStackTrace();
                            e.onError(pE);
                        }
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {

                        getBitmap(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void getBitmap(String imageData) {
        if (StringUtil.isNotEmpty(imageData)) {
            mImageData.add(imageData);
            Toast.makeText(mContext, R.string.take_photo_success, Toast.LENGTH_SHORT).show();
            if (mTakePhotoListener != null) {
                mTakePhotoListener.getPhotoCount(mImageData.size());
            }
        } else {
            Toast.makeText(mContext, R.string.take_photo_failure, Toast.LENGTH_SHORT).show();
        }

//        }
//        FinalBitmap.create(mContext).display(mTopImageView, imageData);

//        mImagePath = imagePath;
//        mTopImageView.setVisibility(View.VISIBLE);


    }

    // TODO: 2017/12/20

    private void setCameraParams(Camera camera, int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // 获取摄像头支持的PictureSize列表
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        /**从列表中选取合适的分辨率*/
        Camera.Size picSize = getProperSize(pictureSizeList, ((float) height / width));
        if (null == picSize) {
            picSize = parameters.getPictureSize();
        }
        // 根据选出的PictureSize重新设置SurfaceView大小
        float w = picSize.width;
        float h = picSize.height;
        parameters.setPictureSize(picSize.width, picSize.height);
//        this.setLayoutParams(new FrameLayout.LayoutParams((int) (height*(h/w)), height));
        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size preSize = getProperSize(previewSizeList, ((float) height) / width);
        if (null != preSize) {
            parameters.setPreviewSize(preSize.width, preSize.height);
        }
        parameters.setJpegQuality(100); // 设置照片质量
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }
        mCamera.cancelAutoFocus();//自动对焦。
        mCamera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
        mCamera.setParameters(parameters);

    }

    /**
     * 从列表中选取合适的分辨率
     * 默认w:h = 4:3
     * <p>注意：这里的w对应屏幕的height
     * h对应屏幕的width<p/>
     */
    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }

        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }
        return result;
    }


    @Override
    public ArrayList<String> getImageDatas() {
        return mImageData;
    }

    @Override
    public void takePhoto() {
        if (mImageData.size() == 3) {
            Toast.makeText(mContext, R.string.take_photo_max_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        //设置参数,并拍照
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        // 当调用camera.takePiture方法后，camera关闭了预览，这时需要调用startPreview()来重新开启预览
        mCamera.takePicture(null, null, jpeg);
    }

    @Override
    public void success() {
        mImageData.clear();
    }

    @Override
    public void releaseCamera() {
        getHolder().removeCallback(this);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void setCameraCallback() {
        getHolder().addCallback(this);

    }
}
