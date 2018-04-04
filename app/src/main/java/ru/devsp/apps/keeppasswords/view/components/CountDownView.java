package ru.devsp.apps.keeppasswords.view.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import ru.devsp.apps.keeppasswords.R;

/**
 * Таймер
 * Created by gen on 10.12.2017.
 */

public class CountDownView extends View {
    private static final int TEXT_SIZE = 22;

    private Paint mTextPaint;
    private int mMainColor;


    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CountDownView,
                0, 0);
        mMainColor = a.getColor(R.styleable.CountDownView_color, ContextCompat.getColor(context, R.color.colorTextMain));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mMainColor);
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText("30",
                getWidth() - getPaddingRight(),
                getPaddingBottom(),
                mTextPaint);
    }
}
