import models.Article;
import models.ArticleComment;
import models.User;
import org.junit.Test;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;


public class ArticlesTest extends WithApplication {

    @Test //add article
    public void test1() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.setCity("Porto");
        u1.save();

        Article article = new Article("Título do Artigo", "texto de exemplo", "15-12-2014", "bobuser1");
        article.save();

        assertEquals(1, Article.findUserArticles(u1.id).size());
    }

    @Test //get article
    public void test2() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.setCity("Porto");
        u1.save();

        Article article = new Article("Título do Artigo", "texto de exemplo", "15-12-2014", "bobuser1");
        article.save();

        assertEquals(1, Article.findUserArticles(u1.id).size());

        Article a2 = Article.find.byId(article.id);
        assertEquals("Título do Artigo", a2.title);
    }

    @Test //add comments to an article
    public void test3() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.setCity("Porto");
        u1.save();

        Article article = new Article("Título do Artigo", "texto de exemplo", "15-12-2014", "bobuser1");
        article.save();

        article.addComment(u1, "exemplo de comentário", "10-12-2014");
        article.addComment(u1, "exemplo de comentário 2", "10-12-2014");

        assertEquals(2, ArticleComment.getCommentsbyArticle(article).size());
    }

    @Test //set articles's image
    public void test4() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.setCity("Porto");
        u1.save();

        Article article = new Article("Título do Artigo", "texto de exemplo", "15-12-2014", "bobuser1");
        article.save();
        assertEquals("default.png", article.imageUrl);

        article.setImageUrl();
        article.save();
        assertEquals(article.id + ".png", article.imageUrl);
    }

}
