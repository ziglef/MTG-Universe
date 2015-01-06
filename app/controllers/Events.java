package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.comingsoon;


public class Events extends Controller {

    public static Result events() {
        return ok(comingsoon.render("Events"));
    }
}
