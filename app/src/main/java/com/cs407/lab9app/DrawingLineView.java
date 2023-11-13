package com.cs407.lab9app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

import java.util.List;

// A custom View class to draw a line on the screen based on a list of points.
public class DrawingLineView extends View {
    private List<PointF> points;
    private int lineColor;

    public DrawingLineView(Context context, List<PointF> points, int lineColor) {
        super(context);
        this.points = points;
        this.lineColor = lineColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint pen = new Paint();

        pen.setColor(lineColor);
        pen.setStrokeWidth(8.0f);
        pen.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        // to move the starting point of the path to the first point in the points list
        path.moveTo(points.get(0).x, points.get(0).y);
        // called for each point to create a connected series of line segments.
        for (PointF point : points) {
            path.lineTo(point.x, point.y);
        }
        path.close();

        canvas.drawPath(path, pen);
    }
}
