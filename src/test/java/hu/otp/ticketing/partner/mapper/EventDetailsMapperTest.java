package hu.otp.ticketing.partner.mapper;

import hu.otp.ticketing.partner.dto.EventDetails;
import hu.otp.ticketing.partner.dto.EventDetailsData;
import hu.otp.ticketing.partner.dto.Seat;
import hu.otp.ticketing.partner.endpoint.rest.model.EventsDetailsResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static hu.otp.ticketing.partner.common.ErrorCode.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventDetailsMapperTest {
    private EventDetailsMapper mapper = new EventDetailsMapperImpl(new SeatMapperImpl());

    @Test
    void map() {
        EventDetails eventDetails = new EventDetails();
        EventDetailsData data = new EventDetailsData();
        eventDetails.setData(data);
        eventDetails.setSuccess(true);
        data.setSeats(Collections.singletonList(new Seat()));

        EventsDetailsResponse mapped = mapper.map(eventDetails);

        assertEquals(1, mapped.getSeats().size());
        assertEquals(SUCCESS.getCode(), mapped.getInfo().getState().getCode());
        assertEquals(SUCCESS.name(), mapped.getInfo().getState().getReason());
        assertTrue(mapped.getInfo().getSuccess());
    }
}
