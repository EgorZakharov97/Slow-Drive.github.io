
package com.Database;

import com.CustomExceptions.PersistenceException;
import com.Database.Interface.IDriverDB;
import com.Logic.Object.Driver;
import com.Logic.Utils.PasswordUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverDB implements IDriverDB {

    private final String dbPath;

    public DriverDB(final String dbPath) {

        this.dbPath = dbPath;
    }



    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }


    //RETRIEVAL
    private Driver fromResultSet(final ResultSet rs) throws SQLException {
        final String firstName = rs.getString("firstname");
        final String lastName = rs.getString("lastname");
        final String email = rs.getString("email");
        final String username = rs.getString("username");
        final String password = rs.getString("password");


        return new Driver(firstName, lastName, email, username, password);
    }

    @Override
    public String getDriverSalt(String username) {

        String salt = null;
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM DRIVER WHERE USERNAME = ?");
            st.setString(1, username);

            ResultSet rs = st.executeQuery();
            if (rs.next()){
                salt = rs.getNString("salt");
            }


            st.close();
            rs.close();



        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

        return salt;
    }

    @Override
    public Driver getDriver(String username) {

        Driver d=null;
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM DRIVER WHERE USERNAME = ?");
            st.setString(1, username);

            ResultSet rs = st.executeQuery();
            if (rs.next()){
                d= new Driver(rs.getNString("firstName"),rs.getString("lastName"),rs.getString("userName"),rs.getString("password"),rs.getString("email"));
            }


            st.close();
            rs.close();



        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

        return d;
    }

    @Override
    public void insert(Driver d){

        try(final Connection con = connection()){
            Driver driver=getDriver(d.getUserName());
            if(driver==null) {
                final PreparedStatement st = con.prepareStatement("INSERT INTO DRIVER VALUES( ?, ?, ?, ?, ?, ?)");
                st.setString(1, d.getFirstName());
                st.setString(2, d.getLastName());
                st.setString(3, d.getEmail());
                st.setString(4, d.getUserName());

                String salt = PasswordUtils.getSalt();
                String securePassword = PasswordUtils.generateSecurePassword(d.getPassword(), salt);

                st.setString(5, securePassword);
                st.setString(6, salt);

                st.executeUpdate();
            }
        }  catch (SQLException e) {
            throw new PersistenceException(e);
        }


    }
    
    // Change the password of the driver
    @Override
    public void changePassword (String newPassword, Driver d){
        try(final Connection con = connection()){

            // Assure driver exists in database
            // UPDATE did not work well, so Instead re-inserting the driver with new password
            Driver driver = getDriver(d.getUserName());
            if(driver!=null) {
                //delete(driver);
                System.out.println ("PRE RUN");
                final PreparedStatement st = con.prepareStatement("UPDATE DRIVER SET password = ?, salt = ? WHERE USERNAME = ?;");


                String salt = PasswordUtils.getSalt(); //generate new salt
                String securePassword = PasswordUtils.generateSecurePassword(newPassword, salt);


                st.setString(1, securePassword);
                st.setString(2, salt);
                st.setString(3, d.getUserName());

                System.out.println (st.toString());
                st.executeUpdate();
            }
        }  catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void delete(Driver d){
        try(final Connection con = connection()){

            PreparedStatement pst = con.prepareStatement("DELETE FROM DRIVER WHERE USERNAME = ?");
            pst.setString(1, d.getUserName());
            pst.executeUpdate();

        }  catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
