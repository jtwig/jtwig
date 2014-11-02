package com.lyncode.jtwig.spring;

import com.google.common.base.Function;
import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.parser.config.TagSymbols;

import java.nio.charset.Charset;

public class XmlConfiguration extends JtwigConfiguration {
    public void setRenderStrictMode (boolean activate) {
        render().strictMode(activate);
    }

    public void setJsonMapper (Function<Object, String> jsonMapper) {
        render().jsonMapper(jsonMapper);
    }

    public void setCharset (String charsetName) {
        render().charset(Charset.forName(charsetName));
    }

    public void setLogNonStrictMode (boolean logOnNonStrictMode) {
        render().logNonStrictMode(logOnNonStrictMode);
    }

    public void setExtraFunctionContainers (Object[] instances) {
        for (Object instance : instances) {
            render().functionRepository().include(instance);
        }
    }

    public void setTagSymbols (String type) {
        parse().withSymbols(TagSymbols.valueOf(type));
    }

    public void setExtraSyntaticAddons (Class<? extends Addon>[] addons) {
        for (Class<? extends Addon> addon : addons) {
            parse().addons().withAddon(addon);
        }
    }
}
