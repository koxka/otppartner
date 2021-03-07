package hu.otp.ticketing.partner.mapper;

import hu.otp.ticketing.partner.endpoint.rest.model.EventsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventResponseMapper extends InformationMapper {
    @Mapping(source = "success", target = "info")
    EventsResponse map(hu.otp.ticketing.partner.dto.EventsResponse source);
}
