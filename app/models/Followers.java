package models;

import play.db.ebean.Model;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

// The model for the average user

@Entity
@Table(name = "Followers")
public class Followers extends Model implements Serializable{

    public static Finder<Integer, Followers> find = new Model.Finder<>(Integer.class, Followers.class);
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    public Integer id;

	@ManyToOne
	public User from;
	
	@ManyToOne
	public User to;

    public Followers(User from, User to) {
        this.from = from;
        this.to = to;
    }
    
    // Check connection
    public static Followers findConnection(String from, String to) {
    	
    	// Set read
    	Followers conn = find.where().and(
			com.avaje.ebean.Expr.eq("from.username", from),
			com.avaje.ebean.Expr.eq("to.username", to)
		).findUnique();
    	
    	return conn;
    }
    
    // Get followers
    public static List<Followers> getFollowers(String username) {
    	
    	// Set read
    	List<Followers> list = find.where().eq("to.username", username).findList();
    	
    	return list;
    }
    
    // Get following
    public static List<Followers> getFollowing(String username) {
    	
    	// Set read
    	List<Followers> list = find.where().eq("from.username", username).findList();
    	
    	return list;
    }


}
