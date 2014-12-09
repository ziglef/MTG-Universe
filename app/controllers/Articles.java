package controllers;

import models.Article;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.articles;
import views.html.createarticle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tiago on 02/12/2014.
 */
public class Articles extends Controller {


    public static Result articles() {
        return ok(articles.render());
    }

    public static Result createArticle() {
        return ok(createarticle.render());
    }

    public static Result addArticleToDB(/*String title, String text, String imageUrl*/) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();

        //Article article = new Article(title, text, imageUrl, dateFormat.format(cal.getTime()));
        //article.save();
        return ok();
    }

}
