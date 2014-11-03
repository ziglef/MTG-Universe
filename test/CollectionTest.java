import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;

public class CollectionTest extends WithApplication {

   /* @Test
    public void test1() {
        User u = new User("bob", "bobuser", "bob@email", "12345");
        u.save();

        GenericCollection collection = GenericCollection.create("ColletionTest", u.id);
        collection.addCard(Card.findCardsByName("").get(1));
        collection.addCard(Card.findCardsByName("").get(2));

        User bob = User.find.where().eq("email", "bob@email").findUnique();
        assertNotNull(bob);
        assertEquals("bob", bob.name);

    }


    @Test
    public void test2() {
        User u2 = new User("bob", "bobuser", "bob@email", "12345");
        u2.save();

        GenericCollection collection = GenericCollection.create("CollectionTest", u2.id);
        collection.addCard(Card.findCardsByName("").get(1));
        collection.addCard(Card.findCardsByName("").get(2));

        //User bob = User.find.byId(1);
        //System.out.println("size: "+u2.collection);
        assertEquals(2, GenericCollection.find.byId(1).cards.size());

        //assertNotNull(u2.collection);
        assertEquals("bob", u2.name);

    }
    */

    @Test
    public void test2() {
        User u2 = new User("bob", "bobuser", "bob@email", "12345");
        u2.save();

        Collection collection = Collection.create("CollectionTest", u2.id);
        collection.addCard(Card.findCardsByName("").get(1));
        collection.addCard(Card.findCardsByName("").get(2));

        //User bob = User.find.byId(1);
        //System.out.println("size: "+u2.collection);
        assertEquals(2, Collection.find.byId(1).cards.size());

        //assertNotNull(u2.collection);
        //assertEquals("bob", u2.name);

    }
}
