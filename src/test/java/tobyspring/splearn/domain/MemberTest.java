package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };

        member = Member.create("toby@splearn.app", "Toby", "secret", passwordEncoder);
    }

    @Test
    void createMember() throws Exception {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }


    @Test
    void constructNullCheck() throws Exception {
        assertThatThrownBy(() -> Member.create(null, "Toby", "secret", null))
                .isInstanceOf(NullPointerException.class);
    }


    @Test
    void activate() throws Exception {

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }


    @Test
    void activateFail() throws Exception {

        member.activate();

        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);

    }


    @Test
    void deactivate() throws Exception {

        member.activate();

        member.deActivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);

    }


    @Test
    void deactivateFail() throws Exception {

        //given
        assertThatThrownBy(member::deActivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deActivate();

        assertThatThrownBy(member::deActivate)
                .isInstanceOf(IllegalStateException.class);
    }


    @Test
    void verifyPassword() throws Exception {
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("fail", passwordEncoder)).isFalse();
    }


    @Test
    void changeNickname() throws Exception {
        assertThat(member.getNickname()).isEqualTo("Toby");

        member.changeNickname("change");

        assertThat(member.getNickname()).isEqualTo("change");
    }


    @Test
    void changePassword() throws Exception {
        member.changePassword("changePassword", passwordEncoder);

        assertThat(member.verifyPassword("changePassword", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deActivate();

        assertThat(member.isActive()).isFalse();
    }


}
