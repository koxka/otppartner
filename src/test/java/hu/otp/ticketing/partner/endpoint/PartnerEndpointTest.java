package hu.otp.ticketing.partner.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.otp.ticketing.partner.config.GeneralConfig;
import hu.otp.ticketing.partner.endpoint.rest.model.*;
import hu.otp.ticketing.partner.endponit.PartnerEndpoint;
import hu.otp.ticketing.partner.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static hu.otp.ticketing.partner.config.SecurityConfig.SERVICE_USER;
import static hu.otp.ticketing.partner.security.HeaderValidatorInterceptor.PROCESS_ID;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = PartnerEndpoint.class)
@TestPropertySource(properties = {"spring.security.user.name=user", "spring.security.user.password=pass"})
class PartnerEndpointTest {
    private static final String BASE_PATH = "/partner-api/v1";
    private static final String BUDAPEST = "Budapest";
    private ObjectMapper mapper = new GeneralConfig().objectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EventService eventService;

    @Test
    @WithMockUser(roles = {SERVICE_USER})
    void events() throws Exception {
        EventsResponse rp = new EventsResponse();
        Info info = new Info();
        info.setSuccess(true);
        rp.setInfo(info);
        Event event = new Event();
        event.setLocation(BUDAPEST);
        rp.setData(Collections.singletonList(event));
        when(eventService.getEvents()).thenReturn(rp);

        mvc.perform(MockMvcRequestBuilders
                .get(BASE_PATH + "/events")
                .headers(getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].location").value(BUDAPEST));
    }

    @Test
    @WithMockUser(roles = {SERVICE_USER})
    void event() throws Exception {
        EventsDetailsResponse rp = new EventsDetailsResponse();
        Info info = new Info();
        info.setSuccess(true);
        rp.setInfo(info);
        Seat seat = new Seat();
        seat.setPrice(BigDecimal.TEN);
        rp.setSeats(Collections.singletonList(seat));
        when(eventService.getEvent(BigDecimal.ONE)).thenReturn(rp);

        mvc.perform(MockMvcRequestBuilders
                .get(BASE_PATH + "/event/1")
                .headers(getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.seats[0].price").value(10));
    }

    @Test
    @WithMockUser(roles = {SERVICE_USER})
    void reserve() throws Exception {
        String reservationId = UUID.randomUUID().toString();
        ReservationResponse rp = new ReservationResponse();
        Info info = new Info();
        info.setSuccess(true);
        rp.setInfo(info);
        Seat seat = new Seat();
        seat.setPrice(BigDecimal.TEN);
        rp.setReservationId(reservationId);

        ReservationRequest rq = new ReservationRequest();
        rq.setSeatId("seatId");
        rq.setEventId(BigDecimal.ONE);

        when(eventService.reserve(rq)).thenReturn(rp);
        mvc.perform(MockMvcRequestBuilders
                .post(BASE_PATH + "/reserve")
                .headers(getHeaders())
                .content(asJsonString(rq))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationId").value(reservationId));
    }

    @Test
    @WithMockUser(roles = {SERVICE_USER})
    void reserve_sad() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(BASE_PATH + "/reserve")
                .headers(getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void reserve_sad2() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(BASE_PATH + "/reserve")
                .headers(getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private String asJsonString(final Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(PROCESS_ID, "processId");
        return headers;
    }
}
