package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.twirl.api.Html;
import utilities.AuthenticationSystem;
import utilities.ImageConverter;
import utilities.PasswordGenerator;
import play.data.Form;
import play.data.validation.Constraints;
import play.db.ebean.Transactional;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.*;

import java.io.File;
import java.io.IOException;
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

        if( regForm.hasErrors() ) {
            return badRequest("Form with errors");
        } else {
        	User newUser = regForm.get();   // Same as calling the constructor
        	
            // Verifica se ja existe
            User findUsername = User.find.where().eq("username", newUser.username).findUnique();
            User findEmail = User.find.where().eq("email", newUser.email).findUnique();
            
            // Verifica erros
            if ( newUser.username.length() < 3 || newUser.email.length() < 5 || newUser.password.length() < 5 )
            {
            	return badRequest("Username/email/password invalid (length)");            	
            }
            if ( findUsername != null || findEmail != null )
            {
            	return badRequest("Username/email already exists");
            }
            else
            {
            	newUser.save();
	            return redirect(routes.Application.index());
            }
        }
    }
	
    // Class de login
    public static class Profile {
        //@Constraints.Required
        public String name, username, email, actualPassword, newPassword, newPassword2;
    }
    
    public static Result profile() {
    	return ok(profile.render(Form.form(Profile.class)));
    }
    
    public static Result editUser() {
        Form<Profile> editForm = Form.form(Profile.class).bindFromRequest();

        if( editForm.hasErrors() ) {
            return badRequest("Form with errors");
        } else {
            // Vai buscar utilizador
            User user = User.authenticate(session().get("username"), editForm.get().actualPassword);

            // Password atual correta
            if ( user != null )
            {
                // Verifica se novo utilizador ja existe
            	if ( !editForm.get().username.equals(session().get("username")) )
            	{
            		User findUsername = User.find.where().eq("username", editForm.get().username).findUnique();	
            		
                    if ( findUsername != null )
                    {
                    	return badRequest("New username already exists");
                    }
            	}
            	
            	// Verifica se novo email ja existe
            	if ( !editForm.get().email.equals(session().get("email")) )
            	{
            		User findEmail = User.find.where().eq("email", editForm.get().email).findUnique();	
            		
                    if ( findEmail != null )
                    {
                    	return badRequest("New email already exists");
                    }
            	}
            	
	            // Verifica nova password
	            if ( editForm.get().newPassword.length() >= 5 )
	            {
	            	if ( editForm.get().newPassword.equals(editForm.get().newPassword2) )
	            	{
	            		user.password = editForm.get().newPassword;
	            	}
	            	else
	            	{
	            		return badRequest("New passwords dont match");
	            	}
	            }
	            else
	            {
		            user.password = editForm.get().actualPassword;	
	            }
            	
            	// Salva avatar
            	MultipartFormData body = request().body().asMultipartFormData();
				FilePart avatar = body.getFile("avatar");
				
				if ( avatar != null ) 
				{
					try {
						ImageConverter.convertFormat(avatar.getFile(), "public/images/avatar/" + session().get("id") + ".png", "png");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
            	
	            // Altera campos
	            user.name = editForm.get().name;
	            user.username = editForm.get().username;
	            user.email = editForm.get().email;
	            	            
	            // Set sessions
	            session("username", user.username);
	            session("name", user.name);
	            session("email", user.email);
	            
	        	user.save();
	        	return redirect(routes.Application.profile());
            }
            else
            {
            	return badRequest("Actual password is wrong");
            }
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
        return ok(createCollection.render(Collections.getUserCollections()));
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

        if ( !loginForm.hasErrors() ) 
        {
            User logged = User.authenticate(loginForm.get().username, loginForm.get().password);

            // Esta logado
            if ( logged != null )
            {
                session().clear();

                // Set session fields
                session("id", logged.id.toString());
                session("username", logged.username);
                session("name", logged.name);
				session("email", logged.email);
                session("islogged", "true");

                return redirect(controllers.routes.Application.index());
            }
        }
        
        // Error logging
        session().clear();
        session("islogged", "false");
        return badRequest("Invalid username/password");
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
                
                // Send email (funciona so no servidor ldso02.fe.up.pt)
                MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
                mail.setSubject("New password at MTGUniverse");
                mail.setRecipient(user.email);
                mail.setFrom("noreply@ldso02.fe.up.pt");

                // Send it
                mail.send( "You requested a new password for your account: " + newPassword + "\n\nBest regards,\nMTGUniverse Team");
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
            row.put("0","<a href=\"#\" rel=\"popover\" data-img=\""+c.imageName+"\">"+c.name.replaceAll("\"","\\\"")+"</a>");
            row.put("1","<button class=\"btn btn-sm btn-success btn-block\" name=\""+c.name.replaceAll("\"","\\\"")+"\" onclick=\"addToCollection(this.name,"+c.id+")\"> Add </button>");
            an.add(row);
        }

        return ok(result);
    }

}
