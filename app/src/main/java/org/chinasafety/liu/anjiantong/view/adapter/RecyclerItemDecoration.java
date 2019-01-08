package org.chinasafety.liu.anjiantong.view.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/*
 * Created by ztt on 2017-05-22.
 */
public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {

//    private final Drawable mDrawable;
    private Paint mPaint;

    public RecyclerItemDecoration() {
//        mDrawable = context.getResources().getDrawable(android.R.drawable.ic_menu_always_landscape_portrait);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        outRect.set(0,0,0,20);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        for (int i=0;i<parent.getChildCount();i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            c.drawLine(child.getPaddingLeft(),top,parent.getWidth(),top,mPaint);
//            int drawableHeight = mDrawable.getIntrinsicHeight();
//            mDrawable.setBounds(0, top, mDrawable.getIntrinsicWidth(), top+drawableHeight);
//            mDrawable.draw(c);
        }
    }
}
