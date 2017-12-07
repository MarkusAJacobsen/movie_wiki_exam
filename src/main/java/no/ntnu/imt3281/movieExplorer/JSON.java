package no.ntnu.imt3281.movieExplorer;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.ArrayList;
import java.util.Set;

/**
 * JSON object has a list capable of holding objects of type JSON
 * the structure allows it to either hold a instance of JSONObject, in
 * which the key and value field is used, or a JSONArray, in which the
 * obj and key field is used.
 */
public class JSON {
    public ArrayList<JSON> obj;
    public String key;
    public Object value;

    public JSON(String jsonInput) {
        this.obj = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject objects = (JSONObject) parser.parse(jsonInput);
            Set<String> keys = objects.keySet();
            this.addInstance(keys, objects);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for a JSONObject
     * @param key - Name or key value
     * @param tmp - Object it self
     */
    public JSON(String key, Object tmp) {
        obj = new ArrayList<>();
        this.key = key;
        this.value = tmp;
    }

    /**
     * Constructor for a JSONArray
     * @param tmp
     */
    public JSON(JSONArray tmp) {
        this.obj = new ArrayList<>();
        this.key = null;
        this.value = null;

        for (Object object : tmp) {
            if (object instanceof JSONArray) {
                obj.add((new JSON((JSONArray) object)));
            } else if (object instanceof JSONObject) {
                obj.add(new JSON(((JSONObject) object).toJSONString()));
            }
        }
    }

    /**
     * Ported main constructor for reduced cognitive complexity (SonarLint)
     * @param keys - posts key values
     * @param posts - All the JSONObjects created from a JSON input string
     */
    public void addInstance(Set<String> keys, JSONObject posts) {
        Object tmp;
        for(String query : keys) {
            tmp = posts.get(query);

            if (tmp instanceof JSONArray) {
                JSON array = new JSON((JSONArray) tmp);
                array.key = query;
                obj.add(array);

            } else if (tmp instanceof  JSONObject) {
                JSON singleObject = new JSON(((JSONObject) tmp).toJSONString());
                obj.add(singleObject);
            } else
                obj.add(new JSON(query,tmp));
        }
    }

    /**
     * Used to return a JSONObject
     * @param query - Key value to find
     * @return Found objective
     */
    public Object getValue(String query) {
        return this.get(query).value;
    }

    /**
     * Loops through this.obj, if found return corresponding JSON object
     * @param query
     * @return
     */
    public JSON get(String query) {
        for (JSON o : obj)  {
            if (query.equals(o.key))
                return o;
        }
        return null;
    }

    /**
     * Returns the size of a JSON object
     * @return
     */
    public int size() {
        return this.obj.size();
    }

    /**
     * Finds a JSON's wanted index and returns corresponding JSON object
     * @param i Index to find
     * @return Found JSON object
     */
    public JSON get(int i) {
       return this.obj.get(i);
    }
}
