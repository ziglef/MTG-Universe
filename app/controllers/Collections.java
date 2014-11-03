package controllers;

import models.AbstractSetofCards;
import models.Card;
import models.Collection;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;


public class Collections extends Controller {

    public static Result addCollection() {
        Collection collection = Collection.create(
                "nome",
                1 //user id
        );
        return ok(); //FIXME
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


}