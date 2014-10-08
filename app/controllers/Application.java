package controllers;

import models.Card;
import models.User;
import play.data.Form;
import play.db.ebean.Transactional;
import play.mvc.*;

import views.html.*;

import java.util.ArrayList;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Form.form(User.class)));
    }

    @Transactional
    public static Result addUser() {
        Form<User> regForm = Form.form(User.class).bindFromRequest();

        if(regForm.hasErrors()){
            return badRequest("/user");
        } else {
            User newUser = regForm.get();
            newUser.save();
            return redirect(routes.Application.index());
        }
    }

    public static Result getPersons() {
        return ok(toJson(User.find.all()));
    }

    public static Result searchCard() {
        return ok(searchSimple.render(Form.form(String.class)));
    }

    public static Result checkCard(){

        Form<String> cardSearchForm = Form.form(String.class).bindFromRequest();

        if(cardSearchForm.hasErrors()){
            return badRequest("/checkcard");
        } else {
            String cardName = cardSearchForm.data().get("cardName");

            System.out.println("Ammount of cards in the DB: " + Card.find.all().size());

            return ok(toJson(Card.find.all()));
            /*
            ArrayList<Card> cards = Card.findCardsByName(cardName);

            if( cards != null ) {
                System.out.println("Found it!");
                return ok();
            } else {
                System.out.println("Card not found!");
                return notFound();
            }
            */
        }
    }

}
