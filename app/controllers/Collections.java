package controllers;

import models.Card;
import models.GenericCollection;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;


public class Collections extends Controller {

    public static Result addCollection() {
        GenericCollection collection = GenericCollection.create(
                Form.form().bindFromRequest().get("name"),
                //request().username()
                session().get("name")
        );
        return ok(); //FIXME
    }

    public static Result addCardtoCollection(Integer collectionId, Integer cardId) {
        GenericCollection collection = GenericCollection.find.ref(collectionId);
        Card newcard = Card.find.byId(cardId.toString());
        collection.addCard(newcard);
        collection.save();
        collection.saveManyToManyAssociations("cards");
        return ok(); //FIXME
    }



}
