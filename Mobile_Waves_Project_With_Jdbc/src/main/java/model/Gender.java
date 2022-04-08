package model;

public enum Gender {
    MALE, FEMALE;

    public int getValue() {
        return ordinal() + 1;
    }

}
