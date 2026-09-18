package com.tuoling.tuolingat.compat.model;

import lombok.ToString;

@ToString
public class TitleModel {

    public String mainTitle;
    public String subTitle;
    public int fadeIn;
    public int stay;
    public int fadeOut;

    public TitleModel() {
    }

    // 直接解析 "标题,副标题,fadeIn,stay,fadeOut"，例："&6%sender%,&b好像在艾特你,10,30,10"
    public TitleModel(String raw) {
        parse(raw);
    }

    private void parse(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return;
        }
        String[] parts = raw.split(",", 5);
        if (parts.length > 0) this.mainTitle = parts[0].trim();
        if (parts.length > 1) this.subTitle  = parts[1].trim();
        if (parts.length > 2) this.fadeIn    = parseInt(parts[2], 10);
        if (parts.length > 3) this.stay      = parseInt(parts[3], 40);
        if (parts.length > 4) this.fadeOut   = parseInt(parts[4], 10);
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public TitleModel copy() {
        TitleModel t = new TitleModel();
        t.mainTitle = this.mainTitle;
        t.subTitle = this.subTitle;
        t.fadeIn = this.fadeIn;
        t.stay = this.stay;
        t.fadeOut = this.fadeOut;
        return t;
    }
}