package com.ticketflow.eventservice.repo;

import com.ticketflow.eventservice.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Modifying
    @Transactional
    @Query(
        "update Event e set e.availableSeats=e.availableSeats-:q where e.id=:id and e.status='PUBLISHED' and e.availableSeats>=:q"
    )
    int reserve(@Param("id") Long id, @Param("q") int quantity);

    @Modifying
    @Transactional
    @Query(
        "update Event e set e.availableSeats=case when e.availableSeats + :q > e.capacity then e.capacity else e.availableSeats + :q end where e.id=:id"
    )
    int release(@Param("id") Long id, @Param("q") int quantity);
}
