package no.ntnu.imt3281.movieExplorer;

import java.sql.*;

/**
 * GenresDB is the database controller for the Genres DB.
 * This db contains registered genres from themoviedb
 *
 * The code is mostly refactored from the lectures and project 2 in this subject
 */
public class GenresDB {
    private Connection con;
    private final String url = "jdbc:derby:genres.db";
    private boolean connectedToDB = false;
    private static GenresDB instance = null;
    private static final String TABLENAME = "Genres";

    /**
     * Constructor, setsup connection and creates the table
     * @throws SQLException
     */
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

    /**
     * This DB is sat up as a singleton, so if it has been instantiated
     * before, only return the "living" instance
     * @return Instance of this DB
     */
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

    /**
     * Creates a table with to fields, number and name representing the field from
     * themoviedb. Also has an internal incrementor
     */
    private void createTable() {
        Statement stm = null;

        try                                         // Check if table exist already
        {
            stm = con.createStatement();
            ResultSet tableExists = stm.executeQuery("SELECT * FROM "+ TABLENAME);
            tableExists.close();
            stm.close();
        }
        catch(SQLException e1)
        {
            try                                     // Create the table
            {
                stm = con.createStatement();
                stm.execute("CREATE TABLE " + TABLENAME +"(id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                       + "number VARCHAR(255) NOT NULL, "
                       + " name varchar(255), "
                       + " PRIMARY KEY ( id ))");
                stm.close();
            }
            catch (SQLException e2)
            {
               e2.printStackTrace();
            }
        }
    }

    /**
     * Adds a genre to the DB
     * @param id External ID
     * @param name External genre name
     * @return return code
     */
    public int addGenres(String id, String name) {
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
                stm = con.prepareStatement("INSERT INTO "+ TABLENAME+" (Number, Name) VALUES (?, ?)");

                stm.setString(1, id); // Make the statement ready
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

    /**
     * Checks if an external ID is registered in the DB before
     * @param id External ID to check if exist
     * @return true/false found/not Found
     */
    public boolean GenreInDb(String id) {
        ResultSet res;                              // Variable to store the result
        String statement = "SELECT Number FROM " +TABLENAME
                + "\n WHERE Number=?";

        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement(statement);
            // Prepare and execute the statment
            stm.setString(1, id);
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

    /**
     * Fetch the "name" field of a DB post
     * @param i External ID to fetch
     * @return corresponding name for supplied i
     */
    public String fetchField(int i) {
        ResultSet res;
        String statement = "SELECT Number, Name FROM " +TABLENAME
                + "\n WHERE Number=?";

        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement(statement);
            // Prepare and execute the statment
            stm.setString(1, Integer.toString(i));
            res = stm.executeQuery();

            if (!res.next())
            {
                stm.close();
                return "";
            }
            // User found
            else
            {
                String result = res.getString("Name");
                stm.close();
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
