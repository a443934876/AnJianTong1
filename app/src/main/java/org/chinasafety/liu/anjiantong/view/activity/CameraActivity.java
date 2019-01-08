package org.chinasafety.liu.anjiantong.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.view.widget.my_camera.ITakePhotoListener;
import org.chinasafety.liu.anjiantong.view.widget.my_camera.MyCamera;

import java.util.ArrayList;

public class CameraActivity extends Activity implements ITakePhotoListener, View.OnClickListener {
    private MyCamera mMyCamera;
    private ImageButton mCameraBtn;
    private TextView mPhotoCount;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//        getActionBar().setTitle("拍照");
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        mMyCamera = (MyCamera) findViewById(R.id.my_camera);
        mMyCamera.setTakePhotoListener(this);
        mCameraBtn = (ImageButton) findViewById(R.id.xcpz_btn);
        mCameraBtn.setOnClickListener(this);
        mPhotoCount = (TextView) findViewById(R.id.photo_count);
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyCamera.releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyCamera.setCameraCallback();
    }

    @Override
    public void getPhotoCount(int count) {
        mPhotoCount.setText(count + "");
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCameraBtn.getId()) {
            mMyCamera.takePhoto();
        } else if (id == mButton.getId()) {
            ArrayList<String> imagePaths = mMyCamera.getImageDatas();
            StringBuilder stringBuilder = new StringBuilder();
            for (String imagePath : imagePaths) {
                stringBuilder.append(imagePath);
                stringBuilder.append(",");
            }
            String imagePath = "";
            if (stringBuilder.length() > 0) {
                imagePath = stringBuilder.substring(0, stringBuilder.length() - 1);
            }
            Intent intent = new Intent();
            intent.putExtra("imagePathResult", imagePath);
            CameraActivity.this.setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
