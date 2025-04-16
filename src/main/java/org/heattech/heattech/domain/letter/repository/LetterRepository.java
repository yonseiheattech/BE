package org.heattech.heattech.domain.letter.repository;

import org.heattech.heattech.domain.letter.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository <Letter, Long> {
    Optional<Letter> findByCode(String code);
    List<Letter> findAllBySenderId(Long senderId);
}
