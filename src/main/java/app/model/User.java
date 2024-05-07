package app.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data(staticConstructor = "EMPTY")
public class User {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    LocalDateTime timeStamp;
}
