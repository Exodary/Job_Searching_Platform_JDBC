package Regex;

public class Regexes {
    public static final String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,15}$";
    public static final String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String UrlRegex = "((http|https)://)(www.)?"
            + "[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]"
            + "{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)";
    public static final String phoneRegex = "^[0-9]+$";
}
