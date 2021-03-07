package hu.otp.ticketing.partner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Seat {
    private String id;
    private BigDecimal price;
    private String currency;
    private boolean reserved;
}
