/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.android;

import android.widget.NumberPicker;

public interface OnValueChangedListener<T> {
    void onValueChange(NumberPicker picker, T _old, T _new);
}
