package com.jochef2.campingdiary.ui.main;

import android.animation.LayoutTransition;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.jochef2.campingdiary.R;
import com.jochef2.campingdiary.ui.views.DraggableCoordinatorLayout;

public class ShowReiseFragment extends Fragment {

    MaterialCardView cardView;
    MaterialCardView card2;
    private final View.AccessibilityDelegate cardDelegate = new View.AccessibilityDelegate() {
        @Override
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return;
            }

            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) cardView
                    .getLayoutParams();
            int gravity = layoutParams.gravity;
            boolean isOnLeft = (gravity & Gravity.LEFT) == Gravity.LEFT;
            boolean isOnRight = (gravity & Gravity.RIGHT) == Gravity.RIGHT;
            boolean isOnTop = (gravity & Gravity.TOP) == Gravity.TOP;
            boolean isOnBottom = (gravity & Gravity.BOTTOM) == Gravity.BOTTOM;
            boolean isOnCenter = (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL;

            if (!(isOnTop && isOnLeft)) {
                info.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.move_card_top_left_action,
                        getString(R.string.cat_card_action_move_top_left)));
            }
            if (!(isOnTop && isOnRight)) {
                info.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.move_card_top_right_action,
                        getString(R.string.cat_card_action_move_top_right)));
            }
            if (!(isOnBottom && isOnLeft)) {
                info.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.move_card_bottom_left_action,
                        getString(R.string.cat_card_action_move_bottom_left)));
            }
            if (!(isOnBottom && isOnRight)) {
                info.addAction(new AccessibilityNodeInfo.AccessibilityAction(
                        R.id.move_card_bottom_right_action,
                        getString(R.string.cat_card_action_move_bottom_right)));
            }
            if (!isOnCenter) {
                info.addAction(new AccessibilityNodeInfo.AccessibilityAction(
                        R.id.move_card_center_action,
                        getString(R.string.cat_card_action_move_center)));
            }
        }

        @Override
        public boolean performAccessibilityAction(View host, int action, Bundle arguments) {
            int gravity;
            if (action == R.id.move_card_top_left_action) {
                gravity = Gravity.TOP | Gravity.LEFT;
            } else if (action == R.id.move_card_top_right_action) {
                gravity = Gravity.TOP | Gravity.RIGHT;
            } else if (action == R.id.move_card_bottom_left_action) {
                gravity = Gravity.BOTTOM | Gravity.LEFT;
            } else if (action == R.id.move_card_bottom_right_action) {
                gravity = Gravity.BOTTOM | Gravity.RIGHT;
            } else if (action == R.id.move_card_center_action) {
                gravity = Gravity.CENTER;
            } else {
                return super.performAccessibilityAction(host, action, arguments);
            }

            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) cardView
                    .getLayoutParams();
            if (layoutParams.gravity != gravity) {
                layoutParams.gravity = gravity;
                cardView.requestLayout();
                card2.requestLayout();
            }

            return true;
        }
    };
    private ShowReiseViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_reise_fragment, container, false);

        DraggableCoordinatorLayout parent = (DraggableCoordinatorLayout) view;

        LayoutTransition transition = ((CoordinatorLayout) view).getLayoutTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            transition.enableTransitionType(LayoutTransition.CHANGING);
        }

        cardView = view.findViewById(R.id.card);
        cardView.setAccessibilityDelegate(cardDelegate);
        card2 = view.findViewById(R.id.card2);
        card2.setAccessibilityDelegate(cardDelegate);

        parent.addDraggableChild(cardView);
        parent.addDraggableChild(card2);
        parent.setViewDragListener(new DraggableCoordinatorLayout.ViewDragListener() {
            @Override
            public void onViewCaptured(@NonNull View view, int i) {
                cardView.setDragged(true);
            }

            @Override
            public void onViewReleased(@NonNull View view, float v, float v1) {
                cardView.setDragged(false);
            }
        });

        return view;
    }
}