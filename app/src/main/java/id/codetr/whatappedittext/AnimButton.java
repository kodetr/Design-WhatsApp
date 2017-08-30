package id.codetr.whatappedittext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;


/**
 * Created by Gunhan on 17.08.2015.
 */
public class AnimButton extends ImageButton {
    private static final Interpolator interpolator = new DecelerateInterpolator();

    public static final int FIRST_STATE = 1;
    public static final int SECOND_STATE = 2;
    private static final float SCALE_TOTAL = 1f;
    private static final float ALPHA_TOTAL = 255;

    private Drawable firstDrawable;
    private Drawable secondDrawable;

    private int state = FIRST_STATE;
    private int duration = 300;
    private boolean init = false;
    private int firstResourceId;
    private int secondResourceId;

    public AnimButton(Context context) {
        super(context);
    }

    public AnimButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnimButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AnimButton, 0, 0);
        if (array != null) {
            firstResourceId = array.getResourceId(R.styleable.AnimButton_first, -1);
            secondResourceId = array.getResourceId(R.styleable.AnimButton_second, -1);
            duration = array.getInteger(R.styleable.AnimButton_duration, duration);

            array.recycle();
        }

        initCommon();
    }

    private void initCommon() {
        if (firstResourceId > 0 && secondResourceId > 0) {
            init = true;

            Resources resources = getResources();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                firstDrawable = resources.getDrawable(firstResourceId, null);
                secondDrawable = resources.getDrawable(secondResourceId, null);
            } else {
                firstDrawable = resources.getDrawable(firstResourceId);
                secondDrawable = resources.getDrawable(secondResourceId);
            }

            setImageDrawable(firstDrawable);
        }
    }

    public void goToState(int state) {
        if (!init || this.state == state) return;

        switch (state) {
            case FIRST_STATE:
                animate(firstDrawable, secondDrawable);
                break;
            case SECOND_STATE:
                animate(secondDrawable, firstDrawable);
                break;
        }

        this.state = state;
    }

    private Drawable makeDrawable(Drawable drawable, float scale, int alpha) {
        ScaleDrawable scaleDrawable = new ScaleDrawable(drawable, 0, scale, scale);
        scaleDrawable.setLevel(1);
        scaleDrawable.setAlpha(alpha);

        return scaleDrawable;
    }


    private void animate(final Drawable from, final Drawable to) {
        //setScaleType(ScaleType.CENTER_INSIDE);

        ValueAnimator animator = ValueAnimator.ofFloat(0, SCALE_TOTAL);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float left = SCALE_TOTAL - value;

                Drawable firstInset = makeDrawable(from, left, (int) (value * ALPHA_TOTAL));
                Drawable secondInset = makeDrawable(to, value, (int) (left * ALPHA_TOTAL));

                setImageDrawable(new LayerDrawable(new Drawable[]{firstInset, secondInset}));
            }
        });

        animator.start();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());

        bundle.putInt("duration", this.duration);
        bundle.putInt("firstResourceId", this.firstResourceId);
        bundle.putInt("secondResourceId", this.secondResourceId);
        bundle.putBoolean("init", this.init);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            this.duration = bundle.getInt("duration");
            this.firstResourceId = bundle.getInt("firstResourceId");
            this.secondResourceId = bundle.getInt("secondResourceId");
            this.init = bundle.getBoolean("init");

            state = bundle.getParcelable("superState");
        }

        super.onRestoreInstanceState(state);
    }
}
