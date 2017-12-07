package no.ntnu.imt3281.movieExplorer;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Genres {



    public static String resolve(int i) {
        GenresDB DB = GenresDB.getInstance();

        if(!DB.GenreInDb(i)) {
            String req;
            JSON obj;
            try {
                req = Unirest.get("https://api.themoviedb.org/3/genre/movie/list?language=en-US&api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
                obj = new JSON(req);

                for(int j = 0; j < obj.size(); j++) {
                    DB.addGenres(Integer.parseInt(obj.key), obj.value.toString());
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }

        }
        return "";
    }
}
