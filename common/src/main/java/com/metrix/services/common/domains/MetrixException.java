package com.metrix.services.common.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class MetrixException extends Exception{
    private int customErrCode;
    private String customErrMsg;
    private String returnUrl;
}