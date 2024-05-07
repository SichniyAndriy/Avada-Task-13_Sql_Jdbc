package app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data(staticConstructor = "EMPTY")
public class Order {
    Long id;
    User user;
    BigDecimal totalPrice;
    String description;
    LocalDateTime timeStamp;
}
