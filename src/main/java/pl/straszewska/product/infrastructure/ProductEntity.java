package pl.straszewska.product.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.straszewska.product.app.Product;

import java.text.DecimalFormat;


@AllArgsConstructor
@Getter
@Table("products")
public class ProductEntity {

    @PrimaryKey
    private String id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String price;

    @Column
    private String category;

    public static ProductEntity fromProduct(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().toString(),
                product.getCategory());
    }

    public Product toProduct() {
        return new Product(
                this.id,
                this.name,
                this.description,
                Float.valueOf(this.price),
                this.category);
    }
}
