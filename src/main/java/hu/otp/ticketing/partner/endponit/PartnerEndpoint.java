package hu.otp.ticketing.partner.endponit;

import hu.otp.ticketing.partner.endpoint.rest.EventsApi;
import hu.otp.ticketing.partner.endpoint.rest.model.EventsDetailsResponse;
import hu.otp.ticketing.partner.endpoint.rest.model.EventsResponse;
import hu.otp.ticketing.partner.endpoint.rest.model.ReservationRequest;
import hu.otp.ticketing.partner.endpoint.rest.model.ReservationResponse;
import hu.otp.ticketing.partner.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("${api.basePath}")
@RequiredArgsConstructor
public class PartnerEndpoint implements EventsApi {
    private final EventService eventService;

    @Override
    public ResponseEntity<EventsDetailsResponse> getEvent(BigDecimal eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @Override
    public ResponseEntity<EventsResponse> getEvents() {
        return ResponseEntity.ok(eventService.getEvents());
    }

    @Override
    public ResponseEntity<ReservationResponse> reserve(@Valid ReservationRequest reservationRequest) {
        return ResponseEntity.ok(eventService.reserve(reservationRequest));
    }
}
