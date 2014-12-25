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

package org.jtwig.loader.impl;

import java.util.ArrayList;
import java.util.List;
import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;

/**
 * The chain loader delegates resource management to one or more child loaders.
 * The order in which child loaders are added determines the order in which they
 * are called.
 */
public class ChainLoader extends Loader {
    private List<Loader> loaders = new ArrayList<>();
    
    public ChainLoader(final List<Loader> loaders) {
        this.loaders = loaders;
    }

    @Override
    public boolean exists(final String name) throws ResourceException {
        for (Loader loader : loaders) {
            if (loader.exists(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Resource get(final String name) throws ResourceException {
        for (Loader loader : loaders) {
            final Resource r = loader.get(name);
            if (r != null) {
                return r;
            }
        }
        return null;
    }
    
}