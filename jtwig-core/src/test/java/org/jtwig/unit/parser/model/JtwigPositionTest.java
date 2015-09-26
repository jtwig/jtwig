package org.jtwig.unit.parser.model;

import org.jtwig.cache.TemplateCache;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.Template;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JtwigPositionTest {
    @Test
    public void rowAndColumn() throws Exception {
        JtwigPosition position = new JtwigPosition(null, 1, 2);

        assertEquals(1, position.getRow());
        assertEquals(2, position.getColumn());
    }
    
    @Test
    public void ensureCurrentTemplateKnowledge() throws Exception {
        // Create the resource
        Loader.Resource resource = mock(Loader.Resource.class);
        when(resource.getCacheKey()).thenReturn("test.twig");
        
        // Build the templates and cache
        Template tpl = new Template(null);
        Template.Compiled compiledTpl = new Template.Compiled(null, null, null, null, Renderable.NOOP, null);
        TemplateCache cache = mock(TemplateCache.class);
        when(cache.getParsed("test.twig")).thenReturn(tpl);
        when(cache.getCompiled("test.twig")).thenReturn(compiledTpl);
        
        
        // Build the compile and render contexts
        CompileContext compileCtx = mock(CompileContext.class);
        when(compileCtx.cache()).thenReturn(cache);
        RenderContext renderCtx = mock(RenderContext.class);
        when(renderCtx.cache()).thenReturn(cache);
        
        // Test the position
        JtwigPosition position = new JtwigPosition(resource, 1, 1);
        assertEquals(tpl, position.getTemplate(compileCtx));
        assertEquals(compiledTpl, position.getCompiledTemplate(compileCtx));
        assertEquals(tpl, position.getTemplate(renderCtx));
        assertEquals(compiledTpl, position.getCompiledTemplate(renderCtx));
    }
    
}
