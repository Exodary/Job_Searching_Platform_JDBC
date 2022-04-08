package model.mock;

import model.CV;
import model.Gender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MockCVs {
    public static final CV[] MOCK_CVS;
    static {
        MOCK_CVS = new CV[]{
                new CV( "Daniel", "Danielov","danielov@abv.bg", LocalDate.parse("1990-10-01"), Gender.MALE,
                        "0885801817", "Sofia", "Random Education", "Random Work Exp",
                        "https://image.shutterstock.com/image-photo/isolated-shot-young-handsome-male-260nw-762790210.jpg"),
                new CV("Maria", "Marieva","maria@abv.bg",LocalDate.parse("1990-10-01"), Gender.FEMALE,
                        "0883175478", "Burgas", "Random Education", "Random Work Exp",
                        "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZmVtYWxlJTIwbW9kZWx8ZW58MHx8MHx8&w=1000&q=80"),
                new CV("Gosho", "Georgiev", "georgiev@abv.bg", LocalDate.parse("1990-10-01"), Gender.MALE,
                        "0878987488", "Sofia", "Random Education", "Random Work Exp",
                        "https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
        };
    }
}
