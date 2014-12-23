package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import models.Article;
import models.ArticleComment;
import models.Collection;
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
            ((ObjectNode) nodeWriter).remove("comments");
        }

        return Json.toJson(arraynode);
    }

    public static Result deleteArticle() {
        JsonNode json = request().body().asJson();

        Integer id;

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            id = Integer.parseInt(json.findPath("artID").textValue());
            if(id == null) {
                return badRequest("Missing parameter [id]");
            }
        }

        Article article = Article.find.ref(id);
        List<ArticleComment> comments = ArticleComment.getCommentsbyArticle(article);
        for(ArticleComment comment : comments)
            comment.delete();
        article.delete();

        String response = new String("ok");
        return ok(Json.toJson(response));
    }

    public static class ArticleFields {
        //@Constraints.Required
        public String articleTitle, atc;
        public Integer articleId;
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

        if ( articleImage != null ){
            try {
                ImageConverter.convertFormat(articleImage.getFile(), "public/images/articles/" + article.id + ".png", "png");
                article.setImageUrl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        article.save();
        return ok(articles.render(getUserArticles()));
    }

    public static Result addEditedArticleToDB() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart articleImage = body.getFile("articleImage");

        Integer id;
        String title, post;
        Form<ArticleFields> articleForm = Form.form(ArticleFields.class).bindFromRequest();

        if( articleForm.hasErrors() ) {
            return badRequest("Form with errors");
        } else {
            title =  articleForm.get().articleTitle;
            post =  articleForm.get().atc;
            id = articleForm.get().articleId;
        }

        Article article = Article.find.byId(id);
        article.setTitle(title);
        article.setPost(post);

        if ( articleImage != null ){
            try {
                ImageConverter.convertFormat(articleImage.getFile(), "public/images/articles/" + article.id + ".png", "png");
                article.setImageUrl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        article.save();
        return ok(articles.render(getUserArticles()));
    }

    public static Result getArticle() {
        JsonNode json = request().body().asJson();

        Integer articleID = Integer.parseInt(json.findPath("artID").textValue());
        Article article = Article.find.byId(articleID);

        JsonNode node = Json.toJson(article);
        JsonNode nodeWriter = node.get("writer");
        ((ObjectNode) nodeWriter).remove("email");
        ((ObjectNode) nodeWriter).remove("name");
        ((ObjectNode) nodeWriter).remove("city");
        ((ObjectNode) nodeWriter).remove("password");

        return ok(node);
    }

    public static Result getArticleComments() {
        JsonNode json = request().body().asJson();
        Integer articleID = Integer.parseInt(json.findPath("artID").textValue());
        Article article = Article.find.byId(articleID);
        JsonNode arraynode = Json.toJson(ArticleComment.getCommentsbyArticle(article));

        for(JsonNode node : arraynode) {
            ((ObjectNode) node).remove("article");
            JsonNode nodeWriter = node.get("commentWriter");
            ((ObjectNode) nodeWriter).remove("email");
            ((ObjectNode) nodeWriter).remove("name");
            ((ObjectNode) nodeWriter).remove("city");
            ((ObjectNode) nodeWriter).remove("password");
        }
        return ok(arraynode);
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

        return ok("{}");
    }

    public static Result editArticle(Integer id) {
        Article art = Article.find.byId(id);
        if(art == null)
            return ok("ARTIGO NÃO EXISTENTE");

        if(Integer.parseInt(session().get("id")) != art.writer.id)
            return ok("ARTIGO NÃO PERTENCENTE A ESTE UTILIZADOR");

        return ok(views.html.editarticle.render(art));
    }

}

// jackson JSON tutorial
//http://www.journaldev.com/2324/jackson-json-processing-api-in-java-example-tutorial