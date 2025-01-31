package pl.straszewska.product.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private Float price;
    private String category;

}
