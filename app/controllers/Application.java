package controllers;

import com.google.gson.JsonObject;
import models.User;
import play.data.Form;
import play.db.ebean.Transactional;
import play.mvc.*;

import utilities.JsonUtil;
import views.html.*;

import java.io.File;

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
            File allCards = new File("app/assets/json/AllCards.json");

            JsonObject result = JsonUtil.find(allCards, "name", cardName);

            if( result != null ) {
                System.out.println("Found it!");
                return ok();
            } else {
                System.out.println("Card not found!");
                return notFound();
            }
        }
    }

}
