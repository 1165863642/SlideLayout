package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class SlideLayout extends FrameLayout {
    private Scroller scroller;

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    private View contentView, menuView;
    private int viewHeight, contentWidth, menuWidth;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = findViewById(R.id.slide_content);
        menuView = findViewById(R.id.slide_menu);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = getMeasuredHeight();
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);
    }

    private float startX, startY, downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = downX = event.getX();
                startY = downY = event.getY();
                closeMenu();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float distanceX = endX - startX;
                int toScroll = (int) (getScrollX() - distanceX);
                if (toScroll < 0)
                    toScroll = 0;
                if (toScroll > menuWidth)
                    toScroll = menuWidth;
                scrollTo(toScroll, getScrollY());
                startX = event.getX();
                float dx = Math.abs(event.getX() - downX);
                float dy = Math.abs(event.getY() - downY);
                if (dx > dy && dx > 6)
                    getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() > menuWidth / 2)
                    openMenu();
                else
                    closeMenu();
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = downX = ev.getX();
                startY = downY = ev.getY();
                if (onStateChangeListener != null)
                    onStateChangeListener.onMove(this);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(ev.getX() - downX);
                float dy = Math.abs(ev.getY() - downY);
                if (dx > dy && dx > 6)
                    return true;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void openMenu() {
        int dx = menuWidth - getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), dx, getScrollY());
        invalidate();
        if (onStateChangeListener != null)
            onStateChangeListener.onOpen(this);
    }

    public void closeMenu() {
        int dx = 0 - getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), dx, getScrollY());
        invalidate();
        if (onStateChangeListener != null)
            onStateChangeListener.onClose(this);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public interface OnStateChangeListener {
        void onOpen(SlideLayout slideLayout);

        void onMove(SlideLayout slideLayout);

        void onClose(SlideLayout slideLayout);
    }
}
