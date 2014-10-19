package models;

import com.avaje.ebean.event.BeanPersistController;
import play.db.ebean.Model;
import javax.persistence.*;
import java.io.Serializable;
import org.mindrot.jbcrypt.BCrypt;

// The model for the average user

@Entity
@Table(name = "Users")
public class User extends Model implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    public Integer id;

    // Finds all users in the database
    public static Finder<Integer, User> find = new Model.Finder<>(Integer.class, User.class);

    @Basic(optional = false)
    @Column(name = "name")
    public String name;

    @Basic(optional = false)
    @Column(name ="username")
    public String username;

    @Basic(optional = false)
    @Column(name ="email")
    public String email;

    @Basic(optional = false)
    @Column(name = "password")
    public String password; // to be changed just for testing


    //Collection of cards
    @OneToOne
    public GenericCollection collection;


    @Override
    public void save() {
        // Hash password
        this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
    
        // a verificacao se ja existe vai ser implementado fora?
        //User findUser = User.find.where().eq("username", this.username).findUnique();
        
        //if ( findUser == null )
        //{
            super.save();
        //}
    }
    
    public static User authenticate(String username, String password) {
    	// Vai buscar utilizador
    	User logged = User.find.where().eq("username", username).findUnique();
    	    	
    	// User e passwords corretos
    	if ( logged != null && BCrypt.checkpw(password, logged.password) )
    		return logged;
    	
    	return null;
    }
}
