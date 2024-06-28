import javax.swing.*;

public class BubbleSortVisualizer extends VisualSorter {

    public BubbleSortVisualizer(int[] array, JPanel panel, JTextArea commentsArea, int delay, JScrollPane commentsScrollPane) {
        super(array, panel, commentsArea, delay, commentsScrollPane);
    }

    @Override
    public void sort() {
        new Thread(() -> {
            try {
                for (int i = 0; i < array.length - 1; i++) {
                    for (int j = 0; j < array.length - i - 1; j++) {
                        highlightedIndex = j;
                        if (array[j] > array[j + 1]) {
                            appendComment("Swapping elements: " + array[j] + " and " + array[j + 1] + "\n");
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                        }
                        panel.repaint();
                        Thread.sleep(delay);
                        waitIfPaused();
                    }
                }
                highlightedIndex = -1;
                panel.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
