package model.mock;

import model.User;

public class MockEmployers {
    public static final User[] MOCK_EMPLOYERS;
    static {
        MOCK_EMPLOYERS = new User[]{
                new User( "deyan@abv.bg","Deyan Prodanov","12345678I@"),
                new User("aseno@abv.bg", "Ivan Asenov","12345678I@"),
                new User("boyanov@abv.bg", "Boyan Boyanov", "12345678I@")
        };
    }
}
