package prog5121poe;

public class Login {
    private String storedUsername;
    private String storedPassword;
    private String storedFirstName;
    private String storedLastName;
    private String storedCellPhoneNumber;
    private boolean isRegistered = false;

    public boolean checkUserName(String username) {
        if (username == null) return false;
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return hasUppercase && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) return false;
        return cellPhoneNumber.startsWith("+") && cellPhoneNumber.length() <= 12;
    }

    public String registerUser(String username, String password, String firstName, String lastName, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least 8 characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        }
        
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedFirstName = firstName;
        this.storedLastName = lastName;
        this.storedCellPhoneNumber = cellPhoneNumber;
        this.isRegistered = true;
        
        return "The user has been successfully registered.";
    }

    public boolean loginUser(String username, String password) {
        if (!isRegistered) return false;
        return username != null && password != null && username.equals(storedUsername) && password.equals(storedPassword);
    }

    // Overloaded returnLoginStatus methods to handle any arguments passed from Main or tests
    public String returnLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            return "Welcome " + storedFirstName + " " + storedLastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again";
        }
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + storedFirstName + " " + storedLastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again";
        }
    }

    public String returnLoginStatus(String username, String password, boolean loginStatus) {
        return returnLoginStatus(loginStatus);
    }
}