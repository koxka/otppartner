package hu.otp.ticketing.partner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.otp.ticketing.partner.common.ErrorCode;
import hu.otp.ticketing.partner.dto.EventDetails;
import hu.otp.ticketing.partner.dto.EventsResponse;
import hu.otp.ticketing.partner.dto.Seat;
import hu.otp.ticketing.partner.endpoint.rest.model.*;
import hu.otp.ticketing.partner.mapper.EventDetailsMapper;
import hu.otp.ticketing.partner.mapper.EventResponseMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static hu.otp.ticketing.partner.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EventService {
    private final ObjectMapper objectMapper;
    private final EventResponseMapper eventResponseMapper;
    private final EventDetailsMapper eventDetailsMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    /**
     * Returns the list of events wrapped.
     *
     * @return {@link EventsResponse}
     */
    public hu.otp.ticketing.partner.endpoint.rest.model.EventsResponse getEvents() {
        String file = "getEvents.json";
        try {
            return eventResponseMapper.map(objectMapper.readValue(getClass().getClassLoader().getResource(file), EventsResponse.class));
        } catch (IOException e) {
            LOGGER.error("couldn't read file {}", file);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        EventsResponse eventsResponse = new EventsResponse();
        eventsResponse.setSuccess(false);
        return eventResponseMapper.map(eventsResponse);
    }

    /**
     * Returns information of the given event.
     *
     * @param id event id
     * @return {@link EventDetails}
     */
    public EventsDetailsResponse getEvent(BigDecimal id) {
        EventDetails eventDetails = readEvent("getEvent" + id + ".json");
        return eventDetails == null ? createUnsuccessfulRp() : eventDetailsMapper.map(eventDetails);

    }

    /**
     * "Makes" reservation to a a gevien event and given seat, if available.
     *
     * @param reservationRequest containint event and seat id.
     * @return {@link ReservationResponse} containing the state of the reservation.
     */
    public ReservationResponse reserve(ReservationRequest reservationRequest) {
        EventDetails eventDetails = readEvent("getEvent" + reservationRequest.getEventId() + ".json");
        if (eventDetails == null) {
            return createReservationErrorResponse(NO_SUCH_FILE);
        } else {
            Optional<Seat> seat = CollectionUtils.emptyIfNull(eventDetails.getData().getSeats()).stream().filter(s -> s.getId().equals(reservationRequest.getSeatId())).findAny();
            if (!seat.isPresent()) {
                return createReservationErrorResponse(NO_SUCH_SEAT);
            } else {
                if (seat.get().isReserved()) {
                    return createReservationErrorResponse(SEAT_ALREADY_TAKEN);
                } else {
                    //TODO: do the physical reservation here
                    return createReservationSuccessResponse(UUID.randomUUID().toString());
                }
            }

        }
    }

    private ReservationResponse createReservationErrorResponse(ErrorCode errorCode) {
        return createReservationResponse(false, errorCode, null);
    }

    private ReservationResponse createReservationSuccessResponse(String reservationId) {
        return createReservationResponse(true, SUCCESS, reservationId);
    }

    private ReservationResponse createReservationResponse(boolean success, ErrorCode errorCode, String uuid) {
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setInfo(createInfo(success, errorCode));
        reservationResponse.setReservationId(uuid);
        return reservationResponse;
    }

    private EventsDetailsResponse createUnsuccessfulRp() {
        EventsDetailsResponse unsuccessfulEventDetails = new EventsDetailsResponse();
        unsuccessfulEventDetails.setInfo(createInfo(false, NO_SUCH_FILE));
        return unsuccessfulEventDetails;
    }

    private Info createInfo(boolean success, ErrorCode errorCode) {
        Info info = new Info();
        info.setSuccess(success);
        State state = new State();
        state.setCode(errorCode.getCode());
        state.setReason(errorCode.name());
        info.setState(state);
        return info;
    }

    private EventDetails readEvent(String fileName) {
        try {
            return objectMapper.readValue(getClass().getClassLoader().getResource(fileName), EventDetails.class);
        } catch (Exception e) {
            LOGGER.error("couldn't read file {}", fileName);
        }
        return null;
    }
}
