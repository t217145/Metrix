package com.metrix.services.common.domains;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tickets implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Operator must be provided!")
    @Size(max = 100, message = "Operator must less than 100 characters!")
    @Column(nullable = false)
    private String operator;

    @NotBlank(message = "QR Code must be provided!")
    @Size(max = 255, message = "QR Code must less than 100 characters!")
    @Column(nullable = false, unique = true)
    private String qrcode;

    private boolean isClaimed;

    @Past(message = "Sell date time must be a past date time")
    @Column(nullable = false)
    private Date sellDtm;

    @Past(message = "Claim date time must be a past date time")
    @Column(nullable = true)
    private Date claimDtm;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Events event;    
}