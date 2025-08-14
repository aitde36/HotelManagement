package com.AfsarDev.AfsarHotel.service.interfac;



import com.AfsarDev.AfsarHotel.dto.Response;
import com.AfsarDev.AfsarHotel.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}
