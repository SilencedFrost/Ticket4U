package user.entities.data;

import common.constants.data.AccountDefault;
import lombok.Data;

@Data
public class UserAccount {

    private String email;
    private String password;

    public UserAccount(AccountDefault accountDefault) {
        this.email = accountDefault.getEmail();
        this.password = accountDefault.getPassword();
    }
}
