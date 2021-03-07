package hu.otp.ticketing.partner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventDetails {
    private EventDetailsData data;
    private boolean success;
}
