package no.ntnu.imt3281.movieExplorer;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


public class JSON {
    public ArrayList<JSON> obj;
    public String key;
    public Object value;

    public JSON(String jsonInput) {
        this.obj = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject objects = (JSONObject) parser.parse(jsonInput);
            Object tmp;
            Set<String> keys = objects.keySet();

            for(String key : keys) {
                tmp = objects.get(key);

                if (tmp instanceof JSONArray) {
                    JSON array = new JSON((JSONArray) tmp);
                    array.key = key;
                    obj.add(array);

                } else if (tmp instanceof  JSONObject) {
                    JSON singleObject = new JSON(((JSONObject) tmp).toJSONString());
                    obj.add(singleObject);
                } else
                    obj.add(new JSON(key,tmp));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public JSON(String key, Object tmp) {
        this.key = key; this.value = tmp;
        obj = new ArrayList<>();
    }

    public JSON(JSONArray tmp) {
        
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
        for (JSON o : obj)  {
            if (query.equals(o.key))
                return o;
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
