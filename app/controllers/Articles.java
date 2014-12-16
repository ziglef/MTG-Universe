package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import models.Article;
import models.Collection;
import models.User;
import play.api.mvc.MultipartFormData;
import play.api.mvc.MultipartFormData.FilePart;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utilities.ImageConverter;
import views.html.articles;
import views.html.createCollection;
import views.html.createarticle;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static play.libs.Json.toJson;


/**
 * Created by Tiago on 02/12/2014.
 */
public class Articles extends Controller {


    private static Object userArticles;

    public static Result articles() {
        return ok(articles.render(getUserArticles()));
    }

    public static Result createArticle() {
        return ok(createarticle.render());
    }

    public static Result viewArticles() {

        return ok(toJson(Article.find.all()));
    }

    public static JsonNode getUserArticles() {
        List<Article> articles = Article.findUserArticles(Integer.parseInt(session().get("id")));
        return Json.toJson(Lists.reverse(articles));
    }

    public static class ArticleFields {
        //@Constraints.Required
        public String articleTitle, articlePost;
    }

    public static Result addArticleToDB() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart articleImage = body.getFile("articleImage");

        String title, post;
        Form<ArticleFields> articleForm = Form.form(ArticleFields.class).bindFromRequest();

        if( articleForm.hasErrors() ) {
            return badRequest("Form with errors");
        } else {
            title =  articleForm.get().articleTitle;
            post =  articleForm.get().articlePost;
        }

        Article article = new Article(title, post, dateFormat.format(cal.getTime()), session().get("username").toString());
        article.save();

        if ( articleImage != null ){
            try {
                ImageConverter.convertFormat(articleImage.getFile(), "public/images/articles/" + article.id + ".png", "png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ok(articles.render(getUserArticles()));
    }

    public static Result getArticle() {
        JsonNode json = request().body().asJson();

        Integer articleID = Integer.parseInt(json.findPath("colID").textValue());
        Article article = Article.find.byId(articleID);

        return ok(Json.toJson(article));
    }

}
