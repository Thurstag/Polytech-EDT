/*
 * Copyright 2018-2022 Polytech-EDT
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

import com.google.common.base.Function;
import com.polytech.edt.R;
import com.polytech.edt.model.BasicEnum;
import com.polytech.edt.util.ListUtil;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

public class EnumPickerDialog<T extends BasicEnum> extends AlertDialog {

    private NumberPicker numberPicker;
    private OnValueChangedListener<T> listener;
    private Collection<T> enums;
    private T _default;

    public EnumPickerDialog(@NonNull Context context, final Collection<T> enums, T _default) {
        super(context);
        this.enums = enums;
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
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(enums.size() - 1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(ListUtil.select(enums, new Function<T, String>() {
            @Override
            public String apply(T input) {
                return input.label();
            }
        }).toArray(new String[0]));
        int indexOf = ListUtil.indexOf(enums, _default);
        numberPicker.setValue(indexOf < 0 ? 0 : indexOf);

        // Define listener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (listener != null) {
                    listener.onValueChange(picker, (T) CollectionUtils.get(enums, oldVal), (T) CollectionUtils.get(enums, newVal));
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

    public void setOnValueChangeListener(OnValueChangedListener<T> listener) {
        this.listener = listener;
    }
}
