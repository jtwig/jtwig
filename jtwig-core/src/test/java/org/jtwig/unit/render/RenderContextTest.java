package org.jtwig.unit.render;

import com.google.common.base.Optional;
import java.io.ByteArrayOutputStream;
import org.jtwig.Environment;
import org.jtwig.JtwigModelMap;
import org.jtwig.functions.exceptions.FunctionException;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.resolver.api.FunctionResolver;
import org.jtwig.functions.resolver.model.Executable;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RenderContextTest {
    private FunctionResolver functionResolver = mock(FunctionResolver.class);
    private RenderContext underTest = RenderContext.create(new Environment(), new JtwigModelMap(), functionResolver, new ByteArrayOutputStream());
    

    @Test(expected = FunctionException.class)
    public void testInvocationTargetException () throws Exception {
        Executable executable = mock(Executable.class);
        when(functionResolver.resolve(anyString(), any(InputParameters.class)))
                .thenReturn(Optional.of(executable));

        doThrow(new IllegalAccessException()).when(executable).execute();

        underTest.executeFunction("nome", new InputParameters());
    }
}
