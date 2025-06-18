package com.senkiv.carsharing.repository;

import com.senkiv.carsharing.model.Rental;
import com.senkiv.carsharing.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    Page<Rental> findByUserAndActualReturnDateIsNull(Pageable pageable, User user);

    Page<Rental> findByUserAndActualReturnDateIsNotNull(Pageable pageable, User user);

}
