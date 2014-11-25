package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Legality extends Model implements Serializable {

    public static Finder<String, Legality> find = new Finder<>(String.class, Legality.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String format;
    public String legality;

    public Legality(String format, String legality) {
        this.format=format;
        this.legality = legality;
        //this.save();
    }

    /*
    public static ForeignName getForeignName(String lang, String nm) {
        ForeignName c = find.where().eq("language",lang).findUnique();
        return c == null ? new ForeignName(tname) : c;
    }
    */
}
