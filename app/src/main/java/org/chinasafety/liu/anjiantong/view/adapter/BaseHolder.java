package org.chinasafety.liu.anjiantong.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by cqj on 2017-05-22.
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder{


    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(List<T> data, int position, IParamContainer container, PublishSubject<T> itemClick);
}
