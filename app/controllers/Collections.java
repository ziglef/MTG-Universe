package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.AbstractSetofCards;
import models.Card;
import models.Collection;
import models.enums.Visibility;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;

import java.util.List;


public class Collections extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public static Result addCollection() {

        String name;
        String visibility;

        JsonNode json = request().body().asJson();

          if(json == null) {
            return badRequest("Expecting Json data");
          } else {
            name = json.findPath("name").textValue();
            visibility = json.findPath("visibility").textValue();
            if(name == null) {
              return badRequest("Missing parameter [name]");
            }
          }

        Collection collection;
        if(visibility.equals("public"))
            collection =  Collection.create(name, Integer.parseInt(session().get("id")), Visibility.PUBLIC_);
        else if(visibility.equals("friends"))
            collection =  Collection.create(name, Integer.parseInt(session().get("id")), Visibility.FRIENDS);
        else
            collection =  Collection.create(name, Integer.parseInt(session().get("id")), Visibility.PRIVATE);


       // String response = new String("<li><a href=\"#\" id=\"col"+collection.id+"\">" + name + "</a><a href=\"#\" name=\""+collection.id+"\" onclick=\"removeCollection(this.name)\" class=\"badge pull-right label-danger remove\">X</a><a href=\"#\" class=\"badge pull-right label-warning\">Edit</a></li>");
        ObjectNode result = Json.newObject();
        result.put("url", "/user");
        result.put("id", collection.id);

        return ok(result);
    }


    public static Result deleteCollection() {

        Integer id;

        JsonNode json = request().body().asJson();

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            id = Integer.parseInt(json.findPath("id").textValue().replace("col",""));
            if(id == null) {
                return badRequest("Missing parameter [id]");
            }
        }

        Collection.find.ref(id).delete();
        String response = new String("ok");
        return ok(Json.toJson(response));
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


    public static JsonNode getUserCollections(){
        List<Collection> collections = Collection.findUserCollections(Integer.parseInt(session().get("id")));
        return Json.toJson(collections);
    }

}