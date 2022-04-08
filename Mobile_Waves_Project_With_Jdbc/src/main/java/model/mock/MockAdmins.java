package model.mock;

import model.User;

public class MockAdmins {
    public static final User[] MOCK_ADMINS;
    static {
        MOCK_ADMINS = new User[]{
                new User("ivanov@abv.bg","ivaka", "12345678I@"),
                new User("krasimir@abv.bg", "krasi","12345678I@")
        };
    }
}
