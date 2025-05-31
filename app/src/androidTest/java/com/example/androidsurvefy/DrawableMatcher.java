package com.example.androidsurvefy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DrawableMatcher extends TypeSafeMatcher<View> {
    private final int expectedDrawableId;

    public DrawableMatcher(int expectedDrawableId) {
        this.expectedDrawableId = expectedDrawableId;
    }

    @Override
    protected boolean matchesSafely(View view) {
        if (expectedDrawableId < 0) return false;

        Context context = view.getContext();
        Drawable expectedDrawable = ContextCompat.getDrawable(context, expectedDrawableId);
        Drawable actualDrawable = view.getBackground();

        if (expectedDrawable == null || actualDrawable == null) return false;

        return actualDrawable.getConstantState() != null &&
                actualDrawable.getConstantState().equals(expectedDrawable.getConstantState());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with drawable id: " + expectedDrawableId);
    }
}