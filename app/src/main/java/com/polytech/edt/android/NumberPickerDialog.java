/*
 * Copyright 2018-2021 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
package com.polytech.edt.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.polytech.edt.R;

public class NumberPickerDialog extends AlertDialog {

    private NumberPicker numberPicker;
    private OnValueChangedListener<Integer> listener;
    private int from;
    private int to;
    private int _default;

    public NumberPickerDialog(@NonNull Context context, int from, int to, int _default) {
        super(context);
        this.from = from;
        this.to = to;
        this._default = _default;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.picker_content, null);

        // Set content
        setView(view);

        // Get number picker
        numberPicker = view.findViewById(R.id.dialog_picker);

        // Define picker
        numberPicker.setMinValue(from);
        numberPicker.setMaxValue(to);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(_default - numberPicker.getMinValue());

        // Define listener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (listener != null) {
                    listener.onValueChange(picker, oldVal, newVal);
                }
            }
        });

        // Define dismiss button
        setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        super.onCreate(savedInstanceState);
    }

    public void setOnValueChangeListener(OnValueChangedListener<Integer> listener) {
        this.listener = listener;
    }
}
