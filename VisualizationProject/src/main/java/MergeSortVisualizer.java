import javax.swing.*;

public class MergeSortVisualizer {
    private int[] array;
    private JPanel panel;

    public MergeSortVisualizer(int[] array, JPanel panel) {
        this.array = array;
        this.panel = panel;
    }

    public void sort() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mergeSort(array, 0, array.length - 1);
            }
        }).start();
    }

    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

    private void merge(int[] array, int left, int middle, int right) {
        int[] leftArray = new int[middle - left + 1];
        int[] rightArray = new int[right - middle];

        System.arraycopy(array, left, leftArray, 0, leftArray.length);
        System.arraycopy(array, middle + 1, rightArray, 0, rightArray.length);

        int i = 0, j = 0, k = left;

        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
            try {
                Thread.sleep(50); // пауза для визуализации
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            panel.repaint(); // перерисовка панели
        }

        while (i < leftArray.length) {
            array[k] = leftArray[i];
            i++;
            k++;
            panel.repaint(); // перерисовка панели
        }

        while (j < rightArray.length) {
            array[k] = rightArray[j];
            j++;
            k++;
            panel.repaint(); // перерисовка панели
        }
    }
}