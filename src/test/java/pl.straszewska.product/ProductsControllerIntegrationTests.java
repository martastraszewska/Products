package pl.straszewska.product;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.straszewska.product.api.ErrorResponse;
import static pl.straszewska.product.api.ProductController.PRODUCT_API_ROOT_PATH;
import pl.straszewska.product.api.ProductRequest;
import pl.straszewska.product.api.ProductResponse;
import pl.straszewska.product.infrastructure.ProductEntity;
import pl.straszewska.product.infrastructure.ProductRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class ProductsControllerIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void before() {
        productRepository.deleteAll();
    }

    private static final String PASSWORD = "password";
    private static final String USER = "user@gmail.com";
    private static final String ADMIN = "admin@gmail.com";

    @Test
    public void shouldCreateProductWhenAdmin() {
        //when
        ProductRequest req = ProductRequestBuilder.builder().build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());

        ResponseEntity<ProductResponse> res = this.testRestTemplate.withBasicAuth(ADMIN, PASSWORD).postForEntity(PRODUCT_API_ROOT_PATH, request, ProductResponse.class);
        ProductResponse body = res.getBody();

        //then
        Assertions.assertEquals(PRODUCT_API_ROOT_PATH + "/" + body.getId(), res.getHeaders().getLocation().toString());
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        //and
        Assertions.assertTrue(body.getId() != null && !body.getId().isBlank());
        Assertions.assertEquals(req.getName(), body.getName());
        Assertions.assertEquals(req.getDescription(), body.getDescription());
        Assertions.assertEquals(req.getCategory(), body.getCategory());
        Assertions.assertEquals(req.getPrice(), body.getPrice());

        //and
        Assertions.assertEquals(productRepository.count(), 1L);

        //and
        ProductEntity entity = productRepository.findAll().get(0);
        Assertions.assertTrue(entity.getId() != null && !entity.getId().isBlank());
        Assertions.assertEquals(req.getCategory(), entity.getCategory());
        Assertions.assertEquals(req.getName(), entity.getName());
        Assertions.assertEquals(req.getDescription(), entity.getDescription());
        Assertions.assertEquals(req.getPrice(), Float.valueOf(entity.getPrice()));

        //and
        Assertions.assertEquals(1, productRepository.count());
    }

    @Test
    public void shouldNotCreateProductWhenUser() {
        //when
        ProductRequest req = ProductRequestBuilder.builder().build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());

        ResponseEntity<ProductResponse> res = this.testRestTemplate.withBasicAuth(USER, PASSWORD).postForEntity(PRODUCT_API_ROOT_PATH, request, ProductResponse.class);

        //then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());

        //and
        Assertions.assertEquals(0, productRepository.count());
    }

    @Test
    public void shouldReturn401WhenIncorrectUser() {
        //when
        ProductRequest req = ProductRequestBuilder.builder().build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());

        ResponseEntity<ProductResponse> res = this.testRestTemplate.withBasicAuth("dummy", "dummy").postForEntity(PRODUCT_API_ROOT_PATH, request, ProductResponse.class);

        //then
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
    }

    @Test
    public void shouldUpdateProductWhenAdmin() {
        //given
        ProductEntity existingProduct = existingProduct();
        String id = existingProduct.getId();

        //when
        ProductRequest req = ProductRequestBuilder.builder().build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());
        ResponseEntity<ProductResponse> res =
                this.testRestTemplate.withBasicAuth(ADMIN, PASSWORD).exchange(PRODUCT_API_ROOT_PATH + "/" + id, HttpMethod.PUT, request, ProductResponse.class);
        ProductResponse body = res.getBody();

        //then
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(id, body.getId());
        Assertions.assertEquals(req.getName(), body.getName());
        Assertions.assertEquals(req.getDescription(), body.getDescription());
        Assertions.assertEquals(req.getCategory(), body.getCategory());
        Assertions.assertEquals(req.getPrice(), body.getPrice());

        //and
        Assertions.assertEquals(1, productRepository.count());
    }

    @Test
    public void shouldNotUpdateProductWhenUser() {
        //given
        ProductEntity existingProduct = existingProduct();
        String id = existingProduct.getId();

        //when
        ProductRequest req = ProductRequestBuilder.builder().build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());
        ResponseEntity<ProductResponse> res =
                this.testRestTemplate.withBasicAuth(USER, PASSWORD).exchange(PRODUCT_API_ROOT_PATH + "/" + id, HttpMethod.PUT, request, ProductResponse.class);
        //then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
    }

    @Test
    public void shouldDeleteProductWhenAdmin() {
        //given
        ProductEntity existingProduct = existingProduct();
        String id = existingProduct.getId();

        //when
        ProductRequest req = ProductRequestBuilder.builder().build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());
        ResponseEntity<Void> res =
                this.testRestTemplate.withBasicAuth(ADMIN, PASSWORD).exchange(PRODUCT_API_ROOT_PATH + "/" + id, HttpMethod.DELETE, request, Void.class);

        //then
        Assertions.assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());

        //and
        Assertions.assertEquals(productRepository.count(), 0L);
    }

    @Test
    public void shouldNotDeleteProductWhenUser() {
        //given
        ProductEntity existingProduct = existingProduct();
        String id = existingProduct.getId();

        //when
        ProductRequest req = ProductRequestBuilder.builder().build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());
        ResponseEntity<Void> res =
                this.testRestTemplate.withBasicAuth(USER, PASSWORD).exchange(PRODUCT_API_ROOT_PATH + "/" + id, HttpMethod.DELETE, request, Void.class);

        //then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN, USER})
    public void shouldReadProductByIdForUserAndAdmin(String user) {
        //given
        ProductEntity existingProduct = existingProduct();
        String id = existingProduct.getId();

        //when
        ResponseEntity<ProductResponse> res = this.testRestTemplate.withBasicAuth(user, PASSWORD).getForEntity(PRODUCT_API_ROOT_PATH + "/" + id.toString(), ProductResponse.class);
        ProductResponse body = res.getBody();

        //then
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        //and
        Assertions.assertEquals(existingProduct.getId(), body.getId());
        Assertions.assertEquals(existingProduct.getCategory(), body.getCategory());
        Assertions.assertEquals(existingProduct.getName(), body.getName());
        Assertions.assertEquals(existingProduct.getDescription(), body.getDescription());
        Assertions.assertEquals(Float.valueOf(existingProduct.getPrice()), body.getPrice());

        //and
        Assertions.assertEquals(productRepository.count(), 1L);
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN, USER})
    public void shouldReadProductByCategory(String user) {
        //given
        ProductEntity firstProduct = existingProduct();
        ProductEntity secondProduct = existingProduct();
        String category = firstProduct.getCategory();

        //when
        ResponseEntity<ProductResponse[]> res = this.testRestTemplate.withBasicAuth(user, PASSWORD).getForEntity(PRODUCT_API_ROOT_PATH + "/category/" + category, ProductResponse[].class);
        List<ProductResponse> body = Arrays.stream(res.getBody()).toList();

        //then
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        //and
        Assertions.assertEquals(body.size(), 2);

        //and
        ProductResponse first =
                body.stream()
                        .filter(productResponse -> Objects.equals(productResponse.getId(), firstProduct.getId()))
                        .findFirst().get();

        Assertions.assertEquals(firstProduct.getCategory(), first.getCategory());
        Assertions.assertEquals(firstProduct.getName(), first.getName());
        Assertions.assertEquals(firstProduct.getDescription(), first.getDescription());
        Assertions.assertEquals(Float.valueOf(firstProduct.getPrice()), first.getPrice());

        //and
        ProductResponse second =
                body.stream()
                        .filter(productResponse -> Objects.equals(productResponse.getId(), secondProduct.getId()))
                        .findFirst().get();

        Assertions.assertEquals(secondProduct.getCategory(), second.getCategory());
        Assertions.assertEquals(secondProduct.getName(), second.getName());
        Assertions.assertEquals(secondProduct.getDescription(), second.getDescription());
        Assertions.assertEquals(Float.valueOf(secondProduct.getPrice()), second.getPrice());

        //and
        Assertions.assertEquals(productRepository.count(), 2L);
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN, USER})
    public void shouldReadAllProducts(String user) {
        //given
        ProductEntity firstProduct = existingProduct();
        ProductEntity secondProduct = existingProduct();

        //when
        ResponseEntity<ProductResponse[]> res = this.testRestTemplate.withBasicAuth(user, PASSWORD).getForEntity(PRODUCT_API_ROOT_PATH, ProductResponse[].class);
        List<ProductResponse> body = Arrays.stream(res.getBody()).toList();

        //then
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        //and
        Assertions.assertEquals(body.size(), 2);

        //and
        ProductResponse first =
                body.stream()
                        .filter(productResponse -> Objects.equals(productResponse.getId(), firstProduct.getId().toString()))
                        .findFirst().get();

        Assertions.assertEquals(firstProduct.getCategory(), first.getCategory());
        Assertions.assertEquals(firstProduct.getName(), first.getName());
        Assertions.assertEquals(firstProduct.getDescription(), first.getDescription());
        Assertions.assertEquals(Float.valueOf(firstProduct.getPrice()), first.getPrice());

        //and
        ProductResponse second =
                body.stream()
                        .filter(productResponse -> Objects.equals(productResponse.getId(), secondProduct.getId()))
                        .findFirst().get();

        Assertions.assertEquals(secondProduct.getCategory(), second.getCategory());
        Assertions.assertEquals(secondProduct.getName(), second.getName());
        Assertions.assertEquals(secondProduct.getDescription(), second.getDescription());
        Assertions.assertEquals(Float.valueOf(secondProduct.getPrice()), second.getPrice());

        //and
        Assertions.assertEquals(productRepository.count(), 2L);
    }

    @Test
    public void shouldNotCreateProductWhenWrongRequest() {
        //when
        ProductRequest req = ProductRequestBuilder.builder().setCategory("").build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());
        ResponseEntity<ErrorResponse> res = this.testRestTemplate.withBasicAuth(ADMIN, PASSWORD).postForEntity(PRODUCT_API_ROOT_PATH, request, ErrorResponse.class);

        //then
        Assert.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        Assert.assertEquals(String.format("%s is incorrect", "category"), res.getBody().getErrorMessage());
    }

    @Test
    public void shouldNotUpdateProductWhenWrongRequest() {
        //given
        String id = existingProduct().getId();

        //when
        ProductRequest req = ProductRequestBuilder.builder().setCategory("").build();
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());
        ResponseEntity<ErrorResponse> res = this.testRestTemplate.withBasicAuth(ADMIN, PASSWORD).exchange(PRODUCT_API_ROOT_PATH + "/" + id, HttpMethod.PUT, request, ErrorResponse.class);

        //then
        Assert.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        Assert.assertEquals(String.format("%s is incorrect", "category"), res.getBody().getErrorMessage());
    }

    @Test
    public void shouldReturnErrorOnUpdateWhenProductNotExists() {
        //given
        String id = UUID.randomUUID().toString();
        ProductRequest req = ProductRequestBuilder.builder().build();

        //when
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());
        ResponseEntity<ErrorResponse> res = this.testRestTemplate.withBasicAuth(ADMIN, PASSWORD).exchange(PRODUCT_API_ROOT_PATH + "/" + id, HttpMethod.PUT, request, ErrorResponse.class);

        //then
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        Assert.assertEquals(String.format("Product not found %s", id), res.getBody().getErrorMessage());
    }

    @Test
    public void shouldReturnErrorOnDeleteWhenProductNotExists() {
        //given
        String id = UUID.randomUUID().toString();
        ProductRequest req = ProductRequestBuilder.builder().build();

        //when
        HttpEntity<ProductRequest> request = new HttpEntity<>(req, new HttpHeaders());
        ResponseEntity<ErrorResponse> res = this.testRestTemplate.withBasicAuth(ADMIN, PASSWORD).exchange(PRODUCT_API_ROOT_PATH + "/" + id, HttpMethod.DELETE, request, ErrorResponse.class);

        //then
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        Assert.assertEquals(String.format("Product not found %s", id), res.getBody().getErrorMessage());
    }

    private ProductEntity existingProduct() {
        ProductEntity existingProduct = new ProductEntity(
                UUID.randomUUID().toString(),
                "name",
                "description",
                "1.25",
                "category"
        );
        return productRepository.save(existingProduct);
    }

}
