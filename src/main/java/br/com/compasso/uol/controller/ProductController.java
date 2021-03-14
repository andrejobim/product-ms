package br.com.compasso.uol.controller;

import br.com.compasso.uol.entity.ResponseErrorModel;
import br.com.compasso.uol.entity.Product;
import br.com.compasso.uol.exception.ProductNotFoundException;
import br.com.compasso.uol.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;

@Api(value = "Product API")
@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductService productService;

    private static String QUERY_Q = "q";
    private static String QUERY_MIN_PRICE = "min_price";
    private static String QUERY_MAX_PRICE = "max_price";

    @ApiOperation(value = "Create new product")
    @PostMapping(value ="/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity save(@ApiParam(value = "payload", required = true)
                                       @Valid @RequestBody ProductModel payload) {
        Product product = productService.save(payload);
        return new ResponseEntity(product, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Change existing product")
    @PutMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@ApiParam(name = "id", value = "id", required = true) @PathVariable String id,
                              @Valid @RequestBody ProductModel productModel) {
        Product product = productService.update(Integer.valueOf(id), productModel);
        return new ResponseEntity(product, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Product search by id")
    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity product(@ApiParam(name = "id", value = "id", required = true) @PathVariable String id) {
        return ResponseEntity.ok(productService.findProductByIdIfNotExistException(Integer.valueOf(id)));
    }

    @ApiOperation(value = "All the products")
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity  products() {
        return ResponseEntity.ok(productService.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "Where product.name = q OR product.description = q", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "min_price", value = "Where product.price >= min_price",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "max_price", value = "Where product.price <= min_price",  dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "Search products")
    @GetMapping(value="/products/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity search(@ApiParam(hidden = true) @RequestParam MultiValueMap<String, String> params) {
        String queryQ = params.containsKey(QUERY_Q) ? params.get(QUERY_Q).get(0) : null;
        BigDecimal priceMin = params.containsKey(QUERY_MIN_PRICE) ?
                BigDecimal.valueOf(Double.valueOf(params.get(QUERY_MIN_PRICE).get(0))) : null;
        BigDecimal priceMax = params.containsKey(QUERY_MAX_PRICE) ?
                BigDecimal.valueOf(Double.valueOf(params.get(QUERY_MAX_PRICE).get(0))) : null;
        return ResponseEntity.ok(productService.search(priceMin, priceMax, queryQ));
    }

    @ApiOperation(value = "Delete Product search by id")
    @DeleteMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@ApiParam(name = "id", value = "id", required = true) @PathVariable String id) {
        productService.delete(Integer.valueOf(id));
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseErrorModel handleServiceException(HttpServletRequest req, HttpServletResponse response, Exception ex){
        response.setHeader("Content-type", "application/json");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return formatBindingErrors(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
