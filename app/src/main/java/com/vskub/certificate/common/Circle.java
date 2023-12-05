package com.vskub.certificate.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HP PC on 29-03-2018.
 */

public class Circle extends View {

    private static final int START_ANGLE_POINT = 0;

    private final Paint paint;
    private final RectF rect;

    private float angle;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int strokeWidth = 100;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        //Circle color
        paint.setColor(Color.parseColor("#10DDE7"));

        //size 200x200 example
        rect = new RectF(strokeWidth, strokeWidth, 400 + strokeWidth, 400 + strokeWidth);

        //Initial Angle (optional, it can be zero)
        angle = 120;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
    }

    public float getAngle() {
        return angle;
    }

    public void setPaintColor(String color){
        paint.setColor(Color.parseColor(color));
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
