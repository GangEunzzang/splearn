package tobyspring.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void equality() throws Exception {
        var email1 = new Email("toby@splearn.app");
        var email2 = new Email("toby@splearn.app");

        assertThat(email1).isEqualTo(email2);
    }

}
