import javax.swing.*;
import java.util.Arrays;

public class MergeSortVisualizer extends VisualSorter {
    public MergeSortVisualizer(int[] array, JPanel panel, JTextArea commentsArea, int delay, JScrollPane commentsScrollPane, JTextField sortedArrayField) {
        super(array, panel, commentsArea, delay, commentsScrollPane, sortedArrayField);
    }

    @Override
    public void sort() {
        mergeSort(0, array.length - 1);
        setHighlightedIndex(-1);
        setComparedIndex(-1);
        addComment("Array is sorted: " + Arrays.toString(array));
        setSortedArray(array);
        updatePanel();
    }

    private void mergeSort(int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(left, middle);
            mergeSort(middle + 1, right);
            merge(left, middle, right);
        }
    }

    private void merge(int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;
        setLeftIndex(left);
        setRightIndex(right);
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        addComment("Merging sub-arrays: " + Arrays.toString(leftArray) + " and " + Arrays.toString(rightArray) + ":");
        while (i < n1 && j < n2) {
            setHighlightedIndex(k);
//            setComparedIndex(-1);
            if (leftArray[i] <= rightArray[j]) {
                addComment("Inserting " + leftArray[i] + " into position " + k);
                array[k] = leftArray[i];
                i++;
            } else {
                addComment("Inserting " + rightArray[j] + " into position " + k);
                array[k] = rightArray[j];
                j++;
            }
            k++;
            pauseIfNeeded();
            sleep();
            updatePanel();
        }

        while (i < n1) {
            setHighlightedIndex(k);

            addComment("Inserting " + leftArray[i] + " into position " + k);
            array[k] = leftArray[i];
            i++;
            k++;
            pauseIfNeeded();
            sleep();
            updatePanel();
        }

        while (j < n2) {
            setHighlightedIndex(k);

            addComment("Inserting " + rightArray[j] + " into position " + k);
            array[k] = rightArray[j];
            j++;
            k++;
            pauseIfNeeded();
            sleep();
            updatePanel();
        }
    }
}
