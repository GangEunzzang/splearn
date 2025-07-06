package tobyspring.splearn.application.provided;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.DuplicateEmailException;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.MemberStatus;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest (MemberRegister memberRegister, EntityManager em) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        em.flush();
        em.clear();

        Member activatedMember = memberRegister.activate(member.getId());

        em.flush();
        assertThat(activatedMember.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void memberRegisterRequestFail() {
        extracted(new MemberRegisterRequest("toby@splearn.app", "toby", "longsecret"));
        extracted(new MemberRegisterRequest("toby@splearn.app", "charle_______________________________", "longsecret"));
        extracted(new MemberRegisterRequest("tobysplearn.app", "chalksdj", "longsecret"));
    }

    private void extracted(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }

}