package com.nighthawk.spring_portfolio.mvc.frames;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameJpaRepository extends JpaRepository<Frame, Long> {
    Optional<Frame> findByfileName(String fileName);

    void deleteByfileName(String fileName);
}
