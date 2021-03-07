package hu.otp.ticketing.partner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Event {
    private Long eventId;
    private String title;
    private String location;
    private OffsetDateTime startTimeStamp;
    private OffsetDateTime endTimeStamp;
}
