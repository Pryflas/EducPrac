import javax.swing.*;

public class BubbleSortVisualizer {
    private int[] array;
    private JPanel panel;

    public BubbleSortVisualizer(int[] array, JPanel panel) {
        this.array = array;
        this.panel = panel;
    }

    public void sort() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean swapped = true;
                for (int i = 0; i < array.length - 1 && swapped; i++) {
                    swapped = false;
                    for (int j = 0; j < array.length - 1 - i; j++) {
                        if (array[j] > array[j + 1]) {
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                            swapped = true;
                        }
                        try {
                            Thread.sleep(50); // пауза для визуализации
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        panel.repaint(); // перерисовка панели
                    }
                }
            }
        }).start();
    }
}
