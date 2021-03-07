package hu.otp.ticketing.partner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventsResponse {
    private List<Event> data;
    private boolean success;
}
