package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Ruling extends Model implements Serializable {
    public static Finder<String, Ruling> find = new Finder<>(String.class, Ruling.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String date;
    @Column(name = "text", columnDefinition = "TEXT(1523)")
    public String text;

    public Ruling(String date, String text) {
        this.date=date;
        this.text = text;
        //this.save();
    }
}
