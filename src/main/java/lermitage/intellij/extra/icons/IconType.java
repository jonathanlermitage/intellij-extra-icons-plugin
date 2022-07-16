// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

/** The type of icon: the path of a bundled icon, or a Base64 encoded user icon stored in config. */
public enum IconType {
    /** The relative path of a bundled icon. */
    PATH,
    /** SVG user icon stored in config as Base64. */
    SVG,
    /** PNG user icon stored in config as Base64. */
    IMG
}
