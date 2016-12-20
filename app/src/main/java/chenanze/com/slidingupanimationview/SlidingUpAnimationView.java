package chenanze.com.slidingupanimationview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import static chenanze.com.slidingupanimationview.SlidingUpAnimationView.MotionPath.BOTTOM_TO_MIDDLE;

/**
 * Created by duian on 2016/10/24.
 */

public class SlidingUpAnimationView extends RelativeLayout {

    private final String TAG = getClass().getSimpleName();
    private final String TAG1 = "tag1";
    private int mLastX;
    private int mLastY;
    private Scroller mScroller;
    private int mScreenHeight;
    private int mScreenHeightHaft;
    private int mOffsetY;
    private LayoutParams mLayoutParams;
    private int mLayoutFullHeight;
    private MotionPath mMotionPath = BOTTOM_TO_MIDDLE;
    private int mLayoutHalfHeight;
    int[] mLocation = new int[2];
    private Context mContext;
    private int mWindowsHeight;
    private int mWindowsWeight;
    private int mNavigationBarHeight;
    private int mStatusBarHeight;
    private int mScreenWidth;
    private int mBottomCoordsY;
    private int mTopCoordsY;
    private int mHeadViewOffset;

    public SlidingUpAnimationView(Context context) {
        super(context);
        init(context);
    }

    public SlidingUpAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlidingUpAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setBackgroundColor(Color.BLUE);
        mScroller = new Scroller(context);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Now the layout of ViewGroup is completed
                initConstant();
            }
        });
    }

    public void initConstant() {

        mScreenHeight = ScreenUtils.getScreenHeight(this);
        mScreenWidth = ScreenUtils.getScreenWidth(this);
        mWindowsHeight = ScreenUtils.getWindowsHeight(this);
        mWindowsWeight = ScreenUtils.getWindowsWidth(this);
        mNavigationBarHeight = ScreenUtils.getNavigationBarHeight(this);
        mStatusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mBottomCoordsY = calculateBottomCoordsY();
        mTopCoordsY = calculateTopCoordsY();
        Log.d(TAG, "mScreenHeight: " + mScreenHeight);
        Log.d(TAG, "mScreenWidth: " + mScreenWidth);
        Log.d(TAG, "mWindowsHeight: " + mWindowsHeight);
        Log.d(TAG, "mWindowsWeight: " + mWindowsWeight);
        Log.d(TAG, "mNavigationBarHeight: " + mNavigationBarHeight);
        Log.d(TAG, "mStatusBarHeight: " + mStatusBarHeight);
        Log.d(TAG, "mBottomCoordsY: " + mBottomCoordsY);
        Log.d(TAG, "mTopCoordsY: " + mTopCoordsY);

        setInitPoint(mScreenWidth, mScreenHeight, mNavigationBarHeight);
    }

    public int calculateBottomCoordsY() {
        return mScreenHeight - mNavigationBarHeight;
    }

    public int calculateTopCoordsY() {
        return mStatusBarHeight;
    }

    public int getHeadViewHeightPx() {
        View view = getChildAt(0);
        return view.getHeight();
    }

    public int getHeadViewHeightDp() {
        return (int) ScreenUtils.dpFromPx(mContext, getHeadViewHeightPx());
    }

    public void setInitPoint(int screenWidth, int screenHeight, int navigationBarHight) {
        mScreenHeight = screenHeight - navigationBarHight;
        mScreenHeightHaft = mScreenHeight / 2;

        mHeadViewOffset = 5;
        int marginTop = mScreenHeight - getHeadViewHeightDp() - mHeadViewOffset;
        mLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
//        mLayoutParams.height = mScreenHeight + navigationBarHight;
        mLayoutFullHeight = mScreenHeight + navigationBarHight;
        mLayoutHalfHeight = mScreenHeightHaft + navigationBarHight;

        mLayoutParams.height = mLayoutHalfHeight;
        mLayoutParams.setMargins(0, marginTop, 0, screenHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        View viewGroup = (View) getParent();
//        getLocationOnScreen();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //int offsetX = x - mLastX;
                //int offsetY = y - mLastY;
                //此时，计算坐标是相反的
                int offsetX = mLastX - x;
                mOffsetY = mLastY - y;
                //让View所在的ViewGroup进行移动
                int locationY = viewGroup.getScrollY();
//                Log.d(TAG1, "y: " + viewGroup.getScrollY());
//                Log.d(TAG1, "locationY: " + mLocation[1]);
//                Log.d(TAG1, "mTopCoordsY: " + mTopCoordsY);
//                Log.d(TAG1, "mBottomCoordsY: " + mBottomCoordsY);
                if (locationY >= 0 && locationY <= mBottomCoordsY) {
                    viewGroup.scrollBy(0, mOffsetY);
                }
                if (mOffsetY > 0) {
                    if (mMotionPath == BOTTOM_TO_MIDDLE || mMotionPath == MotionPath.TOP_TO_MIDDLE) {
                        mLayoutParams.height = mLayoutFullHeight;
                        setLayoutParams(mLayoutParams);
                        Log.d(TAG, "------->full");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollY = viewGroup.getScrollY();
                Log.d(TAG1, "scrollY: " + scrollY);

                if (mOffsetY > 0) {
                    if (scrollY < mScreenHeightHaft) { // bottom to middle
                        int scrollY1 = viewGroup.getScrollY();
                        Log.d(TAG1, "viewGroup.getScrollX(): " + scrollY1);
                        mScroller.startScroll(viewGroup.getScrollX(), scrollY1, 0, mScreenHeightHaft - viewGroup.getScrollY(), 600);
                        mMotionPath = BOTTOM_TO_MIDDLE;
                        Log.d(TAG1, "onTouchEvent: bottom to middle");
                        Log.d(TAG1, "viewGroup.getScrollY(): " + viewGroup.getScrollY());
                    } else { // middle to top
                        int scrollY1 = viewGroup.getScrollY();
                        Log.d(TAG1, "viewGroup.getScrollX(): " + scrollY1);
//                        mScroller.startScroll(viewGroup.getScrollX(), scrollY1, 0, mScreenHeight - viewGroup.getScrollY() - ((int) ScreenUtils.dpFromPx(mContext, mStatusBarHeight) + 10), 600);
                        mScroller.startScroll(viewGroup.getScrollX(), scrollY1, 0, mScreenHeight - viewGroup.getScrollY() - mStatusBarHeight, 600);
                        mMotionPath = MotionPath.MIDDLE_TO_TOP;
                        Log.d(TAG1, "onTouchEvent: middle to top");
                        Log.d(TAG1, "viewGroup.getScrollY(): " + viewGroup.getScrollY());
                    }
                } else {
                    if (scrollY < mScreenHeightHaft) { // middle to bottom
                        int scrollY1 = viewGroup.getScrollY();
                        Log.d(TAG1, "viewGroup.getScrollX(): " + scrollY1);
                        mScroller.startScroll(viewGroup.getScrollX(), scrollY1, 0, -viewGroup.getScrollY(), 600);
                        mMotionPath = MotionPath.MIDDLE_TO_BOTTOM;
                        Log.d(TAG1, "onTouchEvent: middle to bottom");
                        Log.d(TAG1, "viewGroup.getScrollY(): " + viewGroup.getScrollY());
                    } else { // top to middle
                        int scrollY1 = viewGroup.getScrollY();
                        Log.d(TAG1, "viewGroup.getScrollX(): " + scrollY1);
                        mScroller.startScroll(viewGroup.getScrollX(), scrollY1, 0, mScreenHeightHaft - viewGroup.getScrollY(), 600);
                        mMotionPath = MotionPath.TOP_TO_MIDDLE;
                        mLayoutParams.height = mLayoutHalfHeight;
                        setLayoutParams(mLayoutParams);
                        Log.d(TAG1, "onTouchEvent: top to middle");
                        Log.d(TAG1, "viewGroup.getScrollY(): " + viewGroup.getScrollY());
                    }
                }

                //记住需要 invalidate
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //记住，需要不断调用invalidate进行重绘
//            this.getLocationInWindow(mLocation);
            invalidate();
        }
    }

    public enum MotionPath {
        BOTTOM_TO_MIDDLE,
        MIDDLE_TO_TOP,
        TOP_TO_MIDDLE,
        MIDDLE_TO_BOTTOM
    }

}
