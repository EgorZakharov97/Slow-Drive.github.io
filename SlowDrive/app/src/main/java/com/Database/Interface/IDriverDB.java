package com.Database.Interface;

import com.Logic.Object.Driver;

public interface IDriverDB {
    void insert(Driver d);
    void delete(Driver d);
    Driver getDriver(String username);
    void changePassword (String pass, Driver d);
    String getDriverSalt(String username);
}
