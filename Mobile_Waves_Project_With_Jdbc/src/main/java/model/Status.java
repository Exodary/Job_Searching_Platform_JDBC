package model;

public enum Status {
    APPROVED, NOT_APPROVED;

    public int getValue() {
        return ordinal() + 1;
    }
}
