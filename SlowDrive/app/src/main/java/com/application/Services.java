package com.application;

import android.app.Service;

import com.Database.DriverDB;
import com.Database.HistoryDB;
import com.Database.Interface.IDriverDB;
import com.Database.Interface.ILocationHistoryDB;


public class Services {

	private static ILocationHistoryDB history = null;
	private static IDriverDB driver = null;

	public static synchronized ILocationHistoryDB getHistoryDB() {
		if (history == null)
            history = new HistoryDB(Main.getDBPathName());

        return history;
	}

	public static synchronized IDriverDB getDriverDB() {
		if (driver == null)
			driver = new DriverDB(Main.getDBPathName());

		return driver;
	}

}
