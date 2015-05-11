package org.jtwig.parser.config;

import org.jtwig.addons.Addon;
import org.jtwig.addons.concurrent.ConcurrentAddon;
import org.jtwig.addons.filter.FilterAddon;
import org.jtwig.addons.spaceless.SpacelessAddon;

import java.util.ArrayList;
import java.util.List;

public class AddonParserList {
    private List<Class<? extends Addon>> addonParsers = new ArrayList<>();

    public AddonParserList () {
        this.withAddon(SpacelessAddon.class)
                .withAddon(FilterAddon.class)
                .withAddon(ConcurrentAddon.class)
        ;
    }

    public AddonParserList withAddon (Class<? extends Addon> addon) {
        addonParsers.add(addon);
        return this;
    }

    public List<Class<? extends Addon>> list() {
        return addonParsers;
    }
}
