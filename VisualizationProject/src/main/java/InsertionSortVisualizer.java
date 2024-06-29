import javax.swing.*;

public class InsertionSortVisualizer extends VisualSorter {

    public InsertionSortVisualizer(int[] array, JPanel panel, JTextArea commentsArea, int delay, JScrollPane commentsScrollPane) {
        super(array, panel, commentsArea, delay, commentsScrollPane);
    }

    @Override
    public void sort() {
        new Thread(() -> {
            try {
                for (int i = 1; i < array.length; i++) {
                    int key = array[i];
                    int j = i - 1;
                    while (j >= 0 && array[j] > key) {
                        highlightedIndex = j;
                        appendComment("Moving " + array[j] + " to position " + (j + 1) + "\n");
                        array[j + 1] = array[j];
                        j = j - 1;
                        panel.repaint();
                        Thread.sleep(delay);
                        waitIfPaused();
                    }
                    highlightedIndex = j + 1;
                    appendComment("Inserting key " + key + " at position " + (j + 1) + "\n");
                    array[j + 1] = key;
                    panel.repaint();
                    Thread.sleep(delay);
                    waitIfPaused();
                }
                highlightedIndex = -1;
                panel.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
