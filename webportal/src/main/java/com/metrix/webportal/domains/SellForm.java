package com.metrix.webportal.domains;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellForm {
    private int eventId;
    @Min(value = 1, message = "Number of ticket must at least 1")
    @Max(value = 100, message = "Number of ticket must less than 100")//to prevent the do while of generating unique qr code long run
    private int numberOfTicket;
}
