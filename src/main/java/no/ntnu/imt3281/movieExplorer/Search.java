package no.ntnu.imt3281.movieExplorer;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Search {

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

    public static JSON actors(int i) {
        String req;
        try {
            req = Unirest.get("https://api.themoviedb.org/3/movie/"+i+"/credits?api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
            return new JSON(req);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSON takesPartIn(int i) {
        String req;
        try {
            req = Unirest.get("https://api.themoviedb.org/3/discover/movie?with_people=138&page=1&include_video=false&include_adult=false&sort_by=popularity.desc&language=en-US&api_key=a47f70eb03a70790f5dd711f4caea42d").asString().getBody();
            return new JSON(req);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
