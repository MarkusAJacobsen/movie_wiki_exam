package no.ntnu.imt3281.movieExplorer;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Search {

    /**
     * Uses themoviedb's search API, encodes the query
     *"Search multiple models in a single request. Multi search currently supports searching for movies, tv shows and people in a single request."
     * @param query Wanted search query
     * @return new JSON Object with the body of the call
     */
    public static JSON multiSearch(String query) {
        String req;
        String searchString = null;
        try {
            searchString = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            req = Unirest.get("https://api.themoviedb.org/3/search/multi?include_adult=false&page=1&query="+searchString+"&language=en-US&api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
            return new JSON(req);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * "Get a movie or TV credit by ID"
     * @param i ID to search for
     * @return new JSON object with the body of the call
     */
    public static JSON actors(int i) {
        InformationDB db = InformationDB.getInstance();
        String req;
        String id;
        if(!db.inDb(Integer.toString(i), "ActorsInMovie")) {
            try {
                req = Unirest.get("https://api.themoviedb.org/3/movie/" + i + "/credits?api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
                JSON o = new JSON(req);
                id = o.get(1).getValue("id").toString();
                db.saveMovieCreditInDB(id, req);
                return o;
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        } else {
            String tmp = db.fetchMovieActorCredit(Integer.toString(i));
            return new JSON(tmp);

        }
        return null;
    }

    /**
     * "Discover movies by different types of data like average rating, number of votes, genres and certifications. You can get a valid list of certifications from the method."
     * Used in this context to find in which works a person takes part in
     * @param i ID of person
     * @return new JSON object with the body of the call
     */
    public static JSON takesPartIn(int i) {
        String req;
        try {
            req = Unirest.get("https://api.themoviedb.org/3/discover/movie?with_people="+i+"&page=1&include_video=false&include_adult=false&sort_by=popularity.desc&language=en-US&api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
            return new JSON(req);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSON movie(long i) {
        String req;
        try {
            req = Unirest.get("https://api.themoviedb.org/3/movie/"+i+"?language=en-US&api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
            return new JSON(req);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
