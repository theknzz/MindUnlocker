package pt.isec.mindunlocker.pt.isec.mindunlocker.view;

import android.content.Context;
import android.view.View;

public class BaseSudokuCell extends View {

    private int value;
    private boolean modifiable = true;
    private boolean guess = false;
    private boolean isWrong = false;
    private int position;

    public void setNotModifiable() {
        this.modifiable = false;
    }
    public BaseSudokuCell(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public int getValue() {
        return value;
    }

    public void setInitValue(int value){
        this.value = value;
        invalidate();
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public boolean isGuess() {
        return guess;
    }

    public boolean isWrong() {
        return isWrong;
    }

    public void setWrong(boolean wrong) {
        isWrong = wrong;
    }

    public void setGuess(boolean guess) {
        this.guess = guess;
    }

    public void setValue(int value){
        if( modifiable ){
            this.value = value;
        }
        invalidate();
    }

    public int getPosition() {
        return position;
    }

}
