package pt.isec.mindunlocker.pt.isec.mindunlocker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class SudokuCell extends BaseSudokuCell {

    private Paint nPaint;

    static SudokuCell[][] instance = null;

    public SudokuCell(Context context) {
        super(context);
        nPaint = new Paint();
    }


    /**
     * Get or create a instance from himself
     * @return <code>SudokuCell[][]</code>
     */
    public static SudokuCell[][] getInstance(){
        if(instance == null){
            instance = new SudokuCell[9][9];
        }
        return instance;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawNumbers(canvas);
        drawLines(canvas);
    }

    /**
     * Draw a number in the cell to be visible
     * @param canvas <code>Canvas</code> object belongs view
     */
    private void drawNumbers(Canvas canvas){
        nPaint.setColor(Color.BLACK);
        nPaint.setTextSize(60);
        nPaint.setStyle(Paint.Style.FILL);

        Rect bounds = new Rect();

        nPaint.getTextBounds(String.valueOf(getValue()),0,String.valueOf(getValue()).length(),bounds);

        if(!isModifiable()){
            nPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        //check is value is wrong
        if (isWrong()) {
            //if wrong paints the cell red
            this.setBackgroundColor(Color.RED);
        } else if(isGuess()){
            //if guess selected prints the cell blue
            nPaint.setColor(Color.BLUE);
            nPaint.setTextSize(40);
        }else{
            //if not wrong prints the cell white
            this.setBackgroundColor(Color.WHITE);
        }

        //get cell number
        if(getValue() != 0){
            canvas.drawText(String.valueOf(getValue()),
                    (getWidth() - bounds.width())/2,
                    (getHeight() + bounds.height()) / 2, nPaint);
        }
    }

    /**
     * Draw all table lines
     * @param canvas <code>Canvas</code> object belongs view
     */
    private void drawLines(Canvas canvas){
        nPaint.setColor(Color.BLACK);
        nPaint.setStrokeWidth(2);
        nPaint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(0,0, getWidth(), getHeight(), nPaint);

        //auxiliary line drawing
        nPaint.setColor(Color.BLACK);
        nPaint.setStrokeWidth(10);
        nPaint.setStyle(Paint.Style.STROKE);
        if(getY() == 0 || getY() == 3*getHeight() || getY() == 6*getHeight()) {
            canvas.drawLine(0, 0, getWidth(), 0, nPaint);
        }

        if(getX() == 0 || getX() == 3*getHeight() || getX() == 6*getHeight()) {
            canvas.drawLine(0, 0, 0, getWidth(), nPaint);
        }
        if(getY() == 8*getHeight()) {
            canvas.drawLine(0, getHeight(), getHeight(), getHeight(), nPaint);
        }
        if(getX() == 8*getHeight()) {
            canvas.drawLine(getHeight(), 0, getHeight(), getHeight(), nPaint);
        }
    }
}
