package com.lyncode.jtwig.addons.tag;

import com.google.common.base.Function;
import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.compile.config.CompileConfiguration;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.content.model.compilable.Text;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import com.lyncode.jtwig.resource.StringJtwigResource;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TagAddonTest {
    private Function<String, String> transformation = mock(Function.class);
    private TagAddon underTest = new TagAddon(transformation);
    private CompileContext compileContext;
    private ByteArrayOutputStream output;
    private RenderContext renderContext;

    @Before
    public void setUp() throws Exception {
        output = new ByteArrayOutputStream();
        renderContext = RenderContext.create(new RenderConfiguration(), new JtwigContext(), output);
        compileContext = new CompileContext(new StringJtwigResource(""), new JtwigParser(), new CompileConfiguration());
    }

    @Test
    public void appliesTheTransformationToTheContent() throws Exception {
        given(transformation.apply("Hello")).willReturn("A");

        underTest
                .withContent(new Sequence().add(new Text("Hello")))
                .compile(compileContext)
                .render(renderContext);

        assertThat(theResult(), is(equalTo("A")));
        verify(transformation).apply("Hello");
    }

    private String theResult() {
        return output.toString();
    }
}