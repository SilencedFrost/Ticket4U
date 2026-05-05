package common.utility;

import java.security.SecureRandom;

public class RandomUtils {

    // Charsets
    public static final String LOWERCASE_ALPHA = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPERCASE_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMERICAL = "0123456789";
    public static final String SPECIAL_CHARACTERS = "!@$^*()-_=+[]{}\\|;:\",./?~`";

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString(String charset, Integer length) {
        if (length == null || length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer");
        }

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charset.length());
            result.append(charset.charAt(index));
        }

        return result.toString();
    }

     // Returns random string with set length, contains only alphanumeric
    public static String generateRandomString(Integer length) {
        return generateRandomString(RandomUtils.LOWERCASE_ALPHA + RandomUtils.UPPERCASE_ALPHA + RandomUtils.NUMERICAL, length);
    }

    // Returns password containing: uppercase letter, lowercase letter, numbers, and special characters
    public static String generateRandomPassword(int length) {
        // Fail fast
        if(length < 5) throw  new IllegalArgumentException("Password length generated via this method can't be less than 5 characters long");
        // Randomize one of each type to ensure string always has all types
        String randomNumber = generateRandomString(RandomUtils.NUMERICAL, 1);
        String randomLowercase = generateRandomString(RandomUtils.LOWERCASE_ALPHA, 1);
        String randomUppercase = generateRandomString(RandomUtils.UPPERCASE_ALPHA, 1);
        String randomSpecial = generateRandomString(RandomUtils.SPECIAL_CHARACTERS, 1);
        String random = generateRandomString(RandomUtils.LOWERCASE_ALPHA + RandomUtils.UPPERCASE_ALPHA + RandomUtils.NUMERICAL + RandomUtils.SPECIAL_CHARACTERS, length - 4);
        return randomNumber + randomLowercase + randomUppercase + randomSpecial + random;
    }

    // Returns password containing: uppercase letter, lowercase letter, numbers, and special characters that is 14 characters long
    public static String generateRandomPassword() {
        return generateRandomPassword(14);
    }

    public static String generateRandomEmail() {
        // Generate random username (8-12 characters)
        int usernameLength = 8 + random.nextInt(5); // 8 to 12
        String username = generateRandomString(usernameLength);

        // Common email domains
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com"};
        String domain = domains[random.nextInt(domains.length)];

        return username + "@" + domain;
    }

    public static String generateRandomPhoneNumber() {
        return generateRandomPhoneNumber(10);
    }

    public static String generateRandomPhoneNumber(Integer length) {
        if(length == null || length <= 0) return "";
        String allowedPrefixes = "35789";
        String phoneNumber = "0";

        phoneNumber += generateRandomString(allowedPrefixes, 1);

        if(length <= 2) return phoneNumber;

        phoneNumber += generateRandomString(allowedPrefixes, length - 2);

        return phoneNumber;
    }
}
