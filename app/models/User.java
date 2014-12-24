package models;

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
    @Column(name = "city")
    public String city;

    @Basic(optional = false)
    @Column(name ="username")
    public String username;

    @Basic(optional = false)
    @Column(name ="email")
    public String email;

    @Basic(optional = false)
    @Column(name = "password")
    public String password;

    public String imageurl = "default-avatar.png";


    //Collection of cards
    //@OneToMany
    //public Collection collection;

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.imageurl = "default-avatar.png";
    }

    public void setCity(String city) {
        this.city=city;
    }

    @Override
    public void save() {
    	// Call super save() method
        super.save();
    }
    
    public void save(boolean save) {
    	// Hash password
        if ( save ) this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());

        this.save();
    }
    
    public static User authenticate(String username, String password) {
    	// Vai buscar utilizador
    	User logged = User.find.where().eq("username", username).findUnique();
    	    	
    	// User e passwords corretos
    	if ( logged != null && BCrypt.checkpw(password, logged.password) )
    		return logged;
    	
    	return null;
    }

    public static User findUserByUsername(String username){
        User user = null;
        user = find.where().eq("username", username).findUnique();
        return user;
    }

    public void setImageUrl() {
        this.imageurl =id+".png";
    }
}
