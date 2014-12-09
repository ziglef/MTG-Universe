package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Entity
public class Article extends Model implements Serializable {

    public static Finder<Integer, Article> find = new Model.Finder<>(Integer.class, Article.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    public Integer id;

    public String title;

    public String text;

    public String imageUrl;

    //https://www.playframework.com/documentation/2.0/api/java/play/data/format/Formats.DateTime.html
    public String date;


    public Article(String title, String text, String imageUrl, String date) {
        this.title=title;
        this.text=text;
        this.imageUrl=imageUrl;
    }

    public void edit(String title, String text, String imageUrl) {
        this.title=title;
        this.text=text;
        this.imageUrl=imageUrl;
        this.save();
    }

    @ManyToOne
    public User writter;

    // find all articles of some user
    public static List<Article> findUserArticles(Integer userId) {
        return find.where().eq("writter.id", userId).findList();
    }

}
