package com.example;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TxnCompletePayload {

    private Long id ;
    private Boolean success ;
    private String reason ;
    private String requestId ;
}
