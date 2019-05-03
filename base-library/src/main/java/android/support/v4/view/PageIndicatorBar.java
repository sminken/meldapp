package android.support.v4.view; /**
 * Created by bvgastel on 28-02-14.
 */
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import nl.ou.applablib.R;

/**
 *
 */
@ViewPager.DecorView
public class PageIndicatorBar extends FrameLayout {
    private static final String TAG = "android.support.v4.view.PageIndicatorBar";
    private static final boolean DEBUG = false;

    ViewPager mPager;
    TextView mPrevText;
    TextView mCurrText;
    TextView mNextText;
    boolean showDirection;

    private final PageListener mPageListener = new PageListener();

    private static final int[] ATTRS = new int[] {
            android.R.attr.textAppearance,
            android.R.attr.textSize,
            android.R.attr.textColor,
            android.R.attr.fontFamily,
            R.attr.previousText,
            R.attr.nextText,
            R.attr.showDirection
    };

    private static final float SIDE_ALPHA = 0.6f;
    private static final int PADDING_SIDE = 10; // dip

    private int mNonPrimaryAlpha;
    int mTextColor;
    private long animationDuration = 1;

    public PageIndicatorBar(Context context) {
        this(context, null);
    }

    public PageIndicatorBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        animationDuration = Math.max(getResources().getInteger(android.R.integer.config_shortAnimTime), 1);

        String prev = null;
        String next = null;

        final float density = context.getResources().getDisplayMetrics().density;

        final TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        showDirection = a.getBoolean(6, true);
        if (showDirection)
            addView(mPrevText = new TextView(context));
        addView(mCurrText = new TextView(context));
        if (showDirection)
            addView(mNextText = new TextView(context));

        final int textAppearance = a.getResourceId(0, 0);
        if (textAppearance != 0) {
            if (mPrevText != null) mPrevText.setTextAppearance(context, textAppearance);
            mCurrText.setTextAppearance(context, textAppearance);
            if (mNextText != null) mNextText.setTextAppearance(context, textAppearance);
        }
        final int textSize = a.getDimensionPixelSize(1, 0);
        if (textSize != 0) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        if (a.hasValue(2)) {
            final int textColor = a.getColor(2, 0);
            if (mPrevText != null) mPrevText.setTextColor(textColor);
            mCurrText.setTextColor(textColor);
            if (mNextText != null) mNextText.setTextColor(textColor);
        }
        if (a.hasValue(3)) {
            String fontFamilyName = a.getString(3);
            Typeface tf = Typeface.create(fontFamilyName, Typeface.NORMAL);
            if (mPrevText != null) mPrevText.setTypeface(tf);
            mCurrText.setTypeface(tf);
            if (mNextText != null) mNextText.setTypeface(tf);
        }
        CharSequence prevText = a.getText(4);
        CharSequence nextText = a.getText(5);
        a.recycle();

        if (prevText == null)
            prevText = getResources().getString(R.string.previous);
        if (nextText == null)
            nextText = getResources().getString(R.string.next);

        if (mPrevText != null) mPrevText.setText(prevText);
        if (mNextText != null) mNextText.setText(nextText);

        if (mPrevText != null) mPrevText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        mCurrText.setGravity(Gravity.CENTER);
        if (mNextText != null) mNextText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        mTextColor = mCurrText.getTextColors().getDefaultColor();
        setNonPrimaryAlpha(SIDE_ALPHA);

        if (mPrevText != null) mPrevText.setEllipsize(TruncateAt.END);
        mCurrText.setEllipsize(TruncateAt.END);
        if (mNextText != null) mNextText.setEllipsize(TruncateAt.END);

        if (mPrevText != null) mPrevText.setSingleLine();
        mCurrText.setSingleLine();
        if (mNextText != null) mNextText.setSingleLine();
    }

    /**
     * Set the alpha value used for non-primary page titles.
     *
     * @param alpha Opacity value in the range 0-1f
     */
    public void setNonPrimaryAlpha(float alpha) {
        mNonPrimaryAlpha = (int) (alpha * 255) & 0xFF;
        final int transparentColor = (mNonPrimaryAlpha << 24) | (mTextColor & 0xFFFFFF);
        if (mPrevText != null) mPrevText.setTextColor(transparentColor);
        if (mNextText != null) mNextText.setTextColor(transparentColor);
    }

    /**
     * Set the color value used as the base color for all displayed page titles.
     * Alpha will be ignored for non-primary page titles. See {@link #setNonPrimaryAlpha(float)}.
     *
     * @param color Color hex code in 0xAARRGGBB format
     */
    public void setTextColor(int color) {
        mTextColor = color;
        mCurrText.setTextColor(color);
        final int transparentColor = (mNonPrimaryAlpha << 24) | (mTextColor & 0xFFFFFF);
        if (mPrevText != null) mPrevText.setTextColor(transparentColor);
        if (mNextText != null) mNextText.setTextColor(transparentColor);
    }

    /**
     * Set the default text size to a given unit and value.
     * See {@link TypedValue} for the possible dimension units.
     *
     * <p>Example: to set the text size to 14px, use
     * setTextSize(TypedValue.COMPLEX_UNIT_PX, 14);</p>
     *
     * @param unit The desired dimension unit
     * @param size The desired size in the given units
     */
    public void setTextSize(int unit, float size) {
        if (mPrevText != null) mPrevText.setTextSize(unit, size);
        mCurrText.setTextSize(unit, size);
        if (mNextText != null) mNextText.setTextSize(unit, size);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        final ViewParent parent = getParent();
        if (!(parent instanceof ViewPager)) {
            throw new IllegalStateException(
                    "PageIndictorBar must be a direct child of a ViewPager.");
        }

        mPager = (ViewPager) parent;
        mPager.setInternalPageChangeListener(mPageListener);
        mPager.addOnAdapterChangeListener(mPageListener);
        if (mPager.getAdapter() != null)
            mPager.getAdapter().registerDataSetObserver(mPageListener);
        final int itemCount = mPager.getAdapter() != null ? mPager.getAdapter().getCount() : 0;
        updateText(mPager.getCurrentItem(), itemCount);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPager != null) {
            mPager.setInternalPageChangeListener(null);
            mPager.removeOnAdapterChangeListener(mPageListener);
            if (mPager.getAdapter() != null)
                mPager.getAdapter().unregisterDataSetObserver(mPageListener);
            mPager = null;
        }
    }

    private void setVisible(@NonNull final View view, boolean visible) {
        if (view == null)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            return;
        }
        if (!visible) {
            view.animate().cancel();
            view.animate().alpha(0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            view.animate().cancel();
            view.setVisibility(View.VISIBLE);
            view.animate().alpha(1.0f).setDuration(animationDuration).setListener(null);
        }
    }

    void updateText(int position, int itemCount) {
        if (DEBUG) Log.i(TAG, "updating text to position=" + position + " itemCount=" + itemCount);
        setVisible(mPrevText, itemCount > 0 && position > 0);
        setVisible(mCurrText, itemCount > 0);
        setVisible(mNextText, itemCount > 0 && position < itemCount-1);

        mCurrText.setText(getResources().getString(R.string.of, position + 1, itemCount));

        invalidate(); // needed for after restore
    }

    private class PageListener extends DataSetObserver implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (DEBUG) Log.i(TAG, "onPageSelected: " + position + " pager=" + mPager);
            if (mPager != null)
                updateText(position, mPager.getAdapter());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (DEBUG) Log.i(TAG, "onPageScrollStateChanged: " + state);
            if (mPager != null)
                updateText(mPager.getCurrentItem(), mPager.getAdapter());
        }

        @Override
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter oldAdapter, PagerAdapter newAdapter) {
            if (DEBUG) Log.i(TAG, "onAdapterChanged");
            if (oldAdapter != null)
                oldAdapter.unregisterDataSetObserver(this);
            if (mPager != null)
                updateText(mPager.getCurrentItem(), newAdapter);
            if (newAdapter != null)
                newAdapter.registerDataSetObserver(this);
        }

        @Override
        public void onChanged() {
            if (DEBUG) Log.i(TAG, "onChanged");
            if (mPager != null)
                updateText(mPager.getCurrentItem(), mPager.getAdapter());
        }

        private void updateText(int currentItem, PagerAdapter adapter) {
            final int itemCount = adapter != null ? adapter.getCount() : 0;
            PageIndicatorBar.this.updateText(currentItem, itemCount);
        }
    }
}
