import models.User;
import org.junit.Test;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class UserTests extends WithApplication {

    @Test //add users
    public void test1() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.setCity("Porto");
        u1.save();
        User u2 = new User("bob2", "bobuser2", "bob2@email", "54321");
        u2.setCity("Porto");
        u2.save();

        assertEquals(3, User.find.all().size()); //+1 because of automatic admin
    }

    @Test //search user
    public void test2() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.setCity("Porto");
        u1.save();

        User u2 = User.findUserByUsername("bobuser1");
        assertNotNull(u2);
        assertEquals("bob1", u2.name);
        assertEquals("bob1@email", u2.email);
        assertEquals("bobuser1", u2.username);
    }

    @Test //set user avatar
    public void test3() {
        User u1 = new User("bob1", "bobuser1", "bob1@email", "12345");
        u1.setCity("Porto");
        u1.save();

        assertEquals("default-avatar.png", u1.imageurl);

        User u2 = User.findUserByUsername("bobuser1");
        assertNotNull(u2);

        u2.setImageUrl();
        assertEquals(u2.id+".png", u2.imageurl);
    }

}
