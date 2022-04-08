package model.mock;

import model.User;

public class MockApplicants {
    public static final User[] MOCK_APPLICANTS;
    static {
        MOCK_APPLICANTS = new User[]{
                new User( "danielov@abv.bg", "Danielov","12345678I@"),
                new User("maria@abv.bg", "Maria","12345678I@"),
                new User("georgiev@abv.bg", "Goshkata","12345678I@")
        };
    }
}
