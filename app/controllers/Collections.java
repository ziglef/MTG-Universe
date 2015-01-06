package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import models.Card;
import models.Collection;
import models.enums.Visibility;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.List;


public class Collections extends Controller {

    public static Result createCollectionData(){
        return ok(collections.render(Collections.getUserCollections()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result addCollection() {

        String name;
        String visibility;
        String type;

        JsonNode json = request().body().asJson();

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            name = json.findPath("name").textValue();
            visibility = json.findPath("visibility").textValue();
            if(name == null) {
                return badRequest("Missing parameter [name]");
            }
            type = json.findPath("type").textValue();
        }

        Collection collection;
        if(visibility.equals("public"))
            collection =  Collection.createCollection(name, Integer.parseInt(session().get("id")), Visibility.PUBLIC_, type);
        else if(visibility.equals("friends"))
            collection =  Collection.createCollection(name, Integer.parseInt(session().get("id")), Visibility.FRIENDS, type);
        else
            collection =  Collection.createCollection(name, Integer.parseInt(session().get("id")), Visibility.PRIVATE, type);


        // String response = new String("<li><a href=\"#\" id=\"col"+collection.id+"\">" + name + "</a><a href=\"#\" name=\""+collection.id+"\" onclick=\"removeCollection(this.name)\" class=\"badge pull-right label-danger remove\">X</a><a href=\"#\" class=\"badge pull-right label-warning\">Edit</a></li>");
        ObjectNode result = Json.newObject();
        result.put("url", "/editCollection/"+collection.id);

        return ok(result);
    }


    public static Result deleteCollection() {

        JsonNode json = request().body().asJson();

        Integer id;

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            id = Integer.parseInt(json.findPath("colID").textValue());
            if(id == null) {
                return badRequest("Missing parameter [id]");
            }
        }

        Collection.find.ref(id).delete();
        String response = new String("ok");
        return ok(Json.toJson(response));
    }

    public static Result deleteCardFromCollection() {
        JsonNode json = request().body().asJson();

        Integer collectionId = Integer.parseInt(json.findPath("colID").textValue());
        Integer cardId = Integer.parseInt(json.findPath("cardID").textValue());

        Collection collection = Collection.find.ref(collectionId);
        collection.removeCard(cardId);
        collection.save();
        collection.saveManyToManyAssociations("cards");

        String response = new String("ok");
        return ok(Json.toJson(response));
        //TODO error response
    }

    public static Result renameCollection(Integer collectionId, String newName) {
        Collection.find.ref(collectionId).rename(newName);
        return ok(); //FIXME
    }

    public static Result addCardtoCollection() {

        JsonNode json = request().body().asJson();

        Integer collectionId = Integer.parseInt(json.findPath("colID").textValue());
        Integer cardId = Integer.parseInt(json.findPath("cardID").textValue());

        Collection collection = Collection.find.ref(collectionId);
        Card newcard = Card.find.byId(cardId.toString());

        if(collection.containsCard(newcard)) {
            String response = new String("repeated");
            return ok(Json.toJson(response));
        }

        collection.addCard(newcard);
        collection.save();
        collection.saveManyToManyAssociations("cards");
        String response = new String("ok");
        return ok(Json.toJson(response));
    }

    public static Result getUserCollections(Integer userId) {
        List<Collection> collections = Collection.findUserCollections(userId);
        return ok(); //FIXME
    }


    public static JsonNode getUserCollections(){
        List<Collection> collections = Collection.findUserCollections(Integer.parseInt(session().get("id")));
        return Json.toJson(Lists.reverse(collections));
    }
    public static JsonNode getUserDecks(){
        List<Collection> collections = Collection.findUserDecks(Integer.parseInt(session().get("id")));
        return Json.toJson(Lists.reverse(collections));
    }
    public static JsonNode getUserTradeLists(){
        List<Collection> collections = Collection.findUserTradeLists(Integer.parseInt(session().get("id")));
        return Json.toJson(Lists.reverse(collections));
    }
    public static JsonNode getUserWantLists(){
        List<Collection> collections = Collection.findUserWantLists(Integer.parseInt(session().get("id")));
        return Json.toJson(Lists.reverse(collections));
    }

    public static Result editCollection1(Integer id){

        //TODO - Páginas de erro / forbidden
        Collection col = Collection.find.byId(id);
        if(col == null)
            return ok("COLECÇÃO NÃO EXISTENTE");

        if(Integer.parseInt(session().get("id")) != col.owner.id)
            return ok("COLECÇÃO NÃO PERTENCENTE A ESTE UTILIZADOR");

        String type;
        if(col.coltype.equals("co"))
            type = "Collection";
        else if(col.coltype.equals("de"))
            type = "Deck";
        else if(col.coltype.equals("tl"))
            type = "TradeList";
        else
            type = "WantList";

        return ok(views.html.editCollection.render(Collection.findCollectionByID(id),Json.toJson(Collection.findCollectionCards(id)), type));
    }

    public static Result getCollectionCards() {
        JsonNode json = request().body().asJson();

        Integer collectionId = Integer.parseInt(json.findPath("colID").textValue());
        Collection col = Collection.find.byId(collectionId);

        return ok(Json.toJson(col.cards));
    }


    public static Result decks() {
        return ok(decks.render(Collections.getUserDecks()));
    }
    //Coming Soon
    public static Result wantlists() {
        return ok(wantlists.render(Collections.getUserWantLists()));
    }
    public static Result tradelists() {
        return ok(tradelists.render(Collections.getUserTradeLists()));
    }

}