package org.jtwig.addons.tag;

import com.google.common.base.Function;
import org.jtwig.Environment;
import org.jtwig.addons.Addon;
import org.jtwig.addons.AddonModel;
import org.jtwig.loader.Loader;

public abstract class TagAddon extends Addon {
    public TagAddon(Loader.Resource resource, Environment env) {
        super(resource, env);
    }

    @Override
    public AddonModel instance() {
        return new Tag(transformation());
    }

    protected abstract Function<String,String> transformation();
    protected abstract String keyword ();

    @Override
    public String beginKeyword() {
        return keyword();
    }

    @Override
    public String endKeyword() {
        return "end"+keyword();
    }
}
