package org.chinasafety.liu.anjiantong.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.YhfcInfo;
import org.chinasafety.liu.anjiantong.presenter.IYhfcPresenter;
import org.chinasafety.liu.anjiantong.presenter.impl.YhfcPresenterImpl;
import org.chinasafety.liu.anjiantong.view.activity.YhfcUploadActivity;
import org.chinasafety.liu.anjiantong.view.adapter.YhInfoListAdapter;
import org.chinasafety.liu.anjiantong.view.widget.my_camera.ITakePhotoListener;

import java.util.Calendar;
import java.util.List;

/**
 * 隐患已整改未复查
 * Created by Administrator on 2016/4/23.
 */
public class AqfcFragment extends Fragment implements IYhfcPresenter.View, View.OnClickListener,ITakePhotoListener {

    private View mView;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private IYhfcPresenter mPresenter;
    private EditText mStartEdt, mEndEdt, mQymcEdt;
    private TextView fragmentTitle;
    private Button mSearchBtn;

    public static AqfcFragment newInstance() {
        return new AqfcFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_yhfc, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComplement();
    }

    public AdapterView.OnItemClickListener getListItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                YhfcInfo info = (YhfcInfo) pAdapterView.getItemAtPosition(pI);
                try{
                    YhfcUploadActivity.start(getActivity(), Integer.parseInt(info.getHTroubleID()),info.getSafetyTrouble());
                }catch (NumberFormatException ignored){

                }
            }
        };
    }

    private void initComplement() {
        mListView = (ListView) mView.findViewById(R.id.yhfc_list);
        mSearchBtn = (Button) mView.findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(this);
        fragmentTitle = (TextView) mView.findViewById(R.id.fragment_title);
        fragmentTitle.setText(getFragmentTitle());
        mEndEdt = (EditText) mView.findViewById(R.id.search_enddate);
        setDate(mEndEdt);
        mStartEdt = (EditText) mView.findViewById(R.id.search_startdate);
        setDate(mStartEdt);
        mQymcEdt = (EditText) mView.findViewById(R.id.search_qymc);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progress_bar);
        mPresenter = new YhfcPresenterImpl(this);
        mListView.setOnItemClickListener(getListItemClick());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getYhfcList( mStartEdt.getText().toString(), mEndEdt.getText().toString(), "");
    }

    public String getFragmentTitle(){
        return "以下隐患已反馈未复查";
    }

    private void setDate(final EditText edt) {
        edt.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        edt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DateDialog(edt);
            }
        });
        edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean focus) {
                if (focus) {
                    DateDialog(edt);
                }
            }
        });
    }

    private void DateDialog(final EditText edt) {
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String smon = String.valueOf(monthOfYear + 1);
                String sday = String.valueOf(dayOfMonth);
                if (smon.length() == 1) {
                    smon = "0" + smon;
                }
                if (sday.length() == 1) {
                    sday = "0" + sday;
                }
                edt.setText(String.format("%s-%s-%s", String.valueOf(year), smon, sday));
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void getYhfcListSuccess(List<YhfcInfo> pYhfcInfoList) {
        YhInfoListAdapter adapter = new YhInfoListAdapter(getActivity(), pYhfcInfoList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void pendingDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    @Override
    public void cancelDialog() {
        mProgressBar.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void toast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toast(int toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public int getReview() {
        return 0;
    }

    @Override
    public int getFinished() {
        return 0;
    }

    @Override
    public boolean isYhzg() {
        return false;
    }

    @Override
    public void onClick(View pView) {
        if (pView.getId() == mSearchBtn.getId()) {
            mPresenter.getYhfcList(mStartEdt.getText().toString(), mEndEdt.getText().toString(), mQymcEdt.getText().toString());
        }
    }

    @Override
    public void getPhotoCount(int count) {

    }
}
