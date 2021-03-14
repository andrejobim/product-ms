package br.com.compasso.uol.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties(value = {
        "statusCode"
})
public class ResponseErrorModel {

    @JsonProperty("status_code")
    private int statusCode;

    private String message;

}
