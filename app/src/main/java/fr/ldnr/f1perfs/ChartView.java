package fr.ldnr.f1perfs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kévin on 21/04/2017.
 */

public class ChartView extends View
{

    private RectF rect = new RectF();
    private Paint paint = new Paint();
    private int percentage;

    public ChartView(Context context)
    {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setPercentage(int percentage)
    {
        this.percentage = percentage;
        invalidate();
    }

    public int getPercentage()
    {
        return percentage;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > h)
        {
            rect.set(w / 2 - h / 2, 0, w / 2 + h / 2, h);
        }
        else
        {
            rect.set(0, h / 2 - w / 2, w, h / 2 + w / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        paint.setColor(Color.RED);
        canvas.drawArc(rect, 0, 360 * percentage / 100, true, paint);

        paint.setColor(Color.BLUE);
        canvas.drawArc(rect, 360 * percentage / 100, 360 - 360 * percentage / 100, true, paint);
    }

}
