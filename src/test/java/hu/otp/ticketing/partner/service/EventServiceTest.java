package hu.otp.ticketing.partner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.otp.ticketing.partner.dto.EventDetails;
import hu.otp.ticketing.partner.dto.EventDetailsData;
import hu.otp.ticketing.partner.endpoint.rest.model.*;
import hu.otp.ticketing.partner.mapper.EventDetailsMapper;
import hu.otp.ticketing.partner.mapper.EventResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;

import static hu.otp.ticketing.partner.common.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    public static final String SEAT_ID = "id";
    public static final BigDecimal EVENT_ID = BigDecimal.ONE;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private EventResponseMapper eventResponseMapper;
    @Mock
    private EventDetailsMapper eventDetailsMapper;

    @InjectMocks
    private EventService eventService;

    @Captor
    private ArgumentCaptor<hu.otp.ticketing.partner.dto.EventsResponse> eventsCaptor;


    @Test
    void getEvents_happy() throws IOException {
        when(objectMapper.readValue(any(URL.class), eq(hu.otp.ticketing.partner.dto.EventsResponse.class)))
                .thenReturn(new hu.otp.ticketing.partner.dto.EventsResponse());
        EventsResponse mappedRp = new EventsResponse();
        Info info = new Info();
        info.setSuccess(true);
        mappedRp.setInfo(info);
        mappedRp.setData(Collections.singletonList(new Event()));
        when(eventResponseMapper.map(any())).thenReturn(mappedRp);

        EventsResponse events = eventService.getEvents();

        assertEquals(1, events.getData().size());
        assertTrue(events.getInfo().getSuccess());
    }

    @Test
    void getEvents_sad() throws IOException {
        doThrow(new IOException(""))
                .when(objectMapper).readValue(any(URL.class), eq(hu.otp.ticketing.partner.dto.EventsResponse.class));

        eventService.getEvents();

        verify(eventResponseMapper).map(eventsCaptor.capture());
        hu.otp.ticketing.partner.dto.EventsResponse captorValue = eventsCaptor.getValue();

        assertNull(captorValue.getData());
        assertFalse(captorValue.isSuccess());
    }

    @Test
    void getEvent_happy() throws IOException {
        when(objectMapper.readValue(any(URL.class), eq(EventDetails.class))).thenReturn(new EventDetails());
        EventsDetailsResponse mappedRp = new EventsDetailsResponse();
        Info info = new Info();
        info.setSuccess(true);
        mappedRp.setInfo(info);
        mappedRp.setSeats(Collections.singletonList(new Seat()));
        when(eventDetailsMapper.map(any())).thenReturn(mappedRp);

        EventsDetailsResponse rp = eventService.getEvent(EVENT_ID);

        assertEquals(1, rp.getSeats().size());
        assertTrue(rp.getInfo().getSuccess());
    }

    @Test
    void getEvent_sad() throws IOException {
        doThrow(new IOException("")).when(objectMapper).readValue(any(URL.class), eq(EventDetails.class));

        EventsDetailsResponse event = eventService.getEvent(EVENT_ID);

        assertEquals(NO_SUCH_FILE.getCode(), event.getInfo().getState().getCode());
        assertFalse(event.getInfo().getSuccess());
        assertNull(event.getSeats());
    }

    @Test
    void reserve_sad_noEvent() throws IOException {
        doThrow(new IOException("")).when(objectMapper).readValue(any(URL.class), eq(EventDetails.class));
        ReservationRequest rq = new ReservationRequest();
        rq.setEventId(EVENT_ID);
        rq.setSeatId("id");

        ReservationResponse rp = eventService.reserve(rq);

        assertEquals(NO_SUCH_FILE.getCode(), rp.getInfo().getState().getCode());
        assertFalse(rp.getInfo().getSuccess());
        assertNull(rp.getReservationId());
    }

    @Test
    void reserve_sad_noSeat() throws IOException {
        EventDetails details = new EventDetails();
        details.setData(new EventDetailsData());
        when(objectMapper.readValue(any(URL.class), eq(EventDetails.class))).thenReturn(details);
        ReservationRequest rq = new ReservationRequest();
        rq.setEventId(EVENT_ID);
        rq.setSeatId(SEAT_ID);

        ReservationResponse rp = eventService.reserve(rq);

        assertEquals(NO_SUCH_SEAT.getCode(), rp.getInfo().getState().getCode());
        assertFalse(rp.getInfo().getSuccess());
        assertNull(rp.getReservationId());
    }

    @Test
    void reserve_sad_noSeat2() throws IOException {
        EventDetails ed = new EventDetails();
        EventDetailsData data = new EventDetailsData();
        ed.setData(data);
        hu.otp.ticketing.partner.dto.Seat seat = new hu.otp.ticketing.partner.dto.Seat();
        seat.setId(SEAT_ID);
        seat.setReserved(true);
        data.setSeats(Collections.singletonList(seat));
        when(objectMapper.readValue(any(URL.class), eq(EventDetails.class))).thenReturn(ed);
        ReservationRequest rq = new ReservationRequest();
        rq.setEventId(EVENT_ID);
        rq.setSeatId(SEAT_ID);

        ReservationResponse rp = eventService.reserve(rq);

        assertEquals(SEAT_ALREADY_TAKEN.getCode(), rp.getInfo().getState().getCode());
        assertFalse(rp.getInfo().getSuccess());
        assertNull(rp.getReservationId());
    }

    @Test
    void reserve_happy() throws IOException {
        EventDetails ed = new EventDetails();
        EventDetailsData data = new EventDetailsData();
        ed.setData(data);
        hu.otp.ticketing.partner.dto.Seat seat = new hu.otp.ticketing.partner.dto.Seat();
        seat.setId(SEAT_ID);
        seat.setReserved(false);
        data.setSeats(Collections.singletonList(seat));
        when(objectMapper.readValue(any(URL.class), eq(EventDetails.class))).thenReturn(ed);
        ReservationRequest rq = new ReservationRequest();
        rq.setEventId(EVENT_ID);
        rq.setSeatId(SEAT_ID);

        ReservationResponse rp = eventService.reserve(rq);

        assertNotNull(rp.getReservationId());
        assertTrue(rp.getInfo().getSuccess());
        assertEquals(SUCCESS.getCode(), rp.getInfo().getState().getCode());
    }
}
