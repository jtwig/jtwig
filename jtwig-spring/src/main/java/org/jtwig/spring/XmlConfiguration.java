package org.jtwig.spring;

import org.jtwig.addons.Addon;
import org.jtwig.parser.config.TagSymbols;

import org.jtwig.Environment;

public class XmlConfiguration extends Environment {

    public void setExtraFunctionContainers(Object[] instances) {
        for (Object instance : instances) {
            getFunctionRepository().include(instance);
        }
    }

    public void setSymbols(String type) {
        setSymbols(TagSymbols.valueOf(type));
    }

    public void setExtraSyntaticAddons(Class<? extends Addon>[] addons) {
        for (Class<? extends Addon> addon : addons) {
            getAddonParserList().withAddon(addon);
        }
    }
}
