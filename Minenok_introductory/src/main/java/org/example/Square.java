package org.example;
//Класс представляющий один поставленный квадратик
public class Square {
    public int x;
    public int y;
    public int lenght;
    //Конструктор
    public Square(int xv, int yv, int lenghtv){
        x = xv;
        y = yv;
        lenght = lenghtv;
    }

    public Square copy(){
        return new Square(x,y,lenght);
    }

}
