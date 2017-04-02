package com.b2come.pcroom.item;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;


public class MyGridLayout extends GridLayout {

    View[] mChild = null;

    public MyGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridLayout(Context context) {
        this(context, null);
    }

    private void arrangeElements() {

        mChild = new View[getChildCount()];

        for (int i = 0; i < getChildCount(); i++) {
            mChild[i] = getChildAt(i);
        }

        removeAllViews();
        for (int i = 0; i < mChild.length; i++) {
            if (mChild[i].getVisibility() != GONE) {
                /*GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
                mChild[i].setLayoutParams(parem);*/
                addView(mChild[i]);
            }
        }
        for (int i = 0; i < mChild.length; i++) {
            if (mChild[i].getVisibility() == GONE) {
                /*GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
                mChild[i].setLayoutParams(parem);*/
                addView(mChild[i]);
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        arrangeElements();
        super.onLayout(changed, left, top, right, bottom);

    }


}