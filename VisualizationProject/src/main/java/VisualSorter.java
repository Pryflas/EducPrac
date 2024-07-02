import javax.swing.*;
import java.util.Arrays;

public abstract class VisualSorter {
    protected int[] array;
    protected JPanel panel;
    protected JTextArea commentsArea;
    protected int delay;
    protected JScrollPane commentsScrollPane;
    protected JTextField sortedArrayField;
    private int highlightedIndex = -1;
    private int comparedIndex = -1;
    private int leftIndex = -1;
    private int midIndex = -1;
    private int rightIndex = -1;
    private int BubbleIndex = -1;
    private int BubbleIndex2 = -1;
    private volatile boolean paused = false;

    public VisualSorter(int[] array, JPanel panel, JTextArea commentsArea, int delay, JScrollPane commentsScrollPane, JTextField sortedArrayField) {
        this.array = array;
        this.panel = panel;
        this.commentsArea = commentsArea;
        this.delay = delay;
        this.commentsScrollPane = commentsScrollPane;
        this.sortedArrayField = sortedArrayField;
    }

    public abstract void sort();

    protected void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



    public void setDelay(int delay) {
        this.delay = delay;
    }

    protected void updatePanel() {
        panel.repaint();

    }

    protected void addComment(String comment) {
        commentsArea.append(comment + "\n");
        JScrollBar vertical = commentsScrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    protected void setHighlightedIndex(int index) {
        this.highlightedIndex = index;
    }

    public int getHighlightedIndex() {
        return highlightedIndex;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    protected void pauseIfNeeded() {
        while (paused) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected void setSortedArray(int[] array) {
        sortedArrayField.setText(Arrays.toString(array));
    }

    public int getComparedIndex() {
        return comparedIndex;
    }

    protected void setComparedIndex(int index) {
        this.comparedIndex = index;
    }

    protected void setLeftIndex(int index) {
        this.leftIndex = index;
    }

    protected void setRightIndex(int index) {
        this.rightIndex = index;
    }

    protected void setMidIndex(int index) {
        this.midIndex = index;
    }

    public int getLeftIndex() {
        return leftIndex;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public int getMidIndex() {
        return midIndex;
    }

    public void setBubbleIndex(int index) {
        this.BubbleIndex = index;
    }

    public int getBubbleIndex() {
        return BubbleIndex;
    }

    public void setBubbleIndex2(int index) {
        this.BubbleIndex2 = index;
    }

    public int getBubbleIndex2() {
        return BubbleIndex2;
    }
}
