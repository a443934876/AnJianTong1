package org.chinasafety.liu.anjiantong.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import net.tsz.afinal.FinalBitmap;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.view.widget.ZoomImageView;

public class ShowImageActivity extends Activity implements ZoomImageView.ZoomClick {

	private ZoomImageView ziv;
	private WindowManager mWindowManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.showimage_view);
		mWindowManager =getWindowManager();
		DisplayMetrics metrics =new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(metrics);
		ziv =(ZoomImageView) findViewById(R.id.ziv);
		ziv.setZoomClick(this);
		String url = getIntent().getStringExtra("data");
		FinalBitmap.create(this).display(ziv,url);
		ziv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void click() {
		finish();
	}
}
