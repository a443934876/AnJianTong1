package org.chinasafety.liu.anjiantong.view.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.view.BaseActivity;


public class JcbDetailActivity extends BaseActivity {

	private TextView jcbdetail_tv;
	private String detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jcbdetail_view);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		jcbdetail_tv = (TextView) findViewById(R.id.jcbdetail_tv);
		detail =getIntent().getStringExtra("detail");
		jcbdetail_tv.setText(detail);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return true;
	}
}
