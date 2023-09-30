package ru.itmo.common.exceptions;

public class WrongArgumentException extends Exception{
    private final TypeOfError type;

    public WrongArgumentException(TypeOfError type) {
        this.type = type;
    }

    public TypeOfError getType() {
        return type;
    }
}
