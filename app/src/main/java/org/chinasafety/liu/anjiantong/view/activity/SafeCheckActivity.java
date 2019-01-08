package org.chinasafety.liu.anjiantong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.view.BaseActivity;
import org.chinasafety.liu.anjiantong.view.fragment.AqjcFragment;

public class SafeCheckActivity extends BaseActivity {

    private static final String KEY_ORG_ID = "orgId";
    private static final String KEY_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String orgId = getIntent().getStringExtra(KEY_ORG_ID);
        String name = getIntent().getStringExtra("name");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_safe_check_container, AqjcFragment.newInstance(orgId,name))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_safe_check_switch,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }else if(item.getItemId() ==R.id.switch_company){
            CompanySearchActivity.start(this);
            finish();
        }
        return true;
    }

    public static void start(Context context,String orgId,String name) {
        Intent starter = new Intent(context, SafeCheckActivity.class);
        starter.putExtra(KEY_ORG_ID,orgId);
        starter.putExtra(KEY_NAME,name);
        context.startActivity(starter);
    }
}
