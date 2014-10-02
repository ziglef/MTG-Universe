package controllers;

import models.User;
import play.*;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.*;

import views.html.*;

import java.util.List;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result addUser() {

        User user = Form.form(User.class).bindFromRequest().get();
        user.save();

        return redirect(routes.Application.index());
    }

    public static Result getPersons() {
        List<User> users = new Model.Finder(int.class, User.class).all();

        return ok(toJson(users));
    }

}
