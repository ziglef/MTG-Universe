package controllers;

import models.Card;
import models.User;
import utilities.PasswordGenerator;
import play.data.Form;
import play.data.validation.Constraints;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.*;
import java.util.ArrayList;
import static play.libs.Json.toJson;
import com.typesafe.plugin.*;

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

    public static Result login() {
        return ok(login.render(Form.form(Login.class)));
    }

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
                session("id", logged.id.toString());
                session("username", logged.username);
                session("name", logged.name);
                session("islogged", "true");

                return redirect(controllers.routes.Application.index());
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

}
