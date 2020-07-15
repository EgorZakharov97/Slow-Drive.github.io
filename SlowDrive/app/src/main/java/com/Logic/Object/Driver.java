package com.Logic.Object;

public class Driver {

    protected String firstName = "Guest";
    protected String lastName = "";
    protected String userName = "Guest";
    protected String email = "NoOne@gmail.com";
    protected String password = "Passwords do not exist on Guest Accounts";

    public Driver (){

    }

    public Driver(String firstName, String lastName, String userName, String password, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public boolean match(String password){
        if (this.password.equals(password)){
            System.out.println("Password match" + " dp: " + this.getPassword() + "p: " +  password);
            return true;
        } else {
            System.out.println("Password DOESNOT match" + " dp: " + getPassword() + "p: " +  password);
            return false;
        } // && accessDriver.getDriver(userName).getPassword().equals(password); // sorry this line looks gross, ill refactor later
    }

    public String toString(){
        return "Driver's Account: " + userName +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName + "\n";
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getUserName(){
        return userName;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String newPassword (String newPass) { this.password = password; return this.password; }
}
