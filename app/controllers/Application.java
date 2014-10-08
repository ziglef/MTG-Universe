package controllers;

import models.Card;
import models.User;
import play.data.Form;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;
import java.util.ArrayList;
import static play.libs.Json.toJson;
import play.data.validation.Constraints.Required;

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
        	User newUser = new User(regForm.get().getName(), regForm.get().getUsername(), regForm.get().getPassword());
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

            System.out.println("Ammount of cards in the DB: " + Card.find.all().size());

            return ok(toJson(Card.find.all()));
            /*
            ArrayList<Card> cards = Card.findCardsByName(cardName);

            if( cards != null ) {
                System.out.println("Found it!");
                return ok();
            } else {
                System.out.println("Card not found!");
                return notFound();
            }
            */
        }
    }
    
    // Class de login
    public static class Login {
    	@Required
        public String username, password;
    }
    
    public static Result login() {
    	return ok(login.render(Form.form(Login.class)));
    }
    
    // Verifica formulario do login
    public static Result authenticate() {
    	
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        
        if ( loginForm.hasErrors() ) {
            return badRequest(login.render(loginForm));
        } 
        else {        	
        	User logged = User.authenticate(loginForm.get().username, loginForm.get().password);
        	
        	// Esta logado
        	if ( logged != null )
        	{
                session().clear();
                
                // Set session fields
                session("id", logged.getId().toString());
                session("username", logged.getUsername());
                session("name", logged.getName());
                session("islogged", "true");
                
                return redirect(routes.Application.index());	
        	}
        	else
        	{
        		loginForm.reject("login","invalid username/password");
        		return badRequest(login.render(loginForm));
        	}
        }
    }
   
    public static Result logout() {
    	session("islogged", "false");
        session().clear();
        return redirect(routes.Application.index());
    }
    
    // Chamar funcao para verificar se utilizador esta logado ou nao
    public static Boolean isLoggedIn()
    {
    	if ( session().get("islogged") != null && session().get("islogged").equals("true") )
    		return true;
    	
    	return false;
    }

}
