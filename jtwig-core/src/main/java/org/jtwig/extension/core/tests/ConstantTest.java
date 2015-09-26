/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.core.tests;

import static java.lang.Class.forName;
import org.jtwig.extension.api.test.AbstractTest;
import org.jtwig.extension.api.test.TestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://twig.sensiolabs.org/doc/tests/constant.html
 * 
 * According to the Twig documentation, the constant test determines if the left
 * side argument is the same value as the given constant. 
 */
public class ConstantTest extends AbstractTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantTest.class);

    @Override
    public boolean evaluate(Object left, Object... args) throws TestException {
        if (args.length == 0) {
            throw new TestException("Constant test requires a constant to compare to.");
        }
        
        String constant = args[0].toString();
        
        int constantNamePosition = constant.lastIndexOf(".");
        if (constantNamePosition == -1) {
            LOGGER.warn("Invalid constant specified: {}", constant);
            return false; // Twig doesn't throw an exception here
        }

        String className = constant.substring(0, constantNamePosition);
        String constantName = constant.substring(constantNamePosition + 1);

        try {
            return left.equals(forName(className).getDeclaredField(constantName).get(null));
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            LOGGER.warn("Constant '{}' does not exist", constant);
            return false;
        }
    }

}