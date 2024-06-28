import javax.swing.*;

public class MergeSortVisualizer extends VisualSorter {

    public MergeSortVisualizer(int[] array, JPanel panel, JTextArea commentsArea, int delay, JScrollPane commentsScrollPane) {
        super(array, panel, commentsArea, delay, commentsScrollPane);
    }

    @Override
    public void sort() {
        new Thread(() -> {
            try {
                mergeSort(array, 0, array.length - 1);
                highlightedIndex = -1;
                panel.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void mergeSort(int[] array, int left, int right) throws InterruptedException {
        if (left < right) {
            int middle = (left + right) / 2;

            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);

            merge(array, left, middle, right);
        }
    }

    private void merge(int[] array, int left, int middle, int right) throws InterruptedException {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i) {
            L[i] = array[left + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = array[middle + 1 + j];
        }

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            highlightedIndex = k;
            if (L[i] <= R[j]) {
                appendComment("Inserting " + L[i] + " into position " + k + "\n");
                array[k] = L[i];
                i++;
            } else {
                appendComment("Inserting " + R[j] + " into position " + k + "\n");
                array[k] = R[j];
                j++;
            }
            panel.repaint();
            Thread.sleep(delay);
            waitIfPaused();
            k++;
        }

        while (i < n1) {
            highlightedIndex = k;
            appendComment("Inserting " + L[i] + " into position " + k + "\n");
            array[k] = L[i];
            i++;
            k++;
            panel.repaint();
            Thread.sleep(delay);
            waitIfPaused();
        }

        while (j < n2) {
            highlightedIndex = k;
            appendComment("Inserting " + R[j] + " into position " + k + "\n");
            array[k] = R[j];
            j++;
            k++;
            panel.repaint();
            Thread.sleep(delay);
            waitIfPaused();
        }
    }
}
