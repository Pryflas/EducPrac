package org.example;

import javax.swing.plaf.PanelUI;
import java.util.Arrays;
// Создали класс шаблонный класс стека
public class Stack<T> {
    private T[] arr;
    int size;

    //Конструктор класса выделяем память под массив
    public Stack() {
        arr = (T[]) new Object[0]; // Выделяем память, теперь arr - хранит ссылку на массив объектов
        size = 0;
    }
    // Изменяем размер массива
    private void resize(int newSize) {
        arr = Arrays.copyOf(arr, newSize);
    }
    // Добавляем на вершину стека объект
    public void push(T item) {
        size += 1;
        resize(size);
        arr[size - 1] = item;
    }
    // Удаляем объект с вершины стека (освобождаем место изменением размера)
    public void pop() {
        if (size > 0) {
            arr[size - 1] = null;
            size -= 1;
            resize(size);
        }
    }
    // Возвращаем объект с вершины
    // null - если выход за размеры
    public T top() {
        if (size > 0) {
            return arr[size - 1];
        }
        return null;
    }
    // Возвращаем размер
    public int size() {
        return size;
    }
    // Ну пустой или не пустой
    public boolean isEmpty() {
        return size == 0;
    }
    // Глубокое копирование массива, в частности для объекта Square
    public Stack<T> deepCopy() {
        Stack<T> newStack = new Stack<>();
        for (T item : arr) {
            if (item instanceof Square) { // Проверяем, является ли объект классом Square
                newStack.push((T) ((Square) item).copy());
            } else {
                throw new UnsupportedOperationException("Unsupported type for deep copy");
            }
        }
        return newStack;
    }


}
