package ru.itmo.common.model;


import java.time.LocalDate;
import java.util.Objects;

public class HumanBeing implements Comparable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным,
    // Значение этого поля должно генерироваться автоматически
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно
    // генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String soundtrackName; //Поле не может быть null
    private Long minutesOfWaiting; //Поле не может быть null
    private int impactSpeed;
    private boolean realHero;
    private Boolean hasToothpick; //Поле может быть null
    private Coordinates coordinates; //Поле не может быть null
    private Mood mood; //Поле может быть null
    private Car car; //Поле не может быть null
    private String userLogin;

    public HumanBeing(){}

    public HumanBeing(String name, String soundtrackName, Long minutesOfWaiting, int impactSpeed,
                      boolean realHero, Boolean hasToothpick, Coordinates coordinates, Mood mood, Car car) {
        this.name = name;
        this.soundtrackName = soundtrackName;
        this.minutesOfWaiting = minutesOfWaiting;
        this.impactSpeed = impactSpeed;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.coordinates = coordinates;
        this.mood = mood;
        this.car = car;
    }

    // геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setRealHero(boolean realHero) {
        this.realHero = realHero;
    }

    public boolean isRealHero() {
        return realHero;
    }

    public void setHasToothpick(Boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public Boolean isHasToothpick() {
        return hasToothpick;
    }

    public void setImpactSpeed(int impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    public int getImpactSpeed() {
        return impactSpeed;
    }

    public void setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public void setMinutesOfWaiting(Long minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }

    public Long getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public Mood getMood() {
        return mood;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * переобределение метода compareTo для сортировки
     * первое поле, по которому происходит сортировка - coordinates
     */
    @Override
    public int compareTo(Object o) {
        if (o == null){
            return -1;
        }
        if (!(o instanceof HumanBeing)){
            throw new ClassCastException("Несравнимые типы.");
        }
        HumanBeing humanBeing = (HumanBeing) o;
        int result = this.name.compareTo(humanBeing.name);
        if (result == 0) {
            result = this.getCoordinates().compareTo(humanBeing.getCoordinates());
            if ((hasToothpick == null) || (humanBeing.hasToothpick == null)) {
                result = 0;
            } else {
                result = Boolean.compare(this.hasToothpick, humanBeing.hasToothpick);
            }
            if (result == 0) {
                result = Integer.compare(this.impactSpeed, humanBeing.impactSpeed);
            }
            if (result == 0) {
                result = this.soundtrackName.compareTo(humanBeing.soundtrackName);
            }
            if (result == 0) {
                result = this.minutesOfWaiting.compareTo(humanBeing.minutesOfWaiting);
            }
            if (result == 0) {
                if (mood == null || humanBeing.mood == null) {
                    result=0;
                } else {
                    int ordinalThis = Mood.valueOf(this.mood.toString()).ordinal();
                    int ordinalHumanBeing = Mood.valueOf(humanBeing.mood.toString()).ordinal();
                    result = Integer.compare(ordinalThis, ordinalHumanBeing);
                }
            }
            if (result == 0) {
                result = this.car.compareTo(humanBeing.car);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "id = " + id +
                ",\ncreationDate = " + creationDate +
                ",\nuser = " + userLogin +
                ",\nname = " + name +
                ",\nsoundtrackName = " + soundtrackName +
                ",\nminutesOfWaiting = " + minutesOfWaiting +
                ",\nimpactSpeed = " + impactSpeed +
                ",\nrealHero = " + realHero +
                ",\nhasToothpick = " + hasToothpick +
                ",\ncoordinates = " + coordinates +
                ",\nmood = " + mood +
                ",\ncar = " + car +
                '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumanBeing that = (HumanBeing) o;
        return impactSpeed == that.impactSpeed && realHero == that.realHero && Objects.equals(name, that.name) && Objects.equals(soundtrackName, that.soundtrackName) && Objects.equals(minutesOfWaiting, that.minutesOfWaiting) && Objects.equals(hasToothpick, that.hasToothpick) && Objects.equals(coordinates, that.coordinates) && mood == that.mood && Objects.equals(car, that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, soundtrackName, minutesOfWaiting, impactSpeed, realHero, hasToothpick, coordinates, mood, car);
    }
}
