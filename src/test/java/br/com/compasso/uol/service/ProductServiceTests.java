package br.com.compasso.uol.service;

import br.com.compasso.uol.controller.ProductModel;
import br.com.compasso.uol.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Test
    void shouldCreateProduct() {
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.ONE));
        Product product = productService.findAll().get(0);
        assertThat(product.getName()).isEqualTo("Nome 01");
        assertThat(product.getDescription()).isEqualTo("Descrição 01");
        assertThat(product.getPrice().setScale(2)).isEqualTo(BigDecimal.ONE.setScale(2));
    }

    @Test
    void shouldCreateDeleteProduct(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.ONE));
        Product product = productService.findAll().get(0);
        productService.delete(product.getId());
        assertThat(productService.findAll().size()).isEqualTo(0);
    }

    @Test
    void shouldErrorDeleteProduct(){
        try {
            productService.delete(1);
        } catch (Exception e) {
            assertThat(String.format("Product not found id: %d",1)).isEqualTo(e.getMessage());
        }
    }

    @Test
    void shouldFindProduct(){
        Product product = productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.ONE));
        product = productService.findProductByIdIfNotExistException(product.getId());
        assertThat(product.getName()).isEqualTo("Nome 01");
        assertThat(product.getDescription()).isEqualTo("Descrição 01");
        assertThat(product.getPrice().setScale(2)).isEqualTo(BigDecimal.ONE.setScale(2));
    }

    @Test
    void shouldErrorFindProduct(){
        try {
            productService.findProductByIdIfNotExistException(1);
        } catch (Exception e) {
            assertThat(String.format("Product not found id: %d",1)).isEqualTo(e.getMessage());
        }
    }

    @Test
    void shouldSearchProductByMinPrice(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.TEN));
        List<Product> products = productService.search(BigDecimal.TEN, null, null);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    void shouldSearchProductByMaxPrice(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.TEN));
        List<Product> products = productService.search(null, BigDecimal.TEN, null);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    void shouldSearchProductByName(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.TEN));
        List<Product> products = productService.search(null, null, "Nome");
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    void shouldSearchProductByDescription(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.TEN));
        List<Product> products = productService.search(null, null, "Descrição");
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    void shouldSearchProductAllParams(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.TEN));
        List<Product> products = productService.search(BigDecimal.TEN, BigDecimal.TEN, "Descrição");
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    void shouldNotFoundSearchProductPriceMax(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.TEN));
        List<Product> products = productService.search(BigDecimal.TEN, BigDecimal.ONE, "Descrição");
        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    void shouldNotFoundSearchProductPriceMin(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.TEN));
        List<Product> products = productService.search(BigDecimal.TEN.add(BigDecimal.TEN), BigDecimal.TEN, "Descrição");
        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    void shouldNotFoundSearchProductNameDescription(){
        productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.TEN));
        List<Product> products = productService.search(BigDecimal.TEN, BigDecimal.TEN, "Teste 01");
        assertThat(products.size()).isEqualTo(0);
    }


    @Test
    void shouldUpdateProduct(){
        ProductModel productModelSave = createProduct("Nome 01", "Descrição 01", BigDecimal.ONE);
        Integer id = productService.save(productModelSave).getId();

        ProductModel productModelUpdate = createProduct("Nome 02", "Descrição 02", BigDecimal.TEN);
        Product product = productService.update(id, productModelUpdate);

        assertThat(product.getName()).isEqualTo("Nome 02");
        assertThat(product.getDescription()).isEqualTo("Descrição 02");
        assertThat(product.getPrice().setScale(2)).isEqualTo(BigDecimal.TEN.setScale(2));
    }

    @Test
    void shouldErrorCreateProductNameBlank() {
        try {
            productService.save(createProduct(null, "Descrição 01", BigDecimal.ONE));
        } catch (Exception e){
            assertThat("Please provide a name product").isEqualTo(e.getMessage());
        }
    }

    @Test
    void shouldErrorCreateProductDescriptionBlank() {
        try {
            productService.save(createProduct("Nome 01", "Descrição 01", BigDecimal.ONE));
        } catch (Exception e){
            assertThat("Please provide a description product").isEqualTo(e.getMessage());
        }
    }

    @Test
    void shouldErrorCreateProductPriceNull() {
        try {
            productService.save(createProduct("Nome 01", "Descrição 01", null));
        } catch (Exception e){
            assertThat("Please provide a price product").isEqualTo(e.getMessage());
        }
    }

    @Test
    void shouldErrorCreateProductPriceNegative() {
        try {
            productService.save(createProduct("Nome 01", "Descrição 01",
                    BigDecimal.ONE.subtract(BigDecimal.TEN)));
        } catch (Exception e){
            assertThat("Product price must be greater than zero").isEqualTo(e.getMessage());
        }
    }

    private ProductModel createProduct(String name, String description, BigDecimal price) {
        return ProductModel.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();
    }

}
