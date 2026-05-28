package com.gestix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordResponse {

    private String message;

    /** Solo en localhost: aviso si SMTP falló (para no confundir en desarrollo). */
    private String devHint;
}
