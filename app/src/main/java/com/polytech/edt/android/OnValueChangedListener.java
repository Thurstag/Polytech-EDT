package com.polytech.edt.android;

import android.widget.NumberPicker;

public interface OnValueChangedListener<T> {
    void onValueChange(NumberPicker picker, T _old, T _new);
}
