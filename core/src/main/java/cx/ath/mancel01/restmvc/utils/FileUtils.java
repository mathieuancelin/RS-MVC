/*
 *  Copyright 2010 Mathieu ANCELIN.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package cx.ath.mancel01.restmvc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Mathieu ANCELIN
 */
public class FileUtils {

    private static final int BUFFER_SIZE = 1024;

    public static String readFileAsString(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder data = readFromBufferedReader(reader);
            reader.close();
            return new String(data.toString().getBytes(), "utf-8");
        } catch (IOException ex) {
            throw new RuntimeException("File " + file + " not found.");
        }
    }

    private static StringBuilder readFromBufferedReader(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[BUFFER_SIZE];
        int numRead = 0;
        while((numRead = reader.read(buffer)) != -1) {
            builder.append(String.valueOf(buffer, 0, numRead));
            buffer = new char[BUFFER_SIZE];
        }
        return builder;
    }
}
