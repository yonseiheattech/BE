package org.heattech.heattech.domain.postbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostBoxRepository extends JpaRepository<PostBox, Long> {
    // 별도 메서드 없이 전체 리스트 조회 가능
}