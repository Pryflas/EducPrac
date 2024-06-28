import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class MainFrame extends JFrame {
    private JTextField arrayInput; // Поле ввода для массива
    private JButton bubbleSortButton; // Кнопка для запуска сортировки пузырьком
    private JButton mergeSortButton; // Кнопка для запуска сортировки слиянием
    private JButton insertionSortButton; // Кнопка для запуска сортировки вставками
    private JPanel panel; // Панель для визуализации сортировки
    private int[] currentArray; // Текущий массив для сортировки

    public MainFrame() {
        setTitle("Sorting Visualizer"); // Устанавливаем заголовок окна
        setSize(1500, 600); // Устанавливаем размер окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Устанавливаем действие при закрытии окна
        setLayout(new BorderLayout()); // Устанавливаем компоновку для окна

        arrayInput = new JTextField(); // Инициализируем поле ввода массива
        bubbleSortButton = new JButton("Пузырьком"); // Инициализируем кнопку для сортировки пузырьком
        mergeSortButton = new JButton("Слиянием"); // Инициализируем кнопку для сортировки слиянием
        insertionSortButton = new JButton("Вставками"); // Инициализируем кнопку для сортировки вставками

        JPanel inputPanel = new JPanel(); // Панель для ввода массива
        inputPanel.setLayout(new BorderLayout()); // Устанавливаем компоновку для панели ввода
        inputPanel.add(new JLabel("Введите массив через запятую или абсолютный путь к файлу"), BorderLayout.NORTH); // Добавляем метку
        inputPanel.add(arrayInput, BorderLayout.CENTER); // Добавляем поле ввода

        JPanel buttonPanel = new JPanel(); // Панель для кнопок сортировки
        bubbleSortButton.setPreferredSize(new Dimension(150, 40)); // Устанавливаем размер кнопки сортировки пузырьком
        mergeSortButton.setPreferredSize(new Dimension(150, 40)); // Устанавливаем размер кнопки сортировки слиянием
        insertionSortButton.setPreferredSize(new Dimension(150, 40)); // Устанавливаем размер кнопки сортировки вставками
        buttonPanel.add(bubbleSortButton); // Добавляем кнопку сортировки пузырьком на панель
        buttonPanel.add(mergeSortButton); // Добавляем кнопку сортировки слиянием на панель
        buttonPanel.add(insertionSortButton); // Добавляем кнопку сортировки вставками на панель

        JPanel controlPanel = new JPanel(); // Панель для управления (ввод массива и кнопки)
        controlPanel.setLayout(new BorderLayout()); // Устанавливаем компоновку для панели управления
        controlPanel.add(inputPanel, BorderLayout.SOUTH); // Добавляем панель ввода на панель управления
        controlPanel.add(buttonPanel, BorderLayout.CENTER); // Добавляем панель кнопок на панель управления

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentArray != null) {
                    int width = getWidth();
                    int height = getHeight();
                    int barWidth = width / currentArray.length;
                    int maxValue = Integer.MIN_VALUE;
                    for (int value : currentArray) {
                        if (value > maxValue) {
                            maxValue = value;
                        }
                    }
                    for (int i = 0; i < currentArray.length; i++) {
                        int barHeight = (int) (((double) currentArray[i] / maxValue) * height);
                        g.fillRect(i * barWidth, height - barHeight, barWidth, barHeight);
                    }
                }
            }
        };


        add(controlPanel, BorderLayout.NORTH); // Добавляем панель управления на главное окно
        add(panel, BorderLayout.CENTER); // Добавляем панель визуализации на главное окно

        bubbleSortButton.addActionListener(new SortButtonListener()); // Добавляем слушатель к кнопке сортировки пузырьком
        mergeSortButton.addActionListener(new SortButtonListener()); // Добавляем слушатель к кнопке сортировки слиянием
        insertionSortButton.addActionListener(new SortButtonListener()); // Добавляем слушатель к кнопке сортировки вставками
    }

    private class SortButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inputText = arrayInput.getText().trim();

            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Введите массив или абсолютный путь к файлу.");
                return;
            }

            if (inputText.contains(",")) {
                // Обработка ввода массива через запятую
                String[] input = inputText.split(",");
                currentArray = new int[input.length];
                try {
                    for (int i = 0; i < input.length; i++) {
                        currentArray[i] = Integer.parseInt(input[i].trim());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Неверный формат массив. Введите массив через запятую.");
                    return;
                }
            } else {
                // Обработка ввода пути к файлу
                try (BufferedReader br = new BufferedReader(new FileReader(inputText))) {
                    String line = br.readLine();
                    if (line != null) {
                        String[] input = line.split(",");
                        currentArray = new int[input.length];
                        for (int i = 0; i < input.length; i++) {
                            currentArray[i] = Integer.parseInt(input[i].trim());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Файл пустой");
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка чтения файла. Проверьте путь к файлу.");
                    return;
                }
            }

            if (e.getSource() == bubbleSortButton) {
                BubbleSortVisualizer bubbleSortVisualizer = new BubbleSortVisualizer(currentArray, panel);
                bubbleSortVisualizer.sort();
            } else if (e.getSource() == mergeSortButton) {
                MergeSortVisualizer mergeSortVisualizer = new MergeSortVisualizer(currentArray, panel);
                mergeSortVisualizer.sort();
            } else if (e.getSource() == insertionSortButton) {
                InsertionSortVisualizer insertionSortVisualizer = new InsertionSortVisualizer(currentArray, panel);
                insertionSortVisualizer.sort();
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
