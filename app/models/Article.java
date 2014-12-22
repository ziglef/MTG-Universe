package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Column(columnDefinition = "TEXT(20000)")
    public String text;

    public String imageUrl;

    //https://www.playframework.com/documentation/2.0/api/java/play/data/format/Formats.DateTime.html
    public String date;


    public Article(String title, String text, String date, String username) {
        this.title=title;
        this.text=text;
        this.date = date;
        this.writer = User.find.where().eq("username", username).findUnique();
    }

    public void edit(String title, String text, String imageUrl) {
        this.title=title;
        this.text=text;
        this.imageUrl=imageUrl;
        this.save();
    }

    @ManyToOne
    public User writer;

    @OneToMany
    public List<ArticleComment> comments = new ArrayList<>();

    // find all articles of some user
    public static List<Article> findUserArticles(Integer userId) {
        return find.where().eq("writer.id", userId).findList();
    }

    public void addComment(User user, String text, String date) {
        ArticleComment comment = new ArticleComment(this, user, text, date);
        comment.save();
        comments.add(comment);
    }

}
