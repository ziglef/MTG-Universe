package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;

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
    @Column(name = "password")
    public String password; // to be changed just for testing
}
