package com.lyncode.jtwig.unit.render;

import com.google.common.base.Optional;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.resolver.api.FunctionResolver;
import com.lyncode.jtwig.functions.resolver.model.Executable;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RenderContextTest {
    private FunctionResolver functionResolver = mock(FunctionResolver.class);
    private RenderContext underTest = RenderContext.create(new RenderConfiguration(), new JtwigModelMap(), functionResolver, new ByteArrayOutputStream());

    @Test(expected = FunctionException.class)
    public void testInvocationTargetException () throws Exception {
        Executable executable = mock(Executable.class);
        when(functionResolver.resolve(anyString(), any(InputParameters.class)))
                .thenReturn(Optional.of(executable));

        doThrow(new IllegalAccessException()).when(executable).execute();

        underTest.executeFunction("nome", new InputParameters());
    }
}