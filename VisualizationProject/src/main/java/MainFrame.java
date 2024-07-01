import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
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
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        arrayInput = new JTextField();
        delayInput = new JTextField("100");
        bubbleSortButton = new JButton("Bubble Sort");
        mergeSortButton = new JButton("Merge Sort");
        insertionSortButton = new JButton("Insertion Sort");
        pauseButton = new JButton("Pause");

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(8,2));
        controlPanel.add(new JLabel("Array (comma-separated or file path):"));
        controlPanel.add(arrayInput);
        controlPanel.add(new JLabel("Delay (ms):"));
        controlPanel.add(delayInput);
        controlPanel.add(bubbleSortButton);
        controlPanel.add(mergeSortButton);
        controlPanel.add(insertionSortButton);
        controlPanel.add(pauseButton);

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
                        } else if (i >= currentSorter.getLeftIndex() && i <= currentSorter.getRightIndex()) {
                            g.setColor(Color.BLUE);
                        } else {
                            g.setColor(Color.GRAY);
                        }
                        g.fillRect(i * width, getHeight() - height, width, height);
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(800, 400));

        commentsArea = new JTextArea();
        commentsArea.setEditable(false);
        commentsScrollPane = new JScrollPane(commentsArea);
        commentsScrollPane.setPreferredSize(new Dimension(800, 150));

        sortedArrayField = new JTextField();
        sortedArrayField.setEditable(false);
        sortedArrayField.setPreferredSize(new Dimension(800, 50));

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
            JOptionPane.showMessageDialog(this, "Please enter an array or file path.");
            return;
        }

        try {
            currentArray = parseArrayInput(input);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid array input.");
            return;
        }

        int delay;
        try {
            delay = Integer.parseInt(delayInput.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid delay input.");
            return;
        }

        commentsArea.setText("");
        sortedArrayField.setText(""); // Очистить поле отсортированного массива

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
        pauseButton.setText(paused ? "Resume" : "Pause");
    }

    private int[] parseArrayInput(String input) throws IOException {
        if (input.contains(",")) {
            String[] parts = input.split(",");
            int[] array = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                array[i] = Integer.parseInt(parts[i].trim());
            }
            return array;
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(input));
            String line = reader.readLine();
            reader.close();
            String[] parts = line.split(",");
            int[] array = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                array[i] = Integer.parseInt(parts[i].trim());
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
