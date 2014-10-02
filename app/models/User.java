package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

// The model for the average user

@Entity
public class User extends Model{

    @Id
    public int id;

    public String name;
    public String username;
    public String password; // to be changed just for testing

}
