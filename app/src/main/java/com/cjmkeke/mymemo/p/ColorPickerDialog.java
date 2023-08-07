package com.cjmkeke.mymemo.p;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.EditText;

public class ColorPickerDialog {
    private Context context;
    private EditText editText;

    public ColorPickerDialog(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
    }

    public void showColorPickerDialog() {
        final String[] colors = {"Red", "Blue", "Green", "Yellow", "Purple"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("글씨 색상 선택");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedColor = colors[which];
                int colorValue = Color.BLACK;

                switch (selectedColor) {
                    case "Red":
                        colorValue = Color.RED;
                        break;
                    case "Blue":
                        colorValue = Color.BLUE;
                        break;
                    case "Green":
                        colorValue = Color.GREEN;
                        break;
                    case "Yellow":
                        colorValue = Color.YELLOW;
                        break;
                    case "Purple":
                        colorValue = Color.parseColor("#800080"); // Hex color for purple
                        break;
                }

                editText.setTextColor(colorValue);
            }
        });

        builder.show();
    }
}