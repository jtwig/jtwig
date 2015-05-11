/**
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

package org.jtwig.exception;

public class ParseException extends JtwigException {
    private String expression;

    public ParseException(Throwable cause) {
        super(cause);
    }
    public ParseException(String message) {
        super(message);
    }

    public void setExpression(String expression) {
        String result = expression.substring(expression.indexOf("input position"));
        this.expression = result.substring(0, 1).toUpperCase() + result.substring(1);
        this.expression = this.expression
                .substring(0, this.expression.indexOf("org.jtwig.exception.ParseBypassException"));
    }

    @Override
    public String getMessage() {
        if (expression != null)
            return super.getMessage() + "\n" + "Explanation: " + expression;
        else
            return super.getMessage();
    }
}
