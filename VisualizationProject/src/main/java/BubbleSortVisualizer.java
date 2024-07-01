import javax.swing.*;
import java.util.Arrays;

public class BubbleSortVisualizer extends VisualSorter {
    public BubbleSortVisualizer(int[] array, JPanel panel, JTextArea commentsArea, int delay, JScrollPane commentsScrollPane, JTextField sortedArrayField) {
        super(array, panel, commentsArea, delay, commentsScrollPane, sortedArrayField);
    }

    @Override
    public void sort() {
        int n = array.length;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                setHighlightedIndex(i);
                setComparedIndex(i + 1);
                addComment("Comparing elements: " + array[i] + " and " + array[i + 1]);
                if (array[i] > array[i + 1]) {
                    addComment("Swapping elements: " + array[i] + " and " + array[i + 1]);
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    swapped = true;
                    updatePanel();
                }
                pauseIfNeeded();
                sleep();
            }
            n--;
        } while (swapped);
        setHighlightedIndex(-1); // Убираем подсветку после завершения сортировки
        setComparedIndex(-1);
        addComment("Array is sorted: " + Arrays.toString(array));
        setSortedArray(array);
        updatePanel();
    }
}
