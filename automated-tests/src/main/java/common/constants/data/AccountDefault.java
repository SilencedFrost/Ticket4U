package common.constants.data;

import common.utility.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountDefault {

    CUSTOMER(AccountConstant.EMAIL_CUSTOMER, AccountConstant.PASSWORD_DEFAULT, AccountConstant.PHONE_DEFAULT),
    RANDOM(RandomUtils.generateRandomEmail(), RandomUtils.generateRandomPassword(), RandomUtils.generateRandomPhoneNumber());

    private final String email;
    private final String password;
    private final String phoneNumber;
}
