package br.com.compasso.uol.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductModel {

    @NotEmpty(message = "Please provide a name product")
    @ApiModelProperty(dataType = "String", notes = "provide a name product", example = "Galaxy Android")
    private String name;

    @NotEmpty(message = "Please provide a description product")
    @ApiModelProperty(dataType = "String", notes = "provide a description product", example = "Cell Phone")
    private String description;

    @NotNull(message = "Please provide a price product")
    @Positive( message = "Product price must be greater than zero")
    @ApiModelProperty(dataType = "BigDecimal", notes = "provide a price product", example = "10")
    private BigDecimal price;


}
