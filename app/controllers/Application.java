package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Card;
import models.User;
import play.libs.Json;
import play.twirl.api.Html;
import utilities.AuthenticationSystem;
import utilities.PasswordGenerator;
import play.data.Form;
import play.data.validation.Constraints;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;
import java.util.*;
import static play.libs.Json.toJson;

import com.typesafe.plugin.*;

public class Application extends Controller {

    public static Result index() {
		if ( AuthenticationSystem.isLoggedIn() )
			return ok(dashboard.render("Dashboard", null));
		else
			return ok(index.render(Form.form(User.class), Form.form(Login.class)));
    }

    @Transactional
    public static Result addUser() {
        Form<User> regForm = Form.form(User.class).bindFromRequest();

        if(regForm.hasErrors()){
            return badRequest("/user");
        } else {
        	User newUser = regForm.get();   // Same as calling the constructor
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

            ArrayList<Card> results = Card.findCardsByName(cardName);
            if( results != null )
                return ok(toJson(results));
            else
                return notFound();
        }
    }

    public static Result createCollectionData(){
        return ok(createCollection.render());
    }
/*
    public static Result login() {
        if(!session("islogged").equalsIgnoreCase("true"))
            session("islogged", "false");
        return ok(login.render(Form.form(Login.class)));
    }
*/
    public static Result authenticate() {

        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if ( loginForm.hasErrors() ) {
            session("islogged", "false");
            return redirect(controllers.routes.Application.index());
        }
        else {
            User logged = User.authenticate(loginForm.get().username, loginForm.get().password);

            // Esta logado
            if ( logged != null )
            {
                session().clear();

                // Set session fields
                session("id", logged.id.toString());
                session("username", logged.username);
                session("name", logged.name);
                session("islogged", "true");

                return redirect(controllers.routes.Application.index());
            }
            else
            {
                session().clear();
                session("islogged", "false");
                loginForm.reject("login","invalid username/password");
                return redirect(controllers.routes.Application.index());
            }
        }
    }

    public static Result logout() {
        session("islogged", "false");
        session().clear();
        return redirect(controllers.routes.Application.index());
    }

    // Class de login
    public static class Login {
        @Constraints.Required
        public String username, password;
    }

    // Class de recover
    public static class Recover {
        //@Constraints.Required
        public String email;
    }
    
    public static Result recover() {
        Form<Recover> recoverForm = Form.form(Recover.class).bindFromRequest();

        if ( recoverForm.hasErrors() ) {
            return badRequest("/");
        }
        else {
            // Vai buscar utilizador
            User user = User.find.where().eq("email", recoverForm.get().email).findUnique();
            
            // Email existe
            if ( user != null )
            {
                String newPassword = PasswordGenerator.random(6);
                user.password = newPassword;
                user.save();
                
                // Send email (preciso configurar ainda no servidor)
                MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
                mail.setSubject("New password at magicgathering");
                mail.setRecipient(user.email);
                mail.setFrom("Password Recover <noreply@magicgathering.com>");

                // Send it
                //mail.send( "Your new password is: " + newPassword );
                
                // Temporario
                System.out.println("Nova password: " + newPassword);
            }
            else
            {
                //recoverForm.reject("bad","invalid email/username");
            }
            
            return redirect(controllers.routes.Application.index());
        }
    }

    public static Result sortCards(){

        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = Card.find.select("name").setDistinct(true).findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]);

        String sortBy = "name";
        String order = params.get("sSortDir_0")[0];

        /*
        switch(Integer.valueOf(params.get("iSortCol_0)[0]))){
        case 1: sortBy ="rowValue";

        }*/

        Page<Card> cardsPage = Card.find.where(
                Expr.ilike("name", "%" + filter + "%")
        )
        .orderBy(sortBy + " " + order + ", id " + order)
        .findPagingList(pageSize).setFetchAhead(false)
        .getPage(page);

        Integer iTotalDisplayRecords = cardsPage.getTotalRowCount();

        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode an = result.putArray("aaData");

        for(Card c: cardsPage.getList()){
            ObjectNode row = Json.newObject();
            row.put("0","<a href=\"#\" name=\""+c.name+"\" onclick=\"changeImg ( this.name ) ;\">"+c.name+"</a>");
            row.put("1","<button class=\"btn btn-sm btn-success btn-block\" name=\""+c.name+"\" onclick=\"addToCollection(this.name)\"> ADD </button>");
            an.add(row);
        }

        return ok(result);
    }

}
