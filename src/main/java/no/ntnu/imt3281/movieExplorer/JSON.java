package no.ntnu.imt3281.movieExplorer;

import org.json.simple.*;
import org.json.simple.parser.*;



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

    public JSON get(String genres) {
        return null;
    }

    public double size() {
        return 0.0;
    }

    public JSON get(int i) {
        return null;
    }
}
