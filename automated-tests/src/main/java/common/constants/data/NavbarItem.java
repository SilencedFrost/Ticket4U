package common.constants.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NavbarItem {

    LOGIN("/auth/login");

    private final String href;
}
