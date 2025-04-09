package org.heattech.heattech.domain.member.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
    //id
    //username
    //password

    @Id //primary 키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)//Enumtype 스트링으로 설정
    private Role role;

    //기본 생성자 (JPA 내부적으로 필요)
    public Member() {}

    //전체 필드를 받는 생성자
    public Member(Long id, String username, String password,Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    //id 빼고 받는 생성자 (회원가입할 때 사용)
    public Member(String username, String password,Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    //tostring
}


