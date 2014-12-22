package models;

import play.db.ebean.Model;

import javax.persistence.*;


@Entity
public class ArticleComment extends Model {

    public static Finder<Integer, ArticleComment> find = new Model.Finder<>(Integer.class, ArticleComment.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    public Integer id;

    @Column(columnDefinition = "TEXT(2000)")
    public String text;

    public String date;

    @ManyToOne
    public Article article;

    @ManyToOne
    public User commentWriter;

    public ArticleComment(Article article, User user, String text, String date) {
        this.article=article;
        this.text=text;
        this.commentWriter=user;
        this.date=date;
    }
}
