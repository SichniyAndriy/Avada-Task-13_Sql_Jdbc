package app.model;

import lombok.Data;

@Data(staticConstructor = "EMPTY")
public class ShoppingCart {
    Long id;
    User user;
    Product product;
    Integer amount;
}
