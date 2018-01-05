package com.example.administrator.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View;

/**
 * Created by Administrator on 2018/1/3.
 */

public class CounterView extends View implements View.OnClickListener {

    private Paint mPaint ; //定义画笔
    private Rect mbounds ;//获取文字的宽和高
    private int count ; //计数器

    //构造器
    public CounterView (Context context , AttributeSet attributeSet ) {
        super( context , attributeSet ) ;
        mPaint = new Paint( Paint.ANTI_ALIAS_FLAG ) ;// 初始化画笔
        mbounds = new Rect( ) ;
        setOnClickListener( this );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor( Color.BLUE ) ;
        canvas.drawRect( 0 , 0 , getWidth() , getHeight() , mPaint );//4个参数分别是left,top,right,bottom表示把整个图放在左上角
        mPaint.setColor( Color.YELLOW ) ;
        mPaint.setTextSize( 50 ) ;
        String text = String.valueOf( count ) ;
        //获取文字的宽和高
        mPaint.getTextBounds( text , 0 , text.length() , mbounds );//4个参数分别是所需获取的字符串，开始的位置，末尾的位置，和Rect类的实例
        float textWidth = mbounds.width() ;
        float textHeight = mbounds.height() ;
        canvas.drawText( text , getWidth() / 2 - textWidth / 2 , getHeight() / 2+textHeight/2,mPaint );
    }

    @Override
    public void onClick(View v) {
        count ++ ;
        //重绘
        invalidate();
    }
}
