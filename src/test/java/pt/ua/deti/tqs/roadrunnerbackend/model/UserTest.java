package pt.ua.deti.tqs.roadrunnerbackend.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class UserTest {

    @Test
    @DisplayName("Check if the password is correct")
    void checkPassword() {
        User user = new User();
        user.setPassword("password");
        assertThat(user.checkPassword("password"), is(true));
    }

    @Test
    @DisplayName("Check if the password is incorrect")
    void checkPassword2() {
        User user = new User();
        user.setPassword("password");
        assertThat(user.checkPassword("password2"), is(false));
    }

}