package com.tuoling.tuolingat.compat.model;

import lombok.ToString;

@ToString
public class SoundModel {

    public String name;
    public float volume = 1.0f;
    public float pitch = 1.0f;

    public SoundModel() {
    }

    // 直接解析 "音效名,音量,音调"，例："ENTITY_CAT_AMBIENT,1,1"
    public SoundModel(String raw) {
        parse(raw);
    }

    private void parse(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return;
        }
        String[] parts = raw.split(",");
        this.name = parts[0].trim();

        if (parts.length > 1) {
            try {
                this.volume = Float.parseFloat(parts[1].trim());
            } catch (NumberFormatException ignored) {
            }
        }
        if (parts.length > 2) {
            try {
                this.pitch = Float.parseFloat(parts[2].trim());
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public SoundModel copy() {
        SoundModel s = new SoundModel();
        s.name = this.name;
        s.volume = this.volume;
        s.pitch = this.pitch;
        return s;
    }
}