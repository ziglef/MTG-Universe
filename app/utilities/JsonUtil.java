package utilities;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import models.Card;
import play.db.ebean.Transactional;
import play.mvc.*;

import java.io.*;

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

        Gson gson = new Gson();

        try {
            while(jsonReader.hasNext() && jsonReader.peek() != JsonToken.END_DOCUMENT){ //old way (didnt work!) jsonReader.peek() != JsonToken.END_DOCUMENT
                Card card = gson.fromJson(jsonReader, Card.class);
                card.save();
            }

            return ok();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return badRequest();
    }

}
