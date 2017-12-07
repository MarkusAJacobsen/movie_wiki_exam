package no.ntnu.imt3281.movieExplorer;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.Arrays;
import java.util.Map;


public class JSON {
    public JSONObject obj;

    public JSON(String jsonInput) {
        JSONParser parser = new JSONParser();
        try {
            this.obj = (JSONObject) parser.parse(jsonInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public <T> T getValue(String query) {
        if(obj.get(query) != null) {
            T value = (T) obj.get(query);
            if(value instanceof String) {
                return (T) value.toString();
            } else if (value instanceof Long) {
                return value;
            }

        }
        return (T) "";
    }

    public JSON get(String query) {
        Object tmp = obj.get(query);

        if(tmp != null) {
            if(obj.get(query) instanceof JSONArray) {
                return new JSON(tmp.toString());
            }
        }
        return null;
    }

    public int size() {
        return 0;
    }


    public JSON get(int i) {
        return new JSON("he");
    }
}
