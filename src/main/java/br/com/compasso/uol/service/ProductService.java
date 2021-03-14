package br.com.compasso.uol.service;

import br.com.compasso.uol.controller.ProductModel;
import br.com.compasso.uol.entity.Product;
import br.com.compasso.uol.exception.ProductNotFoundException;
import br.com.compasso.uol.repo.ProductRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product save(ProductModel productModel){
        validateProductModel(productModel);
        Product product = createProduct(productModel);
        productRepository.save(product);
        return product;
    }

    public Product update(Integer id, ProductModel productModel) {
        hasProductWithId(id);
        validateProductModel(productModel);
        Product product = mergeProductModel(id, productModel);
        productRepository.save(product);
        return product;
    }

    public void delete(Integer id) {
        hasProductWithId(id);
        productRepository.deleteById(id);
    }

    public Product findProductByIdIfNotExistException(Integer id) {
        hasProductWithId(id);
        return productRepository.findById(id).get();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> search(BigDecimal priceMin, BigDecimal priceMax, String queryQ) {
        return productRepository.search(priceMin, priceMax, queryQ);
    }

    private Product mergeProductModel(Integer id, ProductModel productModel) {
        Product product = productRepository.findById(id).get();
        product.setPrice(productModel.getPrice());
        product.setDescription(productModel.getDescription());
        product.setName(productModel.getName());
        return product;
    }


    private Product createProduct(ProductModel productModel) {
        return Product.builder()
                .name(productModel.getName())
                .description(productModel.getDescription())
                .price(productModel.getPrice())
                .build();
    }

    private void validateProductModel(ProductModel productModel) {
        if (StringUtils.isBlank(productModel.getName())){
            throw new ProductNotFoundException("Please provide a name product");
        }
        if (StringUtils.isBlank(productModel.getDescription())){
            throw new ProductNotFoundException("Please provide a description product");
        }
        if(ObjectUtils.isEmpty(productModel.getPrice())) {
            throw new ProductNotFoundException("Please provide a price product");
        }
        if (productModel.getPrice().compareTo(BigDecimal.ZERO) == -1){
            throw new ProductNotFoundException("Product price must be greater than zero");
        }
    }

    private void hasProductWithId(Integer id) {
        if (!productRepository.existsById(id)){
            throw new ProductNotFoundException(String.format("Product not found id: %d",id));
        }
    }

}
