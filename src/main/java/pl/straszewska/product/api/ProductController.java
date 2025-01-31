package pl.straszewska.product.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.straszewska.product.app.Product;
import pl.straszewska.product.app.ProductService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(ProductController.PRODUCT_API_ROOT_PATH)
public class ProductController {
    public static final String PRODUCT_API_ROOT_PATH = "/api/v1/products";
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(toResponse(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<ProductResponse> responseList = productService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getByCategory(@PathVariable String category) {
        List<ProductResponse> responseList = productService.findByCategory(category)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        Product product = productService.create(productRequest);
        return ResponseEntity.created(URI.create(PRODUCT_API_ROOT_PATH + "/" + product.getId()))
                .body(toResponse(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable String id,
                                                  @RequestBody ProductRequest productRequest) {
        Product product = productService.update(id, productRequest);
        return ResponseEntity.ok(toResponse(product));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {

        productService.delete(id);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .category(product.getCategory())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
