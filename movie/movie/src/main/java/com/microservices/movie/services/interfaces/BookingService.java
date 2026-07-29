package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    Booking createBooking(UUID showId, UUID userId, UUID lockId, String email, String phone);

    Booking confirmBooking(UUID bookingId, UUID paymentId);

    Booking cancelBooking(UUID bookingId, String cancellationReason);

    Booking getBooking(UUID bookingId);

    List<Booking> listUserBookings(UUID userId);
}
