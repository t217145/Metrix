package com.metrix.services.common.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketsForm {
    private int eventId;

    private String operator;

    @Min(value = 1, message = "Number of ticket must at least 1")
    @Max(value = 100, message = "Number of ticket must less than 100")//to prevent the do while of generating unique qr code long run
    private int numberOfTicket;
}
