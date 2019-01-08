package org.chinasafety.liu.anjiantong.view.adapter;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by cqj on 2017-05-22.
 */
public class RecyclerBaseAdapter<T, VH extends BaseHolder<T>> extends RecyclerView.Adapter<VH>
        implements IParamContainer {

    @LayoutRes
    private int mLayoutId;
    private Class<VH> mClazz;
    private List<T> mDataList;
    private PublishSubject<T> mItemClick = PublishSubject.create();

    public RecyclerBaseAdapter(@LayoutRes int layoutId, Class<VH> clazz) {
        mLayoutId = layoutId;
        mClazz = clazz;
        mDataList = new ArrayList<>();
    }

    public void add(T data) {
        if (data == null) {
            return;
        }
        mDataList.add(data);
        notifyItemInserted(mDataList.size()+1);
    }

    public void insert(T data) {
        if (data == null) {
            return;
        }
        if (mDataList.size() == 0) {
            mDataList.add(data);
        } else {
            mDataList.set(0, data);
        }
        notifyItemInserted(0);
    }

    public void addAll(List<T> datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void setAll(List<T> datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position < 0 || position >= mDataList.size()) {
            return;
        }
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(T data) {
        if (data == null) {
            return;
        }
        int position = mDataList.indexOf(data);
        mDataList.remove(data);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public Observable<T> itemClickObserve() {
        return mItemClick.throttleFirst(500, TimeUnit.MILLISECONDS);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        VH vh = null;
        try {
            Constructor<VH> csr = mClazz.getConstructor(View.class);  //调用有参构造
            vh = csr.newInstance(view);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(mDataList, position, this, mItemClick);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private final HashMap<String, Object> paramContainerMap = new HashMap<>();

    @Override
    public Object get(String key) {
        return paramContainerMap.get(key);
    }

    @Override
    public void set(String key, Object object) {
        paramContainerMap.put(key, object);
    }
}
