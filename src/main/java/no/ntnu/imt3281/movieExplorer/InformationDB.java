package no.ntnu.imt3281.movieExplorer;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.GLOBAL_LOGGER_NAME;

/**
 * GenresDB is the database controller for the Genres DB.
 * This db contains registered genres from themoviedb
 *
 * The code is mostly refactored from the lectures and project 2 in this subject
 */
public class InformationDB {
    private Connection con;
    private static final String url = "jdbc:derby:genres.db";
    private boolean connectedToDB = false;
    private static InformationDB instance = null;
    private static final String TABLENAME = "Genres";
    private static final String TABLENAME2 = "ActorsInMovie";
    private static final String TABLENAME3 = "TakesPartIn";
    private static final String TABLENAME4 = "Movies";
    private static final Logger LOGGER = Logger.getLogger(GLOBAL_LOGGER_NAME);

    /**
     * Constructor, setsup connection and creates the table
     * @throws SQLException Error in getting connection
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
        createTableGenres();
        createTableActorsCreditInMovie();
        createTableTakesPartIn();
        createTableMovies();
        connectedToDB = true;
    }


    /**
     * This DB is sat up as a singleton, so if it has been instantiated
     * before, only return the "living" instance
     * @return Instance of this DB
     */
    static InformationDB getInstance() {
        if(instance == null) {
            try {
                instance = new InformationDB();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString());
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
            ResultSet rs = stm.executeQuery("SELECT * FROM "+ TABLENAME);
            rs.close();
            stm.close();
        } catch(SQLException e1) {
            try                                     // Create the table
            {
                stm = con.createStatement();
                stm.execute("CREATE TABLE  " + TABLENAME +"(id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                       + "number VARCHAR(255) NOT NULL, "
                       + " name varchar(255), "
                       + " PRIMARY KEY ( id ))");

                stm.close();
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, e2.toString());
            } finally {
                try { stm.close(); } catch (Exception e) { /* ignored */ }
            }
        } finally {
            try { stm.close(); } catch (Exception e) { /* ignored */ }
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
        } catch(SQLException e1) {
            try                                     // Create the table
            {
                stm = con.createStatement();

                stm.execute("CREATE TABLE " + TABLENAME2 + "(id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                        + "number VARCHAR(55), "
                        + "actorString CLOB, "
                        + "PRIMARY KEY (id))");
                stm.close();
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, e2.toString());
            } finally {
                try { stm.close(); } catch (Exception e) { /* ignored */ }
            }
        } finally {
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    private void createTableTakesPartIn() {
        Statement stm = null;
        try                                         // Check if table exist already
        {
            stm = con.createStatement();
            ResultSet tableExists = stm.executeQuery("SELECT * FROM "+ TABLENAME3);
            tableExists.close();
            stm.close();
        } catch(SQLException e1) {
            try                                     // Create the table
            {
                stm = con.createStatement();

                stm.execute("CREATE TABLE " + TABLENAME3 + "(id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                        + "number VARCHAR(55), "
                        + "takesPartInString CLOB, "
                        + "PRIMARY KEY (id))");
                stm.close();
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, e2.toString());
            } finally {
                try { stm.close(); } catch (Exception e) { /* ignored */ }
            }
        } finally {
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    private void createTableMovies() {
        Statement stm = null;
        try                                         // Check if table exist already
        {
            stm = con.createStatement();
            ResultSet tableExists = stm.executeQuery("SELECT * FROM "+ TABLENAME4);
            tableExists.close();
            stm.close();
        } catch(SQLException e1) {
            try                                     // Create the table
            {
                stm = con.createStatement();

                stm.execute("CREATE TABLE " + TABLENAME4 + "(id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                        + "number VARCHAR(55), "
                        + "movieString CLOB, "
                        + "PRIMARY KEY (id))");
                stm.close();
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, e2.toString());

            } finally {
                try { stm.close(); } catch (Exception e) { /* ignored */ }
            }
        } finally {
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    /**
     * Adds a genre to the DB
     * @param id External ID
     * @param name External genre name
     * @return return code
     */
    int addGenres(String id, String name) {
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
                LOGGER.log(Level.SEVERE, e.toString());
            } finally {
                try { stm.close(); } catch (Exception e) { /* ignored */ }
            }
        }
        return 0;
    }

    /**
     * Checks if an external ID is registered in the DB before
     * @param id External ID to check if exist
     * @param tableName name of the table to check against
     * @return true/false found/not Found
     */
    boolean inDb(String id, String tableName) {
        ResultSet res = null;                              // Variable to store the result
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
            LOGGER.log(Level.SEVERE, e.toString());
        } finally {
            try { res.close(); } catch (Exception e) { /* ignored */ }
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
        return false;
    }




    int saveMovieCreditInDB(String number, String jsonString) {
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
                LOGGER.log(Level.SEVERE, e.toString());
            } finally {
                try { stm.close(); } catch (Exception e) { /* ignored */ }
            }
            return 0;
        }
    }

    int saveTakesPartInInDB(String number, String jsonString) {
        if (!connectedToDB)                          // Is there connection with DB?
            throw new IllegalStateException("Not contact with DB");
        // check if username is used
        if (inDb(number, TABLENAME3))                      // If exist !!
            return -1;
        else                                         // Not exist, add new !!
        {
            PreparedStatement stm = null;
            try {
                stm = con.prepareStatement("INSERT INTO " + TABLENAME3 + " (Number, TakesPartInString) VALUES (?, ?)");

                stm.setString(1, number);
                stm.setString(2, jsonString); // Make the statement ready


                // Execute to update
                int insertedLines = stm.executeUpdate();
                stm.close();

                if (insertedLines > 0)
                    return 1;
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString());
            } finally {
                try { stm.close(); } catch (Exception e) { /* ignored */ }
            }
            return 0;
        }
    }

    public int saveMovieInDB(String number, String jsonString) {
        if (!connectedToDB)                     // Is there connection with DB?
            throw new IllegalStateException("Not contact with DB");
        // check if username is used
        if (inDb(number, TABLENAME4))                      // If exist !!
            return -1;
        else                                         // Not exist, add new !!
        {
            PreparedStatement stm = null;
            try {
                stm = con.prepareStatement("INSERT INTO " + TABLENAME4 + " (Number, MovieString) VALUES (?, ?)");

                stm.setString(1, number);
                stm.setString(2, jsonString); // Make the statement ready


                // Execute to update
                int insertedLines = stm.executeUpdate();
                stm.close();

                if (insertedLines > 0)
                    return 1;
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.toString());
            } finally {
                try { stm.close(); } catch (Exception e) { /* ignored */ }
            }
            return 0;
        }
    }

    /**
     * Fetch the "name" field of a DB post
     * @param i External ID to fetch
     * @return corresponding name for supplied i
     */
    String fetchField(int i) {
        ResultSet res = null;
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
            LOGGER.log(Level.SEVERE, e.toString());
        } finally {
            try { res.close(); } catch (Exception e) { /* ignored */ }
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
        return "";
    }

    /**
     * Fetch the ActorString == JSON string to be used with the JSON constructor
     * @param i Movie ID
     * @return ActorString
     */
    public String fetchMovieActorCredit(String i) {
        ResultSet res = null;
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
            LOGGER.log(Level.SEVERE, e.toString());
        } finally {
            try { res.close(); } catch (Exception e) { /* ignored */ }
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
        return "";
    }

    /**
     * Fetch which movies a actor takes part in
     * @param i Actor ID
     * @return TakesPartInString
     */
    String fetchTakesPartIn(String i) {
        ResultSet res = null;
        String statement = "SELECT Number, TakesPartInString FROM " +TABLENAME3
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
                String result = res.getString("TakesPartInString");
                stm.close();
                return result;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        } finally {
            try { res.close(); } catch (Exception e) { /* ignored */ }
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
        return "";
    }

    /**
     * Fetch a movie based in Movie ID
     * @param i Movie ID
     * @return Movie String
     */
    public String fetchMovie(String i) {
        ResultSet res = null;
        String statement = "SELECT Number, MovieString FROM " +TABLENAME4
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
                String result = res.getString("MovieString");
                stm.close();
                return result;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        } finally {
            try { res.close(); } catch (Exception e) { /* ignored */ }
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
        return "";
    }

    /**
     * Drop a table from the db
     * @param table table name
     */
    void dropTables(String table) {
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("DROP TABLE " + table);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        } finally {
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    /**
     * Drop all tables in the DB
     */
    void dropAllTables() {
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("DROP TABLE " + TABLENAME);
            stm.executeUpdate();
            stm = con.prepareStatement("DROP TABLE " + TABLENAME2);
            stm.executeUpdate();
            stm = con.prepareStatement("DROP TABLE " + TABLENAME3);
            stm.executeUpdate();
            stm = con.prepareStatement("DROP TABLE " + TABLENAME4);
            stm.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        } finally {
            try { stm.close(); } catch (Exception e) { /* ignored */ }
        }
    }




}
