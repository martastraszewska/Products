package pl.straszewska.product;

import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.straszewska.product.api.ProductRequest;
import pl.straszewska.product.app.Product;
import pl.straszewska.product.app.error.InvalidProductRequestException;

import java.util.List;

public class ProductValidationTest {

    @Test
    public void shouldParseProduct() {
        //when
        ProductRequest request = ProductRequestBuilder.builder().build();
        Product product = Product.fromProductRequest(request);
        //then
        Assertions.assertEquals(request.getName(), product.getName());
        Assertions.assertEquals(request.getDescription(), product.getDescription());
        Assertions.assertEquals(request.getPrice(), product.getPrice());
        Assertions.assertEquals(request.getCategory(), product.getCategory());
        Assertions.assertNotNull(product.getId());
    }

    @ParameterizedTest
    @MethodSource("testValidationInput")
    public void shouldThrowExceptionOnInvalidProduct(ProductRequest request, String expectedMessage) {
        //when
        Exception exception = assertThrows(InvalidProductRequestException.class, () -> {
            Product.fromProductRequest(request);
        });
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    public static List<Arguments> testValidationInput() {
        return List.of(
                Arguments.of(
                        ProductRequestBuilder.builder().setCategory("").build(),
                        String.format("%s is incorrect", "category"))
                ,
                Arguments.of(
                        ProductRequestBuilder.builder().setCategory(null).build(),
                        String.format("%s is incorrect", "category")),
                Arguments.of(
                        ProductRequestBuilder.builder().setCategory("   ").build(),
                        String.format("%s is incorrect", "category")),
                Arguments.of(
                        ProductRequestBuilder.builder().setName("").build(),
                        String.format("%s is incorrect", "name"))
                ,
                Arguments.of(
                        ProductRequestBuilder.builder().setName(null).build(),
                        String.format("%s is incorrect", "name")),
                Arguments.of(
                        ProductRequestBuilder.builder().setName("   ").build(),
                        String.format("%s is incorrect", "name")),
                Arguments.of(
                        ProductRequestBuilder.builder().setDescription("").build(),
                        String.format("%s is incorrect", "description"))
                ,
                Arguments.of(
                        ProductRequestBuilder.builder().setDescription(null).build(),
                        String.format("%s is incorrect", "description")),
                Arguments.of(
                        ProductRequestBuilder.builder().setDescription("   ").build(),
                        String.format("%s is incorrect", "description")),
                Arguments.of(
                        ProductRequestBuilder.builder().setPrice(-10.00f).build(),
                        String.format("%s is incorrect", "price"))
                ,
                Arguments.of(
                        ProductRequestBuilder.builder().setPrice(null).build(),
                        String.format("%s is incorrect", "price")),
                Arguments.of(
                        ProductRequestBuilder.builder().setPrice(10.456f).build(),
                        String.format("%s is incorrect", "price"))
        );
    }
}
