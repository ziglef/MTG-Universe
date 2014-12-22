package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import models.Article;
import models.User;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utilities.ImageConverter;
import views.html.articles;
import views.html.createarticle;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static play.libs.Json.toJson;


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
        JsonNode arraynode = Json.toJson(Lists.reverse(articles));

        for(JsonNode node : arraynode) {
            JsonNode nodeWriter = node.get("writer");
            ((ObjectNode) nodeWriter).remove("email");
            ((ObjectNode) nodeWriter).remove("name");
            ((ObjectNode) nodeWriter).remove("city");
            ((ObjectNode) nodeWriter).remove("password");
        }

        return Json.toJson(arraynode);
    }

    public static class ArticleFields {
        //@Constraints.Required
        public String articleTitle, atc;
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
            post =  articleForm.get().atc;
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

        Integer articleID = Integer.parseInt(json.findPath("artID").textValue());
        Article article = Article.find.byId(articleID);

        return ok(Json.toJson(article));
    }

    public static Result getArticleComments() {
        JsonNode json = request().body().asJson();
        Integer articleID = Integer.parseInt(json.findPath("artID").textValue());
        Article article = Article.find.byId(articleID);
        return ok(Json.toJson(article.comments));
    }

    public static Result addComment() {
        JsonNode json = request().body().asJson();
        Integer articleID = Integer.parseInt(json.findPath("artID").textValue());
        Integer userID = Integer.parseInt(json.findPath("writerID").textValue());
        String text = json.findPath("text").textValue();
        String date = json.findPath("date").textValue();

        Article article = Article.find.byId(articleID);
        User writer = User.find.byId(userID);
        article.addComment(writer, text, date);
        article.save();

        return ok();
    }

}