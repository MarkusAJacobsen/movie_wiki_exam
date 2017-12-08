package no.ntnu.imt3281.movieExplorer;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Objects;

/**
 * This class checks if a genre is registered in the DB
 * If not, go to themoviedb and fetch an updated list
 */
public class Genres {

    /**
     * Checks if a genre is in DB if not fetch from themovieDB
     * @param i External ID to fetch
     * @return corresponding name of genre to the i
     */
    public static String resolve(int i) {
        InformationDB DB = InformationDB.getInstance();

        if(!DB.inDb(Integer.toString(i), "Genres")) {
            String reqMovie, reqTv;
            JSON objMovie, objTv;
            try {
                reqMovie = Unirest.get("https://api.themoviedb.org/3/genre/movie/list?language=en-US&api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
                reqTv = Unirest.get("https://api.themoviedb.org/3/genre/tv/list?api_key=a47f70eb03a70790f5dd711f4caea42d&language=en-US").asString().getBody();
                objMovie = new JSON(reqMovie);
                objTv = new JSON(reqTv);
                JSON genresMovie = objMovie.get("genres");
                JSON genresTV = objTv.get("genres");

                for(int j = 0; j < genresMovie.size(); j++) {
                    JSON o = genresMovie.get(j);
                    DB.addGenres(o.getValue("id").toString(), o.getValue("name").toString());
                }

                for(int j = 0; j < genresTV.size(); j++) {
                    InformationDB DB2 = InformationDB.getInstance();
                    JSON o = genresTV.get(j);
                    DB2.addGenres(o.getValue("id").toString(), o.getValue("name").toString());
                }

                InformationDB DB3 = InformationDB.getInstance();
                String result = DB3.fetchField(i);
                if(!Objects.equals(result, "")){
                    return "-1";
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }

        } else
            return DB.fetchField(i);
        return "";
    }
}
