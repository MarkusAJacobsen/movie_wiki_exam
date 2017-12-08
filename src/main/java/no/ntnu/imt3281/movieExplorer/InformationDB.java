package no.ntnu.imt3281.movieExplorer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;

/**
 * GenresDB is the database controller for the Genres DB.
 * This db contains registered genres from themoviedb
 *
 * The code is mostly refactored from the lectures and project 2 in this subject
 */
public class InformationDB {
    private Connection con;
    private final String url = "jdbc:derby:genres.db";
    private boolean connectedToDB = false;
    private static InformationDB instance = null;
    private static final String TABLENAME = "Genres";
    private static final String TABLENAME2 = "ActorsInMovie";
    private static final String TABLENAME5 = "Movies";

    /**
     * Constructor, setsup connection and creates the table
     * @throws SQLException
     */
    private InformationDB() throws SQLException
    {
        try
        {
            con = DriverManager.getConnection(url);

        }
        catch (SQLException e)
        {
            con = DriverManager.getConnection(url + ";create=true");
        }
        createTableGenres();                              // Create new Table
        createTableActorsCreditInMovie();
        connectedToDB = true;                       // Set connection to true

    }



    /**
     * This DB is sat up as a singleton, so if it has been instantiated
     * before, only return the "living" instance
     * @return Instance of this DB
     */
    public static InformationDB getInstance() {
        if(instance == null) {
            try {
                instance = new InformationDB();
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
    private void createTableGenres() {
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
                stm.execute("CREATE TABLE  " + TABLENAME +"(id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
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

    private void createTableActorsCreditInMovie() {
        Statement stm = null;

        try                                         // Check if table exist already
        {
            stm = con.createStatement();
            ResultSet tableExists = stm.executeQuery("SELECT * FROM "+ TABLENAME2);
            tableExists.close();
            stm.close();
        }
        catch(SQLException e1)
        {
            try                                     // Create the table
            {
                stm = con.createStatement();

                stm.execute("CREATE TABLE " + TABLENAME2 + "(id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                        + "number VARCHAR(55), "
                        + "actorString CLOB, "
                        + "PRIMARY KEY (id))");
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
        if (inDb(id, TABLENAME))                      // If exist !!
        {
            return -1;
        }
        else                                         // Not exist, add new !!
        {
            PreparedStatement stm = null;
            try {
                stm = con.prepareStatement("INSERT INTO "+ TABLENAME+" (Number, Name) VALUES (?, ?)");

                stm.setString(1, id); // Make the statement ready
                stm.setString(2, name);

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
    public boolean inDb(String id, String tableName) {
        ResultSet res;                              // Variable to store the result
        String statement = "SELECT Number FROM " +tableName
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

    public int saveMovieCreditInDB(String number, String jsonString) {
        if (!connectedToDB)                          // Is there connection with DB?
            throw new IllegalStateException("Not contact with DB");
        // check if username is used
        if (inDb(number, TABLENAME2))                      // If exist !!
            return -1;
        else                                         // Not exist, add new !!
        {
            PreparedStatement stm = null;
            try {
                stm = con.prepareStatement("INSERT INTO " + TABLENAME2 + " (Number, ActorString) VALUES (?, ?)");

                stm.setString(1, number);
                stm.setString(2, jsonString); // Make the statement ready


                // Execute to update
                int insertedLines = stm.executeUpdate();
                stm.close();

                if (insertedLines > 0)
                    return 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    public void dropTables() {
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("DROP TABLE " + TABLENAME2);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String fetchMovieActorCredit(String i) {
        ResultSet res;
        String statement = "SELECT Number, ActorString FROM " +TABLENAME2
                + "\n WHERE Number=?";

        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement(statement);
            // Prepare and execute the statment
            stm.setString(1, i);
            res = stm.executeQuery();

            if (!res.next())
            {
                stm.close();
                return "";
            }
            // User found
            else
            {
                String result = res.getString("ActorString");
                stm.close();
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

}
