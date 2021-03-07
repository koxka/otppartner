package hu.otp.ticketing.partner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventDetailsData {
    private BigDecimal eventId;
    private List<Seat> seats;
}
