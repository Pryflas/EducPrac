import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainFrame extends JFrame {
    private JTextField arrayInput;
    private JTextField delayInput;
    private JButton bubbleSortButton;
    private JButton mergeSortButton;
    private JButton insertionSortButton;
    private JButton pauseButton;
    private JPanel panel;
    private JTextArea commentsArea;
    private JScrollPane commentsScrollPane;
    private int[] currentArray;
    private VisualSorter currentSorter = null; // Текущий визуализатор сортировки
    private volatile boolean paused = false;
    private Thread currentSortingThread = null; // Ссылка на текущий поток сортировки

    public MainFrame() {
        setTitle("Sorting Visualizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        arrayInput = new JTextField();
        delayInput = new JTextField("100");
        bubbleSortButton = new JButton("Пузырьком");
        mergeSortButton = new JButton("Слиянием");
        insertionSortButton = new JButton("Вставками");
        pauseButton = new JButton("Пауза");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Введите массив натуральных чисел через запятую или абсолютный путь к файлу:"));
        inputPanel.add(arrayInput);
        inputPanel.add(new JLabel("Введите задержку(мс):"));
        inputPanel.add(delayInput);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bubbleSortButton.setPreferredSize(new Dimension(150, 40));
        mergeSortButton.setPreferredSize(new Dimension(150, 40));
        insertionSortButton.setPreferredSize(new Dimension(150, 40));
        pauseButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(bubbleSortButton);
        buttonPanel.add(mergeSortButton);
        buttonPanel.add(insertionSortButton);
        buttonPanel.add(pauseButton);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(inputPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

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
                        if (currentSorter != null && i == currentSorter.getHighlightedIndex()) {
                            g.setColor(Color.GREEN);
                        } else {
                            g.setColor(Color.BLUE);
                        }
                        int barHeight = (int) (((double) currentArray[i] / maxValue) * height);
                        g.fillRect(i * barWidth, height - barHeight, barWidth, barHeight);
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(800, 400));

        commentsArea = new JTextArea();
        commentsArea.setEditable(false);

        commentsScrollPane = new JScrollPane(commentsArea);
        commentsScrollPane.setPreferredSize(new Dimension(800, 100));

        add(controlPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(commentsScrollPane, BorderLayout.SOUTH);

        bubbleSortButton.addActionListener(new SortButtonListener());
        mergeSortButton.addActionListener(new SortButtonListener());
        insertionSortButton.addActionListener(new SortButtonListener());
        pauseButton.addActionListener(e -> {
            paused = !paused;
            if (currentSorter != null) {
                currentSorter.setPaused(paused);
            }
        });
    }

    private class SortButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Прерываем текущую сортировку, если она выполняется
            if (currentSortingThread != null && currentSortingThread.isAlive()) {
                currentSortingThread.interrupt();
            }

            String inputText = arrayInput.getText().trim();

            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Введите массив или путь к файлу.");
                return;
            }

            if (inputText.contains(",")) {
                String[] input = inputText.split(",");
                currentArray = new int[input.length];
                try {
                    for (int i = 0; i < input.length; i++) {
                        currentArray[i] = Integer.parseInt(input[i].trim());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Неверный формат массива. Введите натуральные числа разделенные запятой.");
                    return;
                }
            } else {
                try (BufferedReader br = new BufferedReader(new FileReader(inputText))) {
                    String line = br.readLine();
                    if (line != null) {
                        String[] input = line.split(",");
                        currentArray = new int[input.length];
                        for (int i = 0; i < input.length; i++) {
                            currentArray[i] = Integer.parseInt(input[i].trim());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Файл пустой.");
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка чтения файла. Проверьте путь к файлу.");
                    return;
                }
            }

            int delay;
            try {
                delay = Integer.parseInt(delayInput.getText().trim());
                if (delay < 1) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Неверный формат задержки. Введите натуральное число.");
                return;
            }

            if (e.getSource() == bubbleSortButton) {
                currentSortingThread = new Thread(() -> {
                    currentSorter = new BubbleSortVisualizer(currentArray, panel, commentsArea, delay, commentsScrollPane);
                    currentSorter.sort();
                });
                currentSortingThread.start();
            } else if (e.getSource() == mergeSortButton) {
                currentSortingThread = new Thread(() -> {
                    currentSorter = new MergeSortVisualizer(currentArray, panel, commentsArea, delay, commentsScrollPane);
                    currentSorter.sort();
                });
                currentSortingThread.start();
            } else if (e.getSource() == insertionSortButton) {
                currentSortingThread = new Thread(() -> {
                    currentSorter = new InsertionSortVisualizer(currentArray, panel, commentsArea, delay, commentsScrollPane);
                    currentSorter.sort();
                });
                currentSortingThread.start();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
