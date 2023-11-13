package com.cs407.lab9app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

// This view is designed to display a bounding box (a rectangular shape) with a label inside it.
public class DrawingView extends View {
    private final float MAX_FONT_SIZE = 96F;
    private Rect boundingBox;
    private String label;
    private int boxColor;
    private int textColor;

    public DrawingView(Context context, Rect boundingBox, String label, int boxColor, int textColor) {
        super(context);
        this.boundingBox = boundingBox;
        this.label = label;
        this.boxColor = boxColor;
        this.textColor = textColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint pen = new Paint();
        pen.setTextAlign(Paint.Align.LEFT);

        pen.setColor(boxColor);
        pen.setStrokeWidth(8F);
        pen.setStyle(Paint.Style.STROKE);
        canvas.drawRect(boundingBox, pen);

        // Draw label
        Rect tagSize = new Rect(0, 0, 0, 0);

        // Calculate the right font size
        pen.setStyle(Paint.Style.FILL_AND_STROKE);
        pen.setColor(textColor);
        pen.setStrokeWidth(2F);

        pen.setTextSize(MAX_FONT_SIZE);
        pen.getTextBounds(label, 0, label.length(), tagSize);
        float fontSize = pen.getTextSize() * boundingBox.width() / tagSize.width();

        // Adjust the font size so texts are inside the bounding box
        if (fontSize < pen.getTextSize()) pen.setTextSize(fontSize);

        float margin = (boundingBox.width() - tagSize.width()) / 2.0F;
        if (margin < 0F) margin = 0F;

        // Draw tags onto bitmap (bmp is in upside-down format)
        canvas.drawText(label, boundingBox.left + margin, boundingBox.top + tagSize.height(), pen);
    }
}
