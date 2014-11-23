package utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Card;
import models.CardSet;
import play.db.ebean.Transactional;
import play.mvc.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static play.mvc.Results.ok;

// Utilities for parsing and searching json files
public class JsonUtil {

    @Transactional
    public static Result loadCardsDB(String s) throws IOException {

        File f = new File("app/assets/json/AllSets-x.json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        @SuppressWarnings("unchecked")
        List<Card> allCards =  getAllCards((Map<String, CardSet>)mapper.readValue(f, new TypeReference<Map<String, CardSet>>() {}));

        for(Card card : allCards){

            card.save();

            //not necessary?!
            //card.saveManyToManyAssociations("colors");
        }

        return ok();
    }

    public static List<Card> getAllCards(Map<String, CardSet> sets){

        List<Card> allCards = new ArrayList<>();

        for(CardSet set : sets.values()){
            for(Card card : set.getCards()){
                allCards.add(card);
            }
        }

        return allCards;
    }

    public static ArrayList<String> parseStringArray(JsonNode node) {
        ArrayList<String> result = new ArrayList<>();
        for(int i=0; i<node.size();i++) {
            result.add(node.get(i).asText());
        }
        return result;
    }

}
