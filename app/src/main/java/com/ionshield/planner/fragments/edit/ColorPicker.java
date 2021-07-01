package com.ionshield.planner.fragments.edit;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;

import com.ionshield.planner.R;
import com.ionshield.planner.view.RangeBar;

/**
 * Created by didim99 on 01.07.2021.
 */

class ColorPicker implements DialogInterface.OnShowListener {
    private static final int COLOR_VALUE_MIN = 0;
    private static final int COLOR_VALUE_MAX = 255;

    private final OnColorSelectedListener listener;
    private ColorHolder color;
    private View colorView;

    public ColorPicker(OnColorSelectedListener listener) {
        this.color = new ColorHolder();
        this.listener = listener;
    }

    public void setColor(@ColorInt int color) {
        this.color = new ColorHolder(color);
    }

    public void showDialog(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.color_picker_dialog)
                .setView(R.layout.dialog_color_picker)
                .setPositiveButton(R.string.button_ok, null)
                .setNegativeButton(R.string.button_cancel, null)
                .create();

        dialog.setOnShowListener(this);
        dialog.show();
    }

    @Override
    public void onShow(DialogInterface di) {
        AlertDialog dialog = (AlertDialog) di;
        RangeBar rbRed = dialog.findViewById(R.id.range_bar_red);
        RangeBar rbGreen = dialog.findViewById(R.id.range_bar_green);
        RangeBar rbBlue = dialog.findViewById(R.id.range_bar_blue);
        colorView = dialog.findViewById(R.id.color_view);

        colorView.setBackgroundColor(color.toArgb());
        rbRed.setBounds(COLOR_VALUE_MIN, COLOR_VALUE_MAX);
        rbGreen.setBounds(COLOR_VALUE_MIN, COLOR_VALUE_MAX);
        rbBlue.setBounds(COLOR_VALUE_MIN, COLOR_VALUE_MAX);
        rbRed.setValue(color.getRed());
        rbGreen.setValue(color.getGreen());
        rbBlue.setValue(color.getBlue());

        rbRed.setOnValueChangedListener(v ->
                onValueChanged(ColorHolder.Component.RED, v));
        rbGreen.setOnValueChangedListener(v ->
                onValueChanged(ColorHolder.Component.GREEN, v));
        rbBlue.setOnValueChangedListener(v ->
                onValueChanged(ColorHolder.Component.BLUE, v));

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    listener.onColorSelected(color);
                    di.dismiss();
                });
    }

    private void onValueChanged(ColorHolder.Component component, int value) {
        color.set(component, value);
        if (colorView != null)
            colorView.setBackgroundColor(color.toArgb());
    }

    static class ColorHolder {
        public enum Component {RED, GREEN, BLUE}

        private int red, green, blue;

        public ColorHolder() {
            this(Color.BLACK);
        }

        public ColorHolder(@ColorInt int color) {
            this.red = Color.red(color);
            this.green = Color.green(color);
            this.blue = Color.blue(color);
        }

        public int getRed() {
            return red;
        }

        public int getGreen() {
            return green;
        }

        public int getBlue() {
            return blue;
        }

        public void set(Component component, int value) {
            switch (component) {
                case RED:
                    red = value;
                    break;
                case GREEN:
                    green = value;
                    break;
                case BLUE:
                    blue = value;
                    break;
            }
        }

        public int toArgb() {
            return 0xff000000
                    | ((red & 0xff) << 16)
                    | ((green & 0xff) << 8)
                    | (blue & 0xff);
        }
    }

    public interface OnColorSelectedListener {
        void onColorSelected(ColorHolder color);
    }
}
