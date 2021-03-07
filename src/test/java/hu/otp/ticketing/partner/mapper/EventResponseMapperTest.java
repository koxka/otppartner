package hu.otp.ticketing.partner.mapper;

import hu.otp.ticketing.partner.dto.Event;
import hu.otp.ticketing.partner.dto.EventsResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventResponseMapperTest {
    public static final OffsetDateTime START_TIME_STAMP = OffsetDateTime.now().minusHours(1L);
    public static final OffsetDateTime END_TIME_STAMP = OffsetDateTime.now();
    public static final String TITLE = "title";
    public static final String LOC = "loc";
    public static final long EVENT_ID = 1L;
    private EventResponseMapper mapper = new EventResponseMapperImpl();

    @Test
    void map() {
        EventsResponse source = createSource();
        hu.otp.ticketing.partner.endpoint.rest.model.EventsResponse dest = mapper.map(source);

        assertTrue(dest.getInfo().getSuccess());
        assertEquals(1, dest.getData().size());
        assertEquals(BigDecimal.valueOf(EVENT_ID), dest.getData().get(0).getEventId());
        assertEquals(LOC, dest.getData().get(0).getLocation());
        assertEquals(TITLE, dest.getData().get(0).getTitle());
        assertEquals(END_TIME_STAMP, dest.getData().get(0).getEndTimeStamp());
        assertEquals(START_TIME_STAMP, dest.getData().get(0).getStartTimeStamp());
    }

    private EventsResponse createSource() {
        EventsResponse eventsResponse = new EventsResponse();
        eventsResponse.setSuccess(true);
        Event event = new Event();
        event.setEventId(EVENT_ID);
        event.setLocation(LOC);
        event.setTitle(TITLE);
        event.setEndTimeStamp(END_TIME_STAMP);
        event.setStartTimeStamp(START_TIME_STAMP);
        eventsResponse.setData(Collections.singletonList(event));
        return eventsResponse;
    }
}
