package hu.otp.ticketing.partner.mapper;

import hu.otp.ticketing.partner.dto.Seat;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeatMapperTest {
    public static final boolean RESERVED = true;
    public static final String HUF = "HUF";
    public static final BigDecimal PRICE = BigDecimal.ONE;
    public static final String ID = "id";
    private SeatMapper mapper = new SeatMapperImpl();

    @Test
    void map() {
        Seat source = createSource();
        hu.otp.ticketing.partner.endpoint.rest.model.Seat mappedSeat = mapper.map(source);

        assertEquals(RESERVED, mappedSeat.getReserved());
        assertEquals(HUF, mappedSeat.getCurrency());
        assertEquals(PRICE, mappedSeat.getPrice());
        assertEquals(ID, mappedSeat.getId());

    }

    private Seat createSource() {
        Seat seat = new Seat();
        seat.setReserved(RESERVED);
        seat.setCurrency(HUF);
        seat.setPrice(PRICE);
        seat.setId(ID);
        return seat;
    }
}
