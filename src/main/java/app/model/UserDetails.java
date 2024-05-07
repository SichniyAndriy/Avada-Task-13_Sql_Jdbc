package app.model;

import lombok.Data;

@Data(staticConstructor = "EMPTY")
public class UserDetails {
    Long id;
    User user;
    String postalCode;
    String city;
    String street;
    String house;
    String ipn;
    String passport;
}
