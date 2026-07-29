package com.microservices.movie.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "movie")
@Getter
@Setter
public class MovieConfig {
    
    private SeatLock seatLock = new SeatLock();
    private Payment payment = new Payment();
    private Booking booking = new Booking();
    
    @Getter
    @Setter
    public static class SeatLock {
        private int durationMinutes = 10;
    }
    
    @Getter
    @Setter
    public static class Payment {
        private double mockSuccessRate = 0.9;
    }
    
    @Getter
    @Setter
    public static class Booking {
        private int cancellationWindowHours = 2;
    }
}
