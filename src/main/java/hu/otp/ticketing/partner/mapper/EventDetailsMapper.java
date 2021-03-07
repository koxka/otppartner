package hu.otp.ticketing.partner.mapper;

import hu.otp.ticketing.partner.dto.EventDetails;
import hu.otp.ticketing.partner.endpoint.rest.model.EventsDetailsResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SeatMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EventDetailsMapper extends InformationMapper {
    @Mapping(source = "data.seats", target = "seats")
    @Mapping(source = "success", target = "info")
    EventsDetailsResponse map(EventDetails s);


}
