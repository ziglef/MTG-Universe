package utilities;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Map;

// Utilities for parsing and searching json files
public class JsonUtil {

    //TODO: FIX ME!

    // Search the json file returning the first appearence of an object with a given field value
    // Returns: onSuccess -> the JSONObject ; onFail -> null
    public static JsonObject find(File f, String field, String value){

        JsonReader jsonReader;

        try {
            jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(f)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File " + f.toString() + " was not found!");
            return null;
        }

        JsonParser jsonParser = new JsonParser();

        // FIX FROM HERE ON //

        JsonObject allCards = jsonParser.parse(jsonReader).getAsJsonObject();

        for(Map.Entry<String, JsonElement> card : allCards.entrySet()){
            String fieldValue = ((JsonObject)card.getValue()).get(field).toString();

            if( fieldValue.equals(value) ) return (JsonObject)card.getValue();
        }


        return null;
    }

}
