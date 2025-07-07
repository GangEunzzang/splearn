package tobyspring.splearn.domain.member;

import java.util.regex.Pattern;

public record Profile(String address) {

    private final static Pattern PROFILE_PATTERN = Pattern.compile("^[a-z0-9]+");

    public Profile {
        if (!PROFILE_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("프로필 형식이 바르지 않습니다: " + address);
        }

    }

}
