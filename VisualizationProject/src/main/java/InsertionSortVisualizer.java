import javax.swing.*;
import java.util.Arrays;

public class InsertionSortVisualizer extends VisualSorter {
    public InsertionSortVisualizer(int[] array, JPanel panel, JTextArea commentsArea, int delay, JScrollPane commentsScrollPane, JTextField sortedArrayField) {
        super(array, panel, commentsArea, delay, commentsScrollPane, sortedArrayField);
    }

    @Override
    public void sort() {
        int n = array.length;
        for (int i = 1; i < n; ++i) {
            int key = array[i];

            int j = i - 1;

            addComment("Сравнение элементов: " + array[j] + " и " + key);
            setHighlightedIndex(i);
            setComparedIndex(j);
            while (j >= 0 && array[j] > key) {
                setComparedIndex(j);
                array[j + 1] = array[j];
                j = j - 1;
                addComment("Вставка элемента: " + key + " перед " + array[j+1]);
                pauseIfNeeded();
                sleep();
                updatePanel();
            }
            array[j + 1] = key;
            pauseIfNeeded();
            sleep();
            updatePanel();
        }
        setHighlightedIndex(-1); // Убираем подсветку после завершения сортировки
        setComparedIndex(-1);
        addComment("Отсортированный массив: " + Arrays.toString(array));
        setSortedArray(array);
        updatePanel();
    }
}
