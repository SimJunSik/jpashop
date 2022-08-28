package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class) // JUnit + Spring 같이 실행하고 싶을 때 필수
@SpringBootTest // Spring 컨테이너 안에서 테스트 하고 싶을 때 -> 없으면 Autowired 등 사용불가
@Transactional // Test 에서는 Rollback = True 가 기본 -> commit X -> insert query X
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    // @Rollback(value = false) // commit 호출 방법 - 1
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("sim");

        // when
        Long savedId = memberService.join(member);

        // then
        // em.flush(); // commit 호출 방법 - 2
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("sim");

        Member member2 = new Member();
        member2.setName("sim");

        // when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 함

        // then
        Assert.fail("예외가 발생해야 한다."); // 이 코드로 오면 안될 때 사용
    }
}