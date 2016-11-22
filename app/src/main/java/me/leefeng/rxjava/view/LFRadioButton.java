package me.leefeng.rxjava.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * 版权：北京航天世景信息技术有限公司
 * <p>
 * 作者：李利锋
 * <p>
 * 创建日期：2016/11/22 15:02
 * <p>
 * 描述：
 * <p>
 * 修改历史：
 */

public class LFRadioButton extends RadioButton {
    private  Paint paint;

    public LFRadioButton(Context context) {
        super(context);
    }

    public LFRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawLine(0,0,20,20,paint);
    }
}
