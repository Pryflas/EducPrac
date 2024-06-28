package org.example;
import java.lang.Math;
import java.util.Arrays;

public class Solution {
    private int n; //Храним размер заданного поля
    private int[][] matrix; //Матрца, храним информацию о заполнении
    private Stack<Square> min_count; // Класс структуры дынных Стек

    public Solution(int val)
    {
        n = val;
        min_count = new Stack<>();
    }

    //Находим минимальный делитель
    public int divisor(int n){
        if (n % 2 == 0){
            return 2;
        }
        if (n % 3 == 0){
            return 3;
        }
        if (n % 5 == 0){
            return 5;
        }
        return 1;
    }

    //Заполнение каждой ячейки матрицы данными о квадрате в данной ячейке
    public void fill_square(Square area) {
        int x = area.x;
        int y = area.y;
        for (int i = 0; i < area.lenght; i++){
            for (int j = 0; j < area.lenght; j++){
                matrix[y+i][x+j] = area.lenght;
            }
        }
    }
    //Производим поиск свободного места для постановки квадрата
    public Square find_empty() {
        int size = matrix.length;
        int y = 0, x = 0;
        while (y < size && matrix[y][x] != 0){ // идем по длинам квадрата, как только нашли 0
            x += matrix[y][x];                 // или дошли до конца по y  заканчиваем
            if (x == size){
                y += 1;
                x = 0;
            }
        }
        if (y == size){ // Если дошли до конца по y => нулей не было найдено
            return null;
        }

        int r_y = (y != 0) ? 0 : -1; // Случай для корректировки координат в начале квадрата в (0,0) (x,0) (0,y)
        int r_x = (x != 0) ? 0 : -1;
        // Два цикла определяют максимальное расстояние по x/y для возможного квадрата
        for (int i = y; i < size; i++){
            if (matrix[i][x] == 0)
            {
                r_y += 1;
            }
            else{
                break;
            }
        }
        for (int i = x; i < size; i++){
            if (matrix[y][i] == 0)
            {
                r_x += 1;
            }
            else{
                break;
            }
        }
        Square temp = new Square(x, y, Math.min(r_x, r_y));
        return temp;
    }
    // Проверка на заполненность полотна
    public boolean isFull() {
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                if (matrix[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }


    public void solve(Stack<Square> squares){
        Square temp = find_empty();
        //Если не нашли места => выход
        if (temp == null){
            return;
        }
        //Если перешли за границу (найденная расстановка) => не имеет смысла дальше искать выходим
        if ((squares.size() + 1 > min_count.size()) && (min_count.size() != 0)){
            return;
        }
        Square max_len = new Square(temp.x, temp.y, temp.lenght);
        // Пытаемя расставить квадрт любой длины от макс до 1
        for (int i = max_len.lenght; i > 0; i--)
        {
            max_len.lenght = i;
            fill_square(max_len);
            squares.push(max_len); //Добавили расстановку в стек
            //как только нашли расстановку, то возможно добавляем ее как лучшую
            if (isFull() && ((min_count.isEmpty()) || (min_count.size() > squares.size()))){
                min_count = squares.deepCopy();
            }
            else{
                //ну или ищем дальше
                solve(squares);
            }
            //Очищаем полотно постепенно от маленьких квадратиков
            for (int j = 0; j < max_len.lenght; j++){
                for (int k = 0; k < max_len.lenght; k++){
                    matrix[max_len.y + j][max_len.x + k] = 0;
                }
            }
            //ну и убераем расстановку из временного хранилища(стека)
            squares.pop();
        }
        return;
    }

    public void start_execute() {
        int div = divisor(n);
        Stack<Square> squares = new Stack<>();
        if (div != 1)
        {
            matrix = new int[div][div];
            solve(squares);
        }
        else
        { //Если простой число можем поставить сразу 3 квадрата
            matrix = new int[n][n];
            int half = n - n/2;
            fill_square(new Square(0, 0, half));
            fill_square(new Square(0, half, half-1));
            fill_square(new Square(half, 0, half-1));
            solve(squares);
            min_count.push(new Square(0, 0, half));
            min_count.push(new Square(0, half, half-1));
            min_count.push(new Square(half, 0, half-1));
        }
        System.out.println(min_count.size());
        int scale = (div != 1) ? (int) n/div : 1;
        while (!min_count.isEmpty()){
            System.out.println((min_count.top().x * scale + 1) + " " +
                    (min_count.top().y * scale + 1) + " " +
                    min_count.top().lenght * scale);
            min_count.pop();
        }
    }
}