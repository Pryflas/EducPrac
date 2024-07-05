import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.CharConversionException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MainFrame extends JFrame {
    private final JTextField arrayInput;
    private final JTextField delayInput;
    private final JButton bubbleSortButton;
    private final JButton mergeSortButton;
    private final JButton insertionSortButton;
    private final JButton pauseButton;
    private final JPanel panel;
    private final JTextArea commentsArea;
    private final JScrollPane commentsScrollPane;
    private final JTextField sortedArrayField;
    private int[] currentArray;
    private VisualSorter currentSorter = null; // Текущий визуализатор сортировки
    private  boolean paused = false;
    private Thread currentSortingThread = null; // Ссылка на текущий поток сортировки

    public MainFrame() {
        setTitle("Sorting Visualizer");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        arrayInput = new JTextField();
        delayInput = new JTextField("100");

        bubbleSortButton = new JButton("Пузырьком");
        mergeSortButton = new JButton("Слиянием");
        insertionSortButton = new JButton("Вставками");
        pauseButton = new JButton("Пауза");


        bubbleSortButton.setPreferredSize(new Dimension(450, 30));
        mergeSortButton.setPreferredSize(new Dimension(450, 30));
        insertionSortButton.setPreferredSize(new Dimension(450, 30));
        pauseButton.setPreferredSize(new Dimension(100, 20));


        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        controlPanel.add(new JLabel("Введите массив натуральных чисел через запятую или абсолютный путь к файлу:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        controlPanel.add(arrayInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        controlPanel.add(new JLabel("Введите задержку(мс):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        controlPanel.add(delayInput, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        controlPanel.add(pauseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        controlPanel.add(bubbleSortButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        controlPanel.add(mergeSortButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        controlPanel.add(insertionSortButton, gbc);



        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentArray != null) {
                    int width = getWidth() / currentArray.length;
                    int maxHeight = getHeight();
                    int maxArrayValue = Arrays.stream(currentArray).max().orElse(1);

                    for (int i = 0; i < currentArray.length; i++) {
                        int height = (int) (((double) currentArray[i] / maxArrayValue) * maxHeight);

                        if (i == currentSorter.getHighlightedIndex()) {
                            g.setColor(Color.GREEN);
                        } else if (i == currentSorter.getComparedIndex()) {
                            g.setColor(Color.RED);
                        } else if ((i >= currentSorter.getLeftIndex() && i <= currentSorter.getMidIndex()) || i == currentSorter.getBubbleIndex() || i == currentSorter.getBubbleIndex2()) {
                            g.setColor(Color.BLUE);
                        } else if (i <= currentSorter.getRightIndex() && i >= currentSorter.getMidIndex()) {
                          g.setColor(Color.CYAN);
                        } else {
                            g.setColor(Color.GRAY);
                        }
                        g.fillRect(i * width, getHeight() - height, width, height);
                    }
                }
            }
        };

        commentsArea = new JTextArea();
        commentsArea.setEditable(false);
        commentsScrollPane = new JScrollPane(commentsArea);
        commentsScrollPane.setPreferredSize(new Dimension(1500, 150));

        sortedArrayField = new JTextField();
        sortedArrayField.setEditable(false);
        sortedArrayField.setPreferredSize(new Dimension(1500, 50));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(commentsScrollPane, BorderLayout.CENTER);
        bottomPanel.add(sortedArrayField, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        bubbleSortButton.addActionListener(e -> startSorting("Bubble"));
        mergeSortButton.addActionListener(e -> startSorting("Merge"));
        insertionSortButton.addActionListener(e -> startSorting("Insertion"));
        pauseButton.addActionListener(e -> togglePause());
    }

    private void startSorting(String algorithm) {
        if (currentSortingThread != null && currentSortingThread.isAlive()) {
            currentSorter.setPaused(true); // Остановить текущую сортировку
            currentSortingThread.interrupt(); // Прервать текущий поток
        }

        String input = arrayInput.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите массив или путь к файлу.");
            return;
        }

        try {
            currentArray = parseArrayInput(input);
        } catch (CharConversionException e) {
            JOptionPane.showMessageDialog(this, "Файл пустой.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка чтения файла: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Неверный формат массива. Введите натуральные числа через запятую.");
            return;
        }

        int delay;
        try {
            delay = Integer.parseInt(delayInput.getText().trim());
            if (delay < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Неверный формат задержки. Ведите натуральное число");
            return;
        }

        commentsArea.setText("");
        sortedArrayField.setText(""); // Очистить поле отсортированного массива
        paused = false; // Сбросить паузу
        pauseButton.setText("Пауза"); // Установить текст кнопки на "Pause"

        switch (algorithm) {
            case "Bubble":
                currentSorter = new BubbleSortVisualizer(currentArray, panel, commentsArea, delay, commentsScrollPane, sortedArrayField);
                break;
            case "Merge":
                currentSorter = new MergeSortVisualizer(currentArray, panel, commentsArea, delay, commentsScrollPane, sortedArrayField);
                break;
            case "Insertion":
                currentSorter = new InsertionSortVisualizer(currentArray, panel, commentsArea, delay, commentsScrollPane, sortedArrayField);
                break;
        }

        currentSortingThread = new Thread(() -> currentSorter.sort());
        currentSortingThread.start();
    }

    private void togglePause() {
        if (currentSorter == null) {
            return;
        }

        paused = !paused;
        currentSorter.setPaused(paused);
        pauseButton.setText(paused ? "Возобновить" : "Пауза");

        if (!paused) {
            // Обновить задержку при возобновлении
            int delay;
            try {
                delay = Integer.parseInt(delayInput.getText().trim());
                if (delay < 1) {
                    throw new NumberFormatException();
                }
                currentSorter.setDelay(delay);
            } catch (NumberFormatException e) {
                paused = true;
                currentSorter.setPaused(paused);
                pauseButton.setText(paused ? "Возобновить" : "Пауза");

                JOptionPane.showMessageDialog(this, "Неверный формат задержки. Ведите натуральное число");

            }
        }

    }

    private int[] parseArrayInput(String input) throws IOException {
        if (input.contains(",")) {
            String[] parts = input.split(",");
            int[] array = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                array[i] = Integer.parseInt(parts[i].trim());
                if (array[i] < 0){
                    throw new NumberFormatException();
                }
            }
            return array;
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(input));
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                throw new IOException("Файл пустой.");
            }
            reader.close();
            String[] parts = line.split(",");
            int[] array = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                array[i] = Integer.parseInt(parts[i].trim());
                if (array[i] < 0){
                    throw new NumberFormatException();
                }
            }
            return array;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
