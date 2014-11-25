package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Printing extends Model implements Serializable {

    public static Model.Finder<String, Printing> find = new Model.Finder<>(String.class, Printing.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;

    public Printing(String name) {
        this.name = name;
        this.save();
    }

    public static Printing getPrinting(String name) {
        Printing p = find.where().eq("name",name).findUnique();
        return p == null ? new Printing(name) : p;
    }
}
