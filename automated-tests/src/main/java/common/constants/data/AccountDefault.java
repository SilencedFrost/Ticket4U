package common.constants.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountDefault {

    CUSTOMER("customer@gmail.com", "DefaultP4$$");

    private final String email;
    private final String password;
}
