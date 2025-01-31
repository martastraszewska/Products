package pl.straszewska.product.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private Float price;
    private String category;
}
