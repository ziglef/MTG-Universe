import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import play.test.WithApplication;

import java.util.List;

public class CollectionTest extends WithApplication {

    @Test //add users
    public void test1() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.save();
        User u2 = new User("bob2", "bobuser2", "bob2@email", "54321");
        u2.save();

        assertEquals(2, User.find.all().size());

    }

    @Test //search user
    public void test2() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.save();

        User u2 = User.findUserByUsername("bobuser1");
        assertNotNull(u2);
        assertEquals("bob1@email", u2.email);
    }

    @Test //add collection
    public void test3() {
        User u1 = new User("bob", "bobuser", "bob@email", "12345");
        u1.save();

        Collection collection = Collection.create("CollectionTest", u1.id);
        List<Collection> collections = Collection.findUserCollections(u1.id);
        assertNotNull(collections);

        collection.addCard(Card.findCardsByName("").get(1));
        collection.addCard(Card.findCardsByName("").get(2));

        assertEquals(2, Collection.find.byId(collection.id).cards.size());
    }
}
