package org.chinasafety.liu.anjiantong.view.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.provider.GlobalDataProvider;
import org.chinasafety.liu.anjiantong.presenter.ICenterFragmentPresenter;
import org.chinasafety.liu.anjiantong.presenter.impl.CenterFragmentPresenterImpl;
import org.chinasafety.liu.anjiantong.view.activity.GwListActivity;
import org.chinasafety.liu.anjiantong.view.activity.MapSelectActivity;
import org.chinasafety.liu.anjiantong.view.activity.SafeReviewActivity;
import org.chinasafety.liu.anjiantong.view.adapter.GlideImageLoader;
import org.chinasafety.liu.anjiantong.view.widget.zxing.decoding.Intents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mini on 17/5/23.
 */

public class CenterFragment extends Fragment implements ICenterFragmentPresenter.IView {

    private TextView mTvMessageUnread;
    private TextView mClassNoStudy;
    private TextView mBtnSafeCheck;
    private TextView mBtnNotification;
    //    private TextView mBtnSafeReview;
    private TextView mBtnWorkRecord;
    private TextView mBtnCompanySearch;
    private TextView mBtnPolicy;
    private TextView mBtnStudy;
    private PopupWindow mPopupWindow;
    private ICenterFragmentPresenter mPresenter;
    private Banner banner;


    public static CenterFragment newInstance() {

        Bundle args = new Bundle();
        CenterFragment fragment = new CenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page_center, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    private void initView(View view) {
        banner = (Banner) view.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.login_title_icon);
        images.add(R.drawable.login_title_icon);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImages(images);
        List<String > titles = new ArrayList<>();
        titles.add("慧为安监通");
        titles.add("慧为安监通");
        banner.setBannerTitles(titles);
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        TextView tvDate = (TextView) view.findViewById(R.id.fragment_home_page_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        tvDate.setText(simpleDateFormat.format(new Date()));
        TextView tvCurrentUser = (TextView) view.findViewById(R.id.fragment_home_page_current_user);
        tvCurrentUser.setText(String.format(Locale.getDefault(), "%s%s", getResources().getString(R.string.home_page_current_user_prefix), GlobalDataProvider.INSTANCE.getUserInfo().getName()));
        TextView tvCurrentCompany = (TextView) view.findViewById(R.id.fragment_center_company_name);
        tvCurrentCompany.setText(GlobalDataProvider.INSTANCE.getCompanyInfo().getComFullName());
        mTvMessageUnread = (TextView) view.findViewById(R.id.home_page_message_count);
        mClassNoStudy = (TextView) view.findViewById(R.id.home_page_class_count);
        mBtnNotification = (TextView) view.findViewById(R.id.home_page_goto_notification);
        mBtnNotification.setOnClickListener(mClickListener);
        mBtnSafeCheck = (TextView) view.findViewById(R.id.home_page_goto_check);
        mBtnSafeCheck.setOnClickListener(mClickListener);
//        mBtnSafeReview = (TextView) view.findViewById(R.id.home_page_goto_review);
//        mBtnSafeReview.setOnClickListener(mClickListener);
        mBtnCompanySearch = (TextView) view.findViewById(R.id.home_page_goto_company_search);
        mBtnCompanySearch.setOnClickListener(mClickListener);
        mBtnWorkRecord = (TextView) view.findViewById(R.id.home_page_goto_work_record);
        mBtnWorkRecord.setOnClickListener(mClickListener);
        mBtnPolicy = (TextView) view.findViewById(R.id.home_page_goto_policy);
        mBtnPolicy.setOnClickListener(mClickListener);
        mBtnStudy = (TextView) view.findViewById(R.id.home_page_goto_class);
        mBtnStudy.setOnClickListener(mClickListener);
        mPresenter = new CenterFragmentPresenterImpl(this);
        mPresenter.getUnReadMessageCount();
        mPresenter.getUnStudyClassCount();

    }

    private View.OnClickListener popClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pop_SafeCheck: {
                    MapSelectActivity.start(getActivity());
                    mPopupWindow.dismiss();
                    break;
                }
                case R.id.pop_SafeReview:
                    SafeReviewActivity.start(getActivity());
                    mPopupWindow.dismiss();
                    break;
            }
        }
    };
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if (view.getId() == mBtnSafeCheck.getId()) {
//                MapSelectActivity.start(getActivity());
//            } else if (view.getId() == mBtnSafeReview.getId()) {
//                SafeReviewActivity.start(getActivity());
//            } else
            if (view.getId() == mBtnCompanySearch.getId()) {
                MapSelectActivity.start(getActivity());
//                CompanySearchActivity.start(getActivity());
            } else if (view.getId() == mBtnNotification.getId()) {
                GwListActivity.start(getActivity());


//            } else if (view.getId() == mBtnWorkRecord.getId()) {
//                WorkRecordActivity.start(getActivity());
//            } else if (view.getId() == mBtnPolicy.getId()) {
//                PolicyActivity.start(getActivity());
//            } else if (view.getId() == mBtnStudy.getId()) {
//                StudyActivity.start(getActivity());
            } else if (view.getId() == mBtnSafeCheck.getId()) {
                showPopupWindow();
            }
        }
    };


    private void showPopupWindow() {
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.popuplayout, null);
        mPopupWindow = new PopupWindow(contentView);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        TextView Check = (TextView) contentView.findViewById(R.id.pop_SafeCheck);
        TextView Review = (TextView) contentView.findViewById(R.id.pop_SafeReview);
        Check.setOnClickListener(popClickListener);
        Review.setOnClickListener(popClickListener);
        mPopupWindow.showAsDropDown(mBtnSafeCheck);

    }

//    public List<String> getDataSource() {
//        List<String> spinnerList = new ArrayList<String>();
//        spinnerList.add("安全检查");
//        spinnerList.add("检查");
//        spinnerList.add("复查");
//        return spinnerList;
//    }

    @Override
    public void setUnReadMessageCount(String count) {
        mTvMessageUnread.setText(String.format(Locale.getDefault(), "%s%s", count, getResources().getString(R.string.home_page_message_unread_count)));
    }

    @Override
    public void setUnStydyClassCount(String classCount) {
        mClassNoStudy.setText(String.format(Locale.getDefault(), "%s%s", classCount, getResources().getString(R.string.home_page_class_un_study)));
    }

    @Override
    public void toast(@StringRes int msg) {

    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.startAutoPlay();
    }
}
