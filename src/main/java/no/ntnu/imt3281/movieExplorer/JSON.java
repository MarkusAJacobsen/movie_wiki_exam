package no.ntnu.imt3281.movieExplorer;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.Iterator;


public class JSON {
    public JSONObject obj;

    public JSON(String jsonInput) {
        this.obj = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            this.obj = (JSONObject) parser.parse(jsonInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public JSON(Object tmp) {
        this.obj = new JSONObject();
        try {
            this.obj.put("tmp", tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSON (JSONObject tmp) {
        this.obj = tmp;
    }

    public <T> T getValue(String query) {
        if(obj.get(query) != null) {
            T value = (T) obj.get(query);
            if(value instanceof String) {
                return (T) value.toString();
            } else if (value instanceof Long) {
                return value;
            } else
                return value;

        }
        return (T) "";
    }

    public JSON get(String query) {
        Object tmp =  obj.get(query);
        if(tmp != null) {
            if(obj.get(query) instanceof JSONArray) {
                return new JSON(tmp);
            }
        }
        return null;
    }

    public int size() {
        return this.obj.size();
    }


    public JSON get(int i) {
        JSONArray tmp = (JSONArray) this.obj.get("tmp");
        JSONObject tmp2 = (JSONObject) tmp.get(i);
        return new JSON(tmp2);
    }
}
