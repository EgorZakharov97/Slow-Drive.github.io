package com.Database;

import com.CustomExceptions.PersistenceException;
import com.Database.Interface.ILocationHistoryDB;
import com.Logic.Object.LocationData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryDB implements ILocationHistoryDB {
    private final String dbPath;
    private List<LocationData> history;
    public HistoryDB(final String dbPath) {
        this.dbPath = dbPath;
        history= new ArrayList<>();
        load();
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    private LocationData fromResultSet(final ResultSet rs) throws SQLException {
        final String streetName = rs.getString("STREETNAME");
        final Double longitude = rs.getDouble("LONGITUDE");
        final Double latitude = rs.getDouble("LATITUDE");
        final int speedlimit = rs.getInt("speedLimit");
        return new LocationData(longitude, latitude, speedlimit, streetName);
    }

    @Override
    public boolean addLocation(LocationData location) {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("INSERT INTO HISTORY VALUES(?, ?, ?, ?, ?)");
            st.setString(1, location.getStreetName());
            st.setInt(2, location.getSpeedLimit());
            st.setDouble(3, location.getSpeed());
            st.setDouble(4, location.getLongitude());
            st.setDouble(5, location.getLatitude());
            st.executeUpdate();
            history.add(location);
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
        return true;
    }

    @Override
    public ArrayList<LocationData> getList() {
        ArrayList<LocationData> newClone= new ArrayList();
        for(int i=0;i<history.size();i++){
            newClone.add(history.get(i));
        }
        return newClone;
    }

    public void deleteLocation(String streetName) {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("DELETE FROM HISTORY WHERE streetName=? ");
            st.setString(1, streetName);
            st.executeUpdate();
            boolean flag=false;
            for(int i=0;i<history.size()&&!flag;i++){
                if(history.get(i).getStreetName().equals(streetName)){
                    history.remove(i);
                    flag=true;
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }


    public boolean existsLocation(LocationData location) {
        return existsLocation(location.streetName);
    }

    public boolean existsLocation(String streetName) {
        String compare="";
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM HISTORY WHERE streetName=? ");
            st.setString(1, streetName);
            ResultSet res=st.executeQuery();
            if (res.next()){
                compare=res.getString(1);
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
        return streetName.equals(compare);
    }
    private void load(){
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM HISTORY ");
            ResultSet res=st.executeQuery();
            while (res.next()){
                final String street= res.getString("streetName");
                final double longitude= res.getDouble("longitude");
                final double latitude= res.getDouble("latitude");
                final int limit= res.getInt("speedLimit");
                final float speed= res.getFloat("speed");
                LocationData obj= new LocationData(longitude, latitude, limit, street);
                obj.setSpeed(speed);
                history.add(obj);
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }
}


