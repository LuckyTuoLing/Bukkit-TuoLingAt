package com.tuoling.tuolingat.message;

// 消息解析模式：决定一次解析启用哪些加工项（自定义占位符替换、颜色转义、PlaceholderAPI）
public enum ParseMode {

    RAW(false, false),

    CUSTOM(true, false),

    PAPI(false, true),

    CUSTOM_PAPI(true, true);

    // 是否替换自定义占位符 %key%
    final boolean enableCustom;

    // 是否解析 PlaceholderAPI
    final boolean enablePapi;

    ParseMode(boolean enableCustom, boolean enablePapi) {
        this.enableCustom = enableCustom;
        this.enablePapi = enablePapi;
    }
}