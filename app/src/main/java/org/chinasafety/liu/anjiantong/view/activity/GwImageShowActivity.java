package org.chinasafety.liu.anjiantong.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.chinasafety.liu.anjiantong.R;

public class GwImageShowActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gw_image_show);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.picture_load)
                .error(R.drawable.picture_load)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
