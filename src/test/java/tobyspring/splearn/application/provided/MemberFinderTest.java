package tobyspring.splearn.application.provided;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager em) {

    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        em.flush();
        em.clear();

        Member found = memberFinder.find(member.getId());

        assertThat(found.getId()).isEqualTo(member.getId());
    }

    @Test
    void findFail() {
        Assertions.assertThatThrownBy(() -> memberFinder.find(999L))
            .isInstanceOf(IllegalArgumentException.class);
    }


}