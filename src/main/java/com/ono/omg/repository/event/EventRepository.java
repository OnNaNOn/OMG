package com.ono.omg.repository.event;

import com.ono.omg.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository  extends JpaRepository<Event, Long> {
    @Query("select e from Event e where e.soldType = 'Y'")
    List<Event> findBySoldTypeIsTrue();

    @Query("select e from Event e where e.soldType = 'N'")
    List<Event> findBySoldTypeIsFalse();

    Optional<Event> findByProductId(Long productId);

}
