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

package com.lyncode.jtwig.util;

import java.io.File;

public class FilePath {
    public static FilePath path (String path) { return new FilePath(path); }
    public static FilePath parentOf (String path) {
        return new FilePath(new File(path).getParentFile());
    }

    private File file;

    public FilePath(String path) {
        this.file = new File(path);
    }

    public FilePath (String parent, String child) {
        this.file = new File(parent, child);
    }

    private FilePath(File parentFile) {
        this.file = parentFile;
    }

    public FilePath parent () {
        return new FilePath(this.file.getParentFile());
    }

    public FilePath append (String child) {
        this.file = new File(this.file, child);
        return this;
    }

    @Override
    public String toString () {
        return file.getPath().replace('\\', '/');
    }
}
