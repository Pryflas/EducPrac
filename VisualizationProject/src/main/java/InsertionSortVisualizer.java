import javax.swing.*;

public class InsertionSortVisualizer {
    private int[] array;
    private JPanel panel;

    public InsertionSortVisualizer(int[] array, JPanel panel) {
        this.array = array;
        this.panel = panel;
    }

    public void sort() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < array.length; i++) {
                    int key = array[i];
                    int j = i - 1;
                    while (j >= 0 && array[j] > key) {
                        array[j + 1] = array[j];
                        j--;
                        try {
                            Thread.sleep(50); // пауза для визуализации
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        panel.repaint(); // перерисовка панели
                    }
                    array[j + 1] = key;
                    panel.repaint(); // перерисовка панели
                }
            }
        }).start();
    }
}