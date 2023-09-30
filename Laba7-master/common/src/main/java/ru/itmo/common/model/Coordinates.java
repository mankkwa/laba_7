package ru.itmo.common.model;

public class Coordinates {
    private int x;
    private Float y; //Значение поля должно быть больше -188, Поле не может быть null

    public Coordinates(){}

    public Coordinates(int x, Float y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    // это некрасиво но пока пусть будет
    public int compareTo(Coordinates coord){
        if (this.getX() > coord.getX()) {
            return 1;
        } else if (this.getX() < coord.getX()) {
            return -1;
        } else {
            return this.getY().compareTo(coord.getY());
        }

    }

    @Override
    public String toString() {
        return "Coordinates (" +
                "x = " + x +
                ", y = " + y +
                ')';
    }
}