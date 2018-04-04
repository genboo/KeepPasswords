package ru.devsp.apps.keeppasswords.view.components;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Анимация скрытия кнопки
 * Created by gen on 20.10.2017.
 */
@SuppressWarnings("unused")
public class FabScrollingBehavior extends FloatingActionButton.Behavior {

    public FabScrollingBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type) {
        if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(5f)).start();
        } else if (dyConsumed < 0) {
            child.animate().translationY(0).setInterpolator(new AccelerateInterpolator(5f)).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target,
                                       @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }


}
