package com.tuoling.tuolingat.compat.sound;

import java.util.Locale;

// 音效名格式转换层：config 里写枚举名(ENTITY_CAT_AMBIENT)或资源路径(entity.cat.ambient)都自动适配当前版本
public final class SoundNameUtil {

    private SoundNameUtil() {
    }

    // 转成 1.13+ 的资源路径：小写 + 下划线换点号
    public static String toResourcePath(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.trim().toLowerCase(Locale.ROOT).replace('_', '.');
    }

    // 转成 1.9 ~ 1.12 的枚举名：去掉命名空间 + 大写 + 点号换下划线
    public static String toEnumName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        String raw = name.trim();
        int namespace = raw.indexOf(':');
        if (namespace >= 0) {
            raw = raw.substring(namespace + 1);
        }
        return raw.toUpperCase(Locale.ROOT).replace('.', '_');
    }
}