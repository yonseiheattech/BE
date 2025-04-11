package org.heattech.heattech.domain.member.repository;

import org.heattech.heattech.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username); //이렇게 선언만 해도 Spring Jpa가 다 알아서 해줌.
    //Optional<Member> => Member or null
    Optional<Member> findByKakaoId(Long kakaoId);

}
