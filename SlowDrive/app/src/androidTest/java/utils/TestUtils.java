package utils;

import utils.DriverInfo.FakeDriverInfo;
import com.Database.Interface.IDriverDB;
import com.Logic.Object.Driver;
import com.application.Services;
public class TestUtils {
    public static void setUp(){
        Driver d= new Driver(FakeDriverInfo.firstName,FakeDriverInfo.lastName,FakeDriverInfo.userName,FakeDriverInfo.password,FakeDriverInfo.email);
        IDriverDB db=  Services.getDriverDB();
        db.insert(d);
    }
    public static void clean(){
        Driver d= new Driver(FakeDriverInfo.firstName,FakeDriverInfo.lastName,FakeDriverInfo.userName,FakeDriverInfo.password,FakeDriverInfo.email);
        IDriverDB db= Services.getDriverDB();
        db.delete(d);
    }
}
