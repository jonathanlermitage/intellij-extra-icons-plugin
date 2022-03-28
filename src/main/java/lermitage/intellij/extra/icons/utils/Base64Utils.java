// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import java.util.Base64;

public class Base64Utils {

    /** A Base64 thread-safe decoder. */
    public static final Base64.Decoder B64_DECODER = Base64.getDecoder();

    /** A Base64 thread-safe encoder. */
    public static final Base64.Encoder B64_ENCODER = Base64.getEncoder();
}
