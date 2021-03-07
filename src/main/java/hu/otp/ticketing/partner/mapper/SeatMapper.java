package hu.otp.ticketing.partner.mapper;

import hu.otp.ticketing.partner.endpoint.rest.model.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat map(hu.otp.ticketing.partner.dto.Seat s);
}
