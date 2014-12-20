package org.jtwig.spring;

import com.google.common.base.Function;
import org.jtwig.addons.Addon;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.parser.config.TagSymbols;

import java.nio.charset.Charset;
import org.jtwig.cache.TemplateCache;

public class XmlConfiguration extends JtwigConfiguration {
    public XmlConfiguration(TemplateCache templateCache) {
        super(templateCache);
    }
    public XmlConfiguration (FunctionRepository functionRepository,
            TemplateCache templateCache) {
        super(functionRepository, templateCache);
    }

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
