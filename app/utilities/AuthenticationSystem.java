package utilities;

import static play.mvc.Controller.session;

public class AuthenticationSystem {

    // Chamar funcao para verificar se utilizador esta logado ou nao
    public static Boolean isLoggedIn() {
        if ( session().get("islogged") != null && session().get("islogged").equals("true") )
            return true;

        return false;
    }

}
