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

    /**
     * Cell return value
     * @return <code>int</code> value by cell
     */
    public int getValue() {
        return value;
    }

    /**
     * Set a value in a cell
     * @param value <code>int</code> set <var>value</var> to cell
     */
    public void setInitValue(int value){
        this.value = value;
        invalidate();
    }

    /**
     * Verify if value can be changed
     * @return <code>boolean</code> <code>true</code> if value from cell can be changed and
     * <code>false</code> if not
     */
    public boolean isModifiable() {
        return modifiable;
    }

    /**
     *
     * @return <code>boolean</code>
     */
    public boolean isGuess() {
        return guess;
    }

    /**
     * Verify if cell have a wrong value
     * @return <code>boolean</code> <code>true</code> if value is wrong and
     * <code>false</code> if value is correct
     */
    public boolean isWrong() {
        return isWrong;
    }

    /**
     * Set <var>isWrong</var>
     * @param wrong <code>boolean</code>
     */
    public void setWrong(boolean wrong) {
        isWrong = wrong;
    }

    /**
     * Set <var>guess</var>
     * @param guess <code>boolean</code>
     */
    public void setGuess(boolean guess) {
        this.guess = guess;
    }

    /**
     * Set value in <var>value</var> if the cell was no modifiable
     * @param value <code>int</code>
     */
    public void setValue(int value){
        if( modifiable ){
            this.value = value;
        }
        invalidate();
    }

    /**
     * Get position
     * @return <var>position</var> from cell
     */
    public int getPosition() {
        return position;
    }

}
