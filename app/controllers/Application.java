package controllers;

import models.User;
import play.*;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Transactional;
import play.mvc.*;

import views.html.*;

import java.util.List;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Form.form(User.class)));
    }

    @Transactional
    public static Result addUser() {
        Form<User> regForm = Form.form(User.class).bindFromRequest();

        System.out.println("Form: " + regForm.toString());

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

}
