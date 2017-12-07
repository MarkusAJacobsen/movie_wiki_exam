package no.ntnu.imt3281.movieExplorer;

import java.sql.*;

public class GenresDB {
    private Connection con;
    private final String url = "jdbc:derby:genres.db";
    private boolean connectedToDB = false;
    private static GenresDB instance = null;

    private GenresDB() throws SQLException
    {
        try
        {
            con = DriverManager.getConnection(url);

        }
        catch (SQLException e)
        {
            con = DriverManager.getConnection(url + ";create=true");
        }
        createTable();                              // Create new Table
        connectedToDB = true;                       // Set connection to true

    }

    public static GenresDB getInstance() {
        if(instance == null) {
            try {
                instance = new GenresDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private void createTable() {
        Statement stm = null;

        try                                         // Check if table exist already
        {
            stm = con.createStatement();
            ResultSet tableExists = stm.executeQuery("SELECT * FROM Genres");
            tableExists.close();
            stm.close();
        }
        catch(SQLException e1)
        {
            try                                     // Create the table
            {
                stm = con.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS Genres " +
                        "(Id INTEGER(11) NOT NULL, " +
                        " Name VARCHAR(255), " +
                        " PRIMARY KEY ( Id ))";
                stm.executeUpdate(createTable);
                stm.close();
            }
            catch (SQLException e2)
            {
               e2.printStackTrace();
            }
        }
    }

    public int addGenres(int id, String name) {
        if (!connectedToDB)                          // Is there connection with DB?
            throw new IllegalStateException("Not contact with DB");
        // check if username is used
        if (GenreInDb(id))                      // If exist !!
        {
            return -1;
        }
        else                                         // Not exist, add new !!
        {
            PreparedStatement stm = null;
            try {
                stm = con.prepareStatement("INSERT INTO Genres (Id, Name) VALUES (?, ?)");

                stm.setInt(1, id); // Make the statement ready
                stm.setString(2, name); // hashed password

                // Execute to update
                int insertedLines = stm.executeUpdate();
                stm.close();

                if (insertedLines > 0)
                    return 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public boolean GenreInDb(Integer id) {
        ResultSet res;                              // Variable to store the result
        String statement = "SELECT Id FROM Genres"
                + "\n WHERE Id=?";

        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement(statement);
            // Prepare and execute the statment
            stm.setInt(1, id);
            res = stm.executeQuery();

            if (!res.next())
            {
                stm.close();
                return false;
            }
            // User found
            else
            {
                stm.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
