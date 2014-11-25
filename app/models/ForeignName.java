package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ForeignName extends Model implements Serializable {

    public static Model.Finder<String, ForeignName> find = new Model.Finder<>(String.class, ForeignName.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String language;
    public String name;


    public ForeignName(String lang, String nm) {
        this.language=lang;
        this.name = nm;
        //this.save();
    }

/*
    public static ForeignName getForeignName(String lang, String nm) {
        ForeignName c = find.where().eq("language",lang).findUnique();
        return c == null ? new ForeignName(tname) : c;
    }
*/
}
