package com.metrix.services.common.domains;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventsForm {
    private int id;

    @NotBlank(message = "Event Name must be provided!")
    @Size(max = 255, message = "Event Name must less than 255 characters!")
    private String eventName;

    private boolean isStartSell;

    @Future(message = "Event Date must be a future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date eventDtm; 

    private int venueId;
}