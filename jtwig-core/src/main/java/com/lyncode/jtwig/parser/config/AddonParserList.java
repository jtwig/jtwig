package com.lyncode.jtwig.parser.config;

import com.lyncode.jtwig.addons.AddonParser;
import com.lyncode.jtwig.addons.concurrent.ConcurrentParser;
import com.lyncode.jtwig.addons.filter.FilterParser;
import com.lyncode.jtwig.addons.spaceless.SpacelessParser;

import java.util.ArrayList;
import java.util.List;

public class AddonParserList {
    private List<Class<? extends AddonParser>> addonParsers = new ArrayList<>();

    public AddonParserList () {
        this.withAddon(SpacelessParser.class)
                .withAddon(FilterParser.class)
                .withAddon(ConcurrentParser.class)
        ;
    }

    public AddonParserList withAddon (Class<? extends AddonParser> addon) {
        addonParsers.add(addon);
        return this;
    }

    public List<Class<? extends AddonParser>> list() {
        return addonParsers;
    }
}
