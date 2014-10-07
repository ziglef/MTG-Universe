package controllers;

import models.User;
import play.data.Form;
import play.db.ebean.Transactional;
import play.mvc.*;

import views.html.*;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Form.form(User.class)));
    }

    @Transactional
    public static Result addUser() {
        Form<User> regForm = Form.form(User.class).bindFromRequest();

        if(regForm.hasErrors()){
            return badRequest("/registration");
        } else {
            User newUser = regForm.get();
            newUser.save();
            return redirect(routes.Application.index());
        }
    }

    public static Result getPersons() {
        return ok(toJson(User.find.all()));
    }

    public static Result searchCard(){



        return ok();
        // return seeOther(" link to image ");
    }

}
