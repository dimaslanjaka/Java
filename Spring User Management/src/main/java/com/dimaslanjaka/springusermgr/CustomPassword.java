package com.dimaslanjaka.springusermgr;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

// custom password encoder

public class CustomPassword implements PasswordEncoder {
    private final String SALT;

    public CustomPassword(String str) {
        this.SALT = str;
    }

    public CustomPassword() {
        this.SALT = "DEFAULT_SALT";
    }

    private @NotNull String a(String str) {
        int[] f9939a = new int[256];
        int[] iArr = new int[256];
        String str2 = this.SALT;
        int length = str2.length();
        for (int i = 0; i < 256; i++) {
            iArr[i] = str2.charAt(i % length);
            f9939a[i] = i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 256; i3++) {
            int[] iArr2 = f9939a;
            int i4 = iArr2[i3];
            i2 = ((i2 + i4) + iArr[i3]) % 256;
            iArr2[i3] = iArr2[i2];
            iArr2[i2] = i4;
        }
        StringBuilder sb = new StringBuilder();
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < str.length(); i7++) {
            i5 = (i5 + 1) % 256;
            int i8 = f9939a[i5];
            i6 = (i6 + i8) % 256;
            f9939a[i5] = f9939a[i6];
            f9939a[i6] = i8;
            sb.append(Character.toChars(f9939a[(f9939a[i5] + i8) % 256] ^ str.charAt(i7)));
        }
        return sb.toString();
    }

    public String decode(@NotNull CharSequence str) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < str.length()) {
            try {
                int i2 = i + 2;
                sb.append((char) Integer.parseInt(String.valueOf(str).substring(i, i2), 16));
                i = i2;
            } catch (Exception e3) {
                //
            }
        }
        return a(sb.toString());
    }

    @Override
    public String encode(CharSequence str) {
        String a3 = a(String.valueOf(str));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a3.length(); i++) {
            sb.append(String.format("%02x", (int) a3.charAt(i)));
        }
        return sb.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // return encode(rawPassword).contentEquals(encodedPassword);
        return decode(encodedPassword).contentEquals(rawPassword);
    }
}
