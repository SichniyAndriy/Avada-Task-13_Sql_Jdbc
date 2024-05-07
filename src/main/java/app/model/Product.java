package app.model;

import java.math.BigDecimal;
import lombok.Data;

@Data(staticConstructor = "EMPTY")
public class Product {
    Long id;
    String name;
    BigDecimal price;
}
