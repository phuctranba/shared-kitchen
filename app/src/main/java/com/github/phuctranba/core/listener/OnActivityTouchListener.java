package com.github.phuctranba.core.listener;

import android.view.MotionEvent;

/**
 * OnActivityTouchListener: Sự kiện cho việc swipe để xóa trong fragment danh sách công thức đã lưu
 * Xem thêm tại: https://medium.com/@velmm/android-recyclerview-with-swipe-menu-example-86259df97907
 * */
public interface OnActivityTouchListener {
    void getTouchCoordinates(MotionEvent ev);
}