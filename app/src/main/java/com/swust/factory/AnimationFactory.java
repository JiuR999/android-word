package com.swust.factory;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.annotation.NonNull;

public class AnimationFactory {

    public static ObjectAnimator createSlideOutAnimation(View target) {
        ObjectAnimator slideOutAnimation = ObjectAnimator.ofFloat(target, "translationX", 0, target.getWidth());
        slideOutAnimation.setDuration(500);
        slideOutAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                target.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
        return slideOutAnimation;
    }

    public static ObjectAnimator createSlideInAnimation(View target) {
        ObjectAnimator slideInAnimation = ObjectAnimator.ofFloat(target, "translationX", target.getWidth(), 0);
        slideInAnimation.setDuration(500);
        slideInAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                target.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                target.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
        return slideInAnimation;
    }
}
