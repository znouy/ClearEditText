package com.example.clearedittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

public class ClearEditText extends EditText {

	private Drawable mClearDrawable;

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClearEditText(Context context) {
		this(context, null);
	}

	private void init() {
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {// 没有设置图片就是用默认图片
			mClearDrawable = getResources()
					.getDrawable(R.drawable.search_clear);
		}

		// getIntrinsicWidth() 返回drawable最小的宽(不小于)
		// setBounds()去设置绘制的范围
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());

		// 设置默认不显示
		setClearDrawableVisible(false);

		// 设置焦点变化监听
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// 判断焦点是否发生变化以及文本框里是否有内容
				boolean isHasFocus = hasFocus;
				if (isHasFocus) {
					setClearDrawableVisible(getText().toString().length() >= 1);
				} else {
					setClearDrawableVisible(false);
				}

			}
		});

		// 设置文本改变发生变化时的监听
		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// 当输入结束后判断是否显示清楚图标
				setClearDrawableVisible(getText().toString().length() >= 1);
			}
		});
	}

	protected void setClearDrawableVisible(boolean isVisible) {

		Drawable rightDrawable;
		if (isVisible) {
			rightDrawable = mClearDrawable;
		} else {
			rightDrawable = null;
		}
		// 设置该控件left, top, right, and bottom处的图标
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], rightDrawable,
				getCompoundDrawables()[3]);

	}

	// 设置动画，提醒用户输入
	public void setAnimation() {
		Animation transAnimation = shakeAnimation(5);
		this.setAnimation(transAnimation);
	}

	private Animation shakeAnimation(float cycleTimes) {
		Animation transAnimation = new TranslateAnimation(0, 10, 0, 10);
		transAnimation.setInterpolator(new CycleInterpolator(cycleTimes));
		transAnimation.setDuration(1000);
		return transAnimation;
	}

	/*
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
	 * getPaddingRight():clean的图标右边缘至控件右边缘的距离
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (getCompoundDrawables()[2] != null) {
				boolean isClean = event.getX() > (getWidth() - getTotalPaddingRight())
						&& event.getX() < (getWidth() - getPaddingRight());
				if (isClean) {
					setText("");// 清空文本
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
