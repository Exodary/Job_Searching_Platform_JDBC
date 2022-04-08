package model;

public enum Role {
    ADMIN, EMPLOYER, APPLICANT;

    public int getValue() {
        return ordinal() + 1;
    }
}
