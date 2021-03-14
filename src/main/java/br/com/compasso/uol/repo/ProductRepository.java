package br.com.compasso.uol.repo;

import br.com.compasso.uol.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM product p WHERE " +
            " ( :minPrice is null AND :maxPrice is null AND :q is null) OR " +

            " ( ( :minPrice is not null AND p.price >= :minPrice ) AND " +
            "   ( :maxPrice is null AND :q is null) ) OR " +

            " ( ( :maxPrice is not null AND p.price >= :maxPrice ) AND " +
            "   ( :minPrice is null AND :q is null) ) OR " +

            " ( ( :minPrice is not null AND p.price >= :minPrice ) AND " +
            "   ( :maxPrice is not null AND p.price <= :maxPrice ) AND " +
            "   ( :q is null) ) OR " +

            " ( ( :minPrice is not null AND p.price >= :minPrice ) AND " +
            "   ( :maxPrice is not null AND p.price <= :maxPrice ) AND " +
            "   ( :q is not null AND LOWER( p.description ) like LOWER( concat('%', concat(:q, '%')))) AND " +
            "   ( :q is not null AND LOWER( p.name )        like LOWER( concat('%', concat(:q, '%')))) ) OR "+

            " ( (  :minPrice is null AND :maxPrice is null  ) AND " +
            "   (( :q is not null AND LOWER( p.description ) like LOWER( concat('%', concat(:q, '%')))) AND " +
            "   ( :q is not null AND LOWER( p.name )        like LOWER( concat('%', concat(:q, '%'))))) ) OR "+

            " ( ( :minPrice is not null AND p.price >= :minPrice ) AND " +
            "   ( :maxPrice is not null AND p.price <= :maxPrice ) AND " +
            "   (( :q is not null AND LOWER( p.description ) like LOWER( concat('%', concat(:q, '%')))) OR " +
            "   ( :q is not null AND LOWER( p.name )        like LOWER( concat('%', concat(:q, '%'))))) ) OR" +

            " ( ( :minPrice is null AND :maxPrice is null ) AND " +
            "   (( :q is not null AND LOWER( p.description ) like LOWER( concat('%', concat(:q, '%')))) OR " +
            "   ( :q is not null AND LOWER( p.name )        like LOWER( concat('%', concat(:q, '%'))))) ) ",

            nativeQuery = true)
    List<Product> search(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice,
                         @Param("q") String qName);
}
