package nl.ou.applablib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * adapted from http://stackoverflow.com/questions/18229358/bitmap-in-imageview-with-rounded-corners
 * Created by bvgastel on 23-02-14.
 */
public class RoundedFrameLayout extends FrameLayout {
    private static final String TAG = "RoundedFrameLayout";
    private final float[] radii;
    private Path clipPath;

    public RoundedFrameLayout(Context c) {
        this(c, null);
    }

    public RoundedFrameLayout(Context c, AttributeSet attrs) {
        super(c, attrs);

        /*
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        */

        int radiusTopLeft = 0;
        int radiusTopRight = 0;
        int radiusBottomLeft = 0;
        int radiusBottomRight = 0;

        final TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.RoundedImageView);
        try {
            radiusTopLeft = radiusTopRight = radiusBottomLeft = radiusBottomRight = ta.getDimensionPixelSize(
                    R.styleable.RoundedImageView_radius,
                    0);
        } finally {
            ta.recycle();
        }
        radii = new float[8];
        radii[0] = radii[1] = radiusTopLeft;
        radii[2] = radii[3] = radiusTopRight;
        radii[4] = radii[5] = radiusBottomRight;
        radii[6] = radii[7] = radiusBottomLeft;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Log.i(TAG, "onSizeChanged(w=" + w + ", h=" + h + ")");
        clipPath = new Path();
        RectF rect = new RectF(getPaddingLeft(), getPaddingTop(), w-getPaddingRight(), h-getPaddingBottom());
        clipPath.addRoundRect(rect, radii, Path.Direction.CW);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        //Log.i(TAG, "drawChild");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            // only software clipping is supported on Ice Cream Sandwich, which is terrible slow, so it is better to now have any rounded corners
            canvas.clipPath(clipPath);
        return super.drawChild(canvas, child, drawingTime);
    }
}
