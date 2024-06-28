import javax.swing.*;

public abstract class VisualSorter {
    protected int[] array;
    protected JPanel panel;
    protected JTextArea commentsArea;
    protected JScrollPane commentsScrollPane;
    protected int delay;
    protected int highlightedIndex = -1;
    protected boolean paused = false;

    public VisualSorter(int[] array, JPanel panel, JTextArea commentsArea, int delay, JScrollPane commentsScrollPane) {
        this.array = array;
        this.panel = panel;
        this.commentsArea = commentsArea;
        this.delay = delay;
        this.commentsScrollPane = commentsScrollPane;
    }

    public abstract void sort();

    protected void waitIfPaused() {
        while (paused) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getHighlightedIndex() {
        return highlightedIndex;
    }

    protected void appendComment(String comment) {
        commentsArea.append(comment);
        JScrollBar verticalScrollBar = commentsScrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }
}
