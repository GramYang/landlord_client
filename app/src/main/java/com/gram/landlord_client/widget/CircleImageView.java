package com.gram.landlord_client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.gram.landlord_client.R;

/**
 * 原型的ImageView
 */
public class CircleImageView extends AppCompatImageView {
    //缩放类型
    private static final ImageView.ScaleType SCALE_TYPE = ImageView.ScaleType.CENTER_CROP;
    //默认边框宽度
    private static final int DEFAULT_BORDER_WIDTH = 0;
    private int borderWidth = DEFAULT_BORDER_WIDTH;
    //默认边框颜色
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private int borderColor = DEFAULT_BORDER_COLOR;
    //图片矩形和边框矩形
    private final RectF drawableRect = new RectF();
    private final RectF borderRect = new RectF();
    //矩阵变换
    private final Matrix shaderMatrix = new Matrix();
    //图片和边框绘制
    private final Paint bitmapPaint = new Paint();
    private final Paint borderPaint = new Paint();
    //图片和边框半径
    private float drawableRadius;
    private float borderRadius;
    //图片的宽和高
    private int bitmapWidth;
    private int bitmapHeight;
    //用于处理构造函数的执行和setup方法执行的时间差
    private boolean ready;
    private boolean setupPending;
    //默认为圆形
    private boolean isDisplayCircle = true;
    private Bitmap bitmap;
    private BitmapShader bitmapShader;
    //生成bitmap属性
    private static final int COLOR_DRAWABLE_DIMENSION = 1;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setScaleType(SCALE_TYPE);
        //获取layout中定义的控件属性，这里需要在attrs.xml中定义<declare-styleable>
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);
        borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        borderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
        a.recycle();
        ready = true;
        if(setupPending) {
            setup();
            setupPending = false;
        }
    }

    private void setup() {
        //如果构造器没有被调用，则在构造器中调用setup()
        if(!ready) {
            setupPending = true;
            return;
        }
        //bitmap必须不为空
        if(bitmap == null) return;
        //初始化bitmapPaint
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setShader(bitmapShader);
        //初始化borderPaint
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        //获取bitmap宽高
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        //边框矩形和半径
        borderRect.set(0, 0, getWidth(), getHeight());
        borderRadius = Math.min((borderRect.height() - borderWidth) / 2, (borderRect.width() - borderWidth) / 2);
        //图片矩形和半径
        drawableRect.set(borderWidth, borderWidth, borderRect.width() - borderWidth, borderRect.height() - borderWidth);
        drawableRadius = Math.min(drawableRect.height() / 2, drawableRect.width() / 2);
        //矩阵变换
        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;
        shaderMatrix.set(null);
        if(bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / (float) bitmapHeight;
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f;
        } else {
            scale = drawableRect.width() / (float) bitmapWidth;
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f;
        }
        shaderMatrix.setScale(scale, scale);
        shaderMatrix.postTranslate((int) (dx + 0.5f) + borderWidth, (int) (dy + 0.5f) + borderWidth);
        bitmapShader.setLocalMatrix(shaderMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //非圆形则委托给ImageView的onDraw
        if(!isDisplayCircle) {
            super.onDraw(canvas);
            return;
        }
        //必须设置了drawable
        if(getDrawable() == null) return;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, drawableRadius, bitmapPaint);
        if(borderWidth != 0)
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, borderRadius, borderPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        bitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        bitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = bm;
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if(drawable == null) return null;
        if(drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap;
        if(drawable instanceof ColorDrawable) {
            bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
