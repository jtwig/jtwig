/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.functions.internal.string;

import com.lyncode.jtwig.functions.Function;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static org.hamcrest.CoreMatchers.equalTo;

public class UrlEncode implements Function {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(equalTo(1));

        if (arguments[0] == null)
            return null;
        try {
            if (arguments[0] instanceof Map)
                return encodeMap((Map) arguments[0]);
            return URLEncoder.encode(arguments[0].toString(), Charset.defaultCharset().displayName());
        } catch (UnsupportedEncodingException e) {
            throw new FunctionException(e);
        }
    }

    private String encodeMap(Map argument) throws FunctionException {
        List<String> pieces = new ArrayList<String>();
        for (Object key : argument.keySet()) {
            pieces.add(encode(key.toString()) + "=" + encode(argument.get(key).toString()));
        }
        return StringUtils.join(pieces, "&");
    }

    private String encode(String value) throws FunctionException {
        try {
            return URLEncoder.encode(value, Charset.defaultCharset().displayName());
        } catch (UnsupportedEncodingException e) {
            throw new FunctionException(e);
        }
    }
}
