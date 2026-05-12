package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ChartView extends View {
    private List<Float> values = new ArrayList<>();
    private String chartType = "PieChart";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GRAY, Color.LTGRAY, Color.DKGRAY, Color.BLACK};

    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<Float> values, String chartType) {
        this.values = values;
        this.chartType = chartType;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || values.isEmpty()) return;

        if ("PieChart".equals(chartType)) {
            drawPieChart(canvas);
        } else if ("ColumnChart".equals(chartType)) {
            drawColumnChart(canvas);
        } else if ("BarChart".equals(chartType)) {
            drawBarChart(canvas);
        }
    }

    private void drawPieChart(Canvas canvas) {
        float total = 0;
        for (float v : values) total += v;

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float size = Math.min(getWidth(), getHeight()) - 400;
        RectF rectF = new RectF(centerX - size/2, centerY - size/2, centerX + size/2, centerY + size/2);
        
        float startAngle = 0;
        for (int i = 0; i < values.size(); i++) {
            float val = values.get(i);
            paint.setColor(colors[i % colors.length]);
            float sweepAngle = (val / total) * 360f;
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            
            // Draw value text
            float middleAngle = startAngle + sweepAngle / 2f;
            float labelRadius = size / 2f + 60;
            float x = (float) (centerX + labelRadius * Math.cos(Math.toRadians(middleAngle)));
            float y = (float) (centerY + labelRadius * Math.sin(Math.toRadians(middleAngle)));
            
            canvas.drawText(String.valueOf(val), x, y, textPaint);
            
            startAngle += sweepAngle;
        }
    }

    private void drawColumnChart(Canvas canvas) {
        float max = 0;
        for (float v : values) if (v > max) max = v;
        if (max == 0) max = 1;

        float width = getWidth();
        float height = getHeight();
        float availableWidth = width - 200;
        float barWidth = availableWidth / values.size();
        
        for (int i = 0; i < values.size(); i++) {
            float val = values.get(i);
            paint.setColor(colors[i % colors.length]);
            float barHeight = (val / max) * (height - 300);
            float left = 100 + i * barWidth;
            float top = height - 100 - barHeight;
            float right = 100 + (i + 1) * barWidth - 10;
            float bottom = height - 100;
            
            canvas.drawRect(left, top, right, bottom, paint);
            
            // Draw value text on top
            canvas.drawText(String.valueOf(val), (left + right) / 2f, top - 20, textPaint);
        }
    }

    private void drawBarChart(Canvas canvas) {
        float max = 0;
        for (float v : values) if (v > max) max = v;
        if (max == 0) max = 1;

        float width = getWidth();
        float height = getHeight();
        float availableHeight = height - 200;
        float barHeight = availableHeight / values.size();

        textPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < values.size(); i++) {
            float val = values.get(i);
            paint.setColor(colors[i % colors.length]);
            float barWidth = (val / max) * (width - 300);
            float left = 100;
            float top = 100 + i * barHeight;
            float right = 100 + barWidth;
            float bottom = 100 + (i + 1) * barHeight - 10;
            
            canvas.drawRect(left, top, right, bottom, paint);
            
            // Draw value text at the end
            canvas.drawText(String.valueOf(val), right + 10, (top + bottom) / 2f + 15, textPaint);
        }
        textPaint.setTextAlign(Paint.Align.CENTER); // Reset align
    }
}
