package controllers;

import models.AbstractSetofCards;
import models.Card;
import models.Collection;
import models.enums.Visibility;
import play.mvc.Controller;
import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;


public class Collections extends Controller {

    public static Result addCollection() {
        
        String name;
        
        JsonNode json = request().body().asJson();
          if(json == null) {
            return badRequest("Expecting Json data");
          } else {
            name = json.findPath("name").textValue();
            if(name == null) {
              return badRequest("Missing parameter [name]");
            }
          }
        
        Collection collection = Collection.create(
                name,
                2, //user id
                Visibility.PUBLIC_
        );
        
        return ok();
    }

    public static Result deleteCollection(Integer collectionId) {
        Collection.find.ref(collectionId).delete();
        return ok(); //FIXME
    }

    public static Result renameCollection(Integer collectionId, String newName) {
        Collection.find.ref(collectionId).rename(newName);
        return ok(); //FIXME
    }

    public static Result addCardtoCollection(Integer collectionId, Integer cardId) {
        AbstractSetofCards collection = Collection.find.ref(collectionId);
        Card newcard = Card.find.byId(cardId.toString());
        collection.addCard(newcard);
        collection.save();
        collection.saveManyToManyAssociations("cards");
        return ok(); //FIXME
    }

    public static Result getUserCollections(Integer userId) {
        List<Collection> collections = Collection.findUserCollections(userId);
        return ok(); //FIXME
    }

        
    public static Result getUserCollections(){
        List<Collection> collections = Collection.findUserCollections(Integer.parseInt(session().get("id")));
        return ok(play.libs.Json.toJson(collections));
        
    }
                  

}