package utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;
import play.db.ebean.Transactional;
import play.mvc.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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
            //card.saveManyToManyAssociations("foreign_Names");

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

    public static ArrayList<ForeignName> parseForeignNames(JsonNode node) {
        ArrayList<ForeignName> result = new ArrayList<>();
        for(int i=0; i<node.size();i++) {
            ForeignName f = new ForeignName(node.get(i).get("language").asText(), node.get(i).get("name").asText());
            result.add(f);
        }
        return result;
    }

    public static ArrayList<Legality> parseLegalities(JsonNode node) {
        ArrayList<Legality> result = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> itr = node.fields();
        while(itr.hasNext()) {
            Map.Entry<String, JsonNode> n = itr.next();
            Legality f = new Legality(n.getKey(), n.getValue().asText());
            result.add(f);
        }
        return result;
    }

    public static ArrayList<Ruling> parseRulings(JsonNode node) {
        ArrayList<Ruling> result = new ArrayList<>();
        for(int i=0; i<node.size(); i++) {
            Ruling r = new Ruling(node.get(i).get("date").asText(), node.get(i).get("text").asText());
            result.add(r);
        }
        return result;
    }
}
