package com.tuoling.tuolingat.compat;

import com.tuoling.tuolingat.compat.sound.NowSoundCompat;
import com.tuoling.tuolingat.compat.sound.OldSoundCompat;
import com.tuoling.tuolingat.compat.sound.SoundCompat;
import com.tuoling.tuolingat.compat.title.MidTitleCompat;
import com.tuoling.tuolingat.compat.title.NowTitleCompat;
import com.tuoling.tuolingat.compat.title.TitleCompat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// title / sound 跨版本兼容管理器：启动时按优先级（新 → 旧）注册，选取第一个 isMatch() 为 true 的实现缓存
// 支持运行时追加 / 移除实现，改动后立即按优先级重新选取
public class CompatManager {

    private final List<TitleCompat> titleCompatList = new ArrayList<>();
    private final List<SoundCompat> soundCompatList = new ArrayList<>();

    @Getter
    private TitleCompat titleCompat;
    @Getter
    private SoundCompat soundCompat;

    public CompatManager() {
        // 5 参 sendTitle：1.11 ~ 26.x；2 参 sendTitle（且无 5 参）：1.8 ~ 1.10
        registerTitle(new NowTitleCompat(), new MidTitleCompat());
        // 字符串音效名：1.9+；Sound 枚举：1.8
        registerSound(new NowSoundCompat(), new OldSoundCompat());
    }

    // 追加 title 实现（排在已有实现之后，即优先级更低），并重新选取
    public void registerTitle(TitleCompat... impls) {
        titleCompatList.addAll(Arrays.asList(impls));
        this.titleCompat = pickTitle();
    }

    public void unregisterTitle(TitleCompat... impls) {
        titleCompatList.removeAll(Arrays.asList(impls));
        this.titleCompat = pickTitle();
    }

    // 追加 sound 实现（排在已有实现之后，即优先级更低），并重新选取
    public void registerSound(SoundCompat... impls) {
        soundCompatList.addAll(Arrays.asList(impls));
        this.soundCompat = pickSound();
    }

    public void unregisterSound(SoundCompat... impls) {
        soundCompatList.removeAll(Arrays.asList(impls));
        this.soundCompat = pickSound();
    }

    private TitleCompat pickTitle() {
        for (TitleCompat t : titleCompatList) {
            if (t.isMatch()) return t;
        }
        // 1.8 ~ 26.x 内 Now / Mid 必有一个 isMatch，正常不会走到这里
        return null;
    }

    private SoundCompat pickSound() {
        for (SoundCompat s : soundCompatList) {
            if (s.isMatch()) return s;
        }
        // Now / Old 的 isMatch 严格互补，正常不会走到这里
        return null;
    }
}