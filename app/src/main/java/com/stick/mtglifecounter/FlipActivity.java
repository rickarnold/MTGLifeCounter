package com.stick.mtglifecounter;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class FlipActivity extends AppCompatActivity {

    private final int ANIMATION_COUNT = 10 - ((int) System.currentTimeMillis() % 2);
    private boolean isHeads = false;
    private int animationCounter = 0;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);

        mDetector = new GestureDetectorCompat(this, new FlipGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void startScaleAnimation()
    {
        int centerX = getImageView().getWidth() / 2;

        final ScaleAnimation animationShrink = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, centerX, 0);
        final ScaleAnimation animationGrow = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, centerX, 0);

        animationShrink.setDuration(getDuration());
        animationGrow.setDuration(getDuration());

        animationShrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationGrow.setDuration(getDuration());
                getImageView().startAnimation(animationGrow);
                toggleImage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationGrow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (continueAnimate()) {
                    animationShrink.setDuration(getDuration());
                    getImageView().startAnimation(animationShrink);
                }
                else
                    displayFinished();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        getImageView().startAnimation(animationShrink);
    }

    private int getDuration()
    {
        double val1 = (1.0d - Math.pow((1.0d - (animationCounter / (double) ANIMATION_COUNT)), 1.0d));
        return  (int) (val1 * 500.0d);
    }

    private void toggleImage()
    {
        ImageView flipImageView = getImageView();
        if (isHeads)
            flipImageView.setImageResource(R.drawable.card_back);
        else
            flipImageView.setImageResource(R.drawable.shivan_dragon);

        isHeads = !isHeads;
    }

    private boolean continueAnimate()
    {
        animationCounter++;
        return animationCounter < ANIMATION_COUNT;
    }

    private void displayFinished()
    {
        TextView textView = (TextView) findViewById(R.id.heads_or_tails);
        if (isHeads)
            textView.setText("HEADS");
        else
            textView.setText("TAILS");
        textView.setVisibility(View.VISIBLE);

        setCloseListener();
    }

    private void setCloseListener()
    {
        ImageView flipImageView = (ImageView) findViewById(R.id.flip_image);
        flipImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private ImageView getImageView()
    {
        return (ImageView) findViewById(R.id.flip_image);
    }

    class FlipGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            startScaleAnimation();
            return true;
        }
    }
}

