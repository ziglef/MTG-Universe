import models.*;
import models.enums.Visibility;
import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import play.test.WithApplication;

import java.util.List;

public class CollectionTest extends WithApplication {

    @Test //createCollection collection
    public void test1() {
        User u1 = new User("bob", "bobuser", "bob@email", "12345");
        u1.setCity("Porto");
        u1.save();

        Collection.createCollection("CollectionTest", u1.id, Visibility.PUBLIC_, "co");
        List<Collection> collections = Collection.findUserCollections(u1.id);
        assertNotNull(collections);
    }

    @Test //add cards to collection
    public void test2() {
        User u1 = new User("bob", "bobuser", "bob@email", "12345");
        u1.setCity("Porto");
        u1.save();

        Collection collection = Collection.createCollection("CollectionTest", u1.id, Visibility.PUBLIC_, "co");

        Card c1 = Card.findCardsByName("").get(1);
        Card c2 = Card.findCardsByName("").get(2);

        collection.addCard(c1);
        collection.addCard(c2);
        collection.save();

        assertEquals(2, Collection.find.byId(collection.id).cards.size());
    }

    @Test //remove cards from collection
    public void test3() {
        User u1 = new User("bob", "bobuser", "bob@email", "12345");
        u1.setCity("Porto");
        u1.save();

        Collection collection = Collection.createCollection("CollectionTest", u1.id, Visibility.PUBLIC_, "co");

        Card c1 = Card.findCardsByName("").get(1);
        Card c2 = Card.findCardsByName("").get(2);

        collection.addCard(c1);
        collection.addCard(c2);
        collection.save();

        collection.removeCard(c1.id);
        collection.save();

        assertEquals(1, Collection.find.byId(collection.id).cards.size());
    }


    @Test //delete collection
    public void test4() {
        User u1 = new User("bob", "bobuser", "bob@email", "12345");
        u1.setCity("Porto");
        u1.save();

        Collection collection = Collection.createCollection("CollectionTest", u1.id, Visibility.PUBLIC_, "co");

        collection.delete();

        assertEquals(0, Collection.findUserCollections(u1.id).size());
    }

}
