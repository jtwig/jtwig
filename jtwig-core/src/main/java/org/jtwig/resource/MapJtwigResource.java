package org.jtwig.resource;

import org.apache.commons.io.IOUtils;
import org.jtwig.exception.ResourceException;

import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-12-30
 * Time: 11:05
 */
public class MapJtwigResource implements JtwigResource {

    private Map<String, String> resources;

    private String name;

    public MapJtwigResource(String name, Map<String, String> resources) {
        this.name = name;
        this.resources = resources;
    }

    @Override
    public boolean exists() {
        return resources.containsKey(name);
    }

    @Override
    public String path() {
        return name;
    }

    @Override
    public InputStream retrieve() throws ResourceException {
        return IOUtils.toInputStream(resources.get(name));
    }

    @Override
    public JtwigResource resolve(String name) throws ResourceException {
        return new MapJtwigResource(name, resources);
    }

}
