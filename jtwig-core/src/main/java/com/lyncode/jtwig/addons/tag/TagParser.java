package com.lyncode.jtwig.addons.tag;

import com.google.common.base.Function;
import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.addons.AddonParser;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;

public abstract class TagParser extends AddonParser {
    public TagParser(JtwigResource resource, ParserConfiguration configuration) {
        super(resource, configuration);
    }

    @Override
    public Addon instance() {
        return new TagAddon(transformation());
    }

    protected abstract Function<String,String> transformation();
}
