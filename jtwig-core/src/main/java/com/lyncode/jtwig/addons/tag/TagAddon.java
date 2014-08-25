package com.lyncode.jtwig.addons.tag;

import com.google.common.base.Function;
import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.addons.AddonModel;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public abstract class TagAddon extends Addon {
    public TagAddon(JtwigResource resource, ParserConfiguration configuration) {
        super(resource, configuration);
    }

    @Override
    public AddonModel instance() {
        return new Tag(transformation());
    }

    protected abstract Function<String,String> transformation();
}
