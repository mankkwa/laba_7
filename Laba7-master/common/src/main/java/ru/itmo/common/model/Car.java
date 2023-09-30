package ru.itmo.common.model;

//import com.fasterxml.jackson.annotation.JsonInclude;

public class Car implements Comparable {
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name; //Поле может быть null
    private boolean cool;
    private int result;

    public Car(){}

    public Car(String name, boolean cool){
        this.name = name;
        this.cool = cool;
    }

    public void setCarName(String name) {
        this.name = name;
    }

    public void setCarCool(boolean cool) {
        this.cool = cool;
    }

    public String getCarName() {
        return name;
    }

    public Boolean getCarCool(){
        return cool;
    }

    @Override
    public int compareTo(Object o) {
        Car car = (Car) o;
        if (name == null) {
            result = Boolean.compare(this.cool, car.cool);
        } else {
            result = this.name.compareTo(car.name);
            if (result == 0) {
                result = Boolean.compare(this.cool, car.cool);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String carInfo = "";
        if (getCarName() != null) {
            carInfo += getCarName() + ", ";
        }
        if (getCarCool()) {
            carInfo += "крутая тачка";
        } else {
            carInfo += "среднячок";
        }
        return carInfo;
    }
}

