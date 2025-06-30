package com.example.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TxnRequestDTO {

    @NotNull
    private Long  fromUserId ;

    @NotNull
    private Long toUserId ;

    @NotNull
    private Double amount ;

    @NotNull
    private String comment ;

}
