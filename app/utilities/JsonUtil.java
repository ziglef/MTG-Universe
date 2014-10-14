package utilities;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import models.Card;
import play.db.ebean.Transactional;
import play.mvc.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

// Utilities for parsing and searching json files
public class JsonUtil {

    @Transactional
    public static Result loadCardsDB(String s){

        File f = new File("app/assets/json/AllCards-x.json");

        JsonReader jsonReader;

        try {
            jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File " + f.toString() + " was not found!");
            return badRequest("/");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("UTF-8 not supported!");
            return badRequest("/");
        }

        Type type = new TypeToken<List<Card>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(type, new JsonDeserialize());

        Gson gson = gsonBuilder.create();

        List<Card> cards = gson.fromJson(jsonReader, type);

        for(Card c : cards)
            c.save();

        return ok();
    }

    public static class JsonDeserialize implements JsonDeserializer<List<Card>>{
        Gson gson = new Gson();

        @Override
        public List<Card> deserialize(JsonElement el, Type type, JsonDeserializationContext context) throws JsonParseException {
            List<Card> ls = new ArrayList<Card>();
            JsonArray jarr = el.getAsJsonArray();

            for(JsonElement e : jarr){
                Card c = gson.fromJson(e, Card.class);
                if( c != null )
                    ls.add(c);
            }
            return ls;
        }
    }

}
