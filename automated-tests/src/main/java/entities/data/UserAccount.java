package entities.data;

import common.constants.data.AccountDefault;
import common.utility.RandomUtils;
import lombok.Data;

@Data
public class UserAccount {

    private String email;
    private String password;
    private String phoneNumber;

    public UserAccount(String email, String phoneNumber, String password) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public UserAccount(AccountDefault accountDefault) {
        this(accountDefault.getEmail(), accountDefault.getPhoneNumber(), accountDefault.getPassword());
    }

    public UserAccount(String identifier, String password) {
        this(identifier, identifier, password);
    }
}
