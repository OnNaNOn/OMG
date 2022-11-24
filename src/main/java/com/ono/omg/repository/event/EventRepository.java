package com.ono.omg.repository.event;

import com.ono.omg.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository  extends JpaRepository<Event, Long> {
}
