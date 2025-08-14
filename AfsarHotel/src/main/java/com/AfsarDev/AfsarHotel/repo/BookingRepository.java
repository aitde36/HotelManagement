package com.AfsarDev.AfsarHotel.repo;

import java.util.List;
import java.util.Optional;

import com.AfsarDev.AfsarHotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	List<Booking> findByRoomId(Long roomId);

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);
    
    List<Booking> findByUserId(Long userId);
}
