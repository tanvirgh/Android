/*
 * Copyright 2014 Matthias Einwag
 *
 * The jawampa authors license this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ws.wamp.jawampa.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ws.wamp.jawampa.ApplicationError;

public abstract class UriValidator {
    
    static final Pattern STRICT_URI_NOT_EMPTY = Pattern.compile("^([0-9a-z_]+\\.)*([0-9a-z_]+)$");
    static final Pattern STRICT_URI_MAYBE_EMPTY = Pattern.compile("^(([0-9a-z_]+\\.)|\\.)*([0-9a-z_]+)?$");
    
    /**
     * Checks a WAMP Uri for validity.
     * @param uri The uri that should be validated
     * @param mayContainEmptyParts Whether the Uri may contain empty parts
     * @return true if Uri is valid, false otherwise
     */
    public static boolean tryValidate(String uri, boolean mayContainEmptyParts) {
        if (uri == null) return false;
        
        Matcher m;
        if (mayContainEmptyParts)
            m = STRICT_URI_MAYBE_EMPTY.matcher(uri);
        else
            m = STRICT_URI_NOT_EMPTY.matcher(uri);
        
        if (m.matches()) return true;
        else return false;
    }
    
    /**
     * Checks a WAMP Uri for validity.
     * @param uri The uri that should be validated
     * @return true if Uri is valid, false otherwise
     */
    public static boolean tryValidate(String uri) {
        return tryValidate(uri, false);
    }
    
    /**
     * Checks a WAMP Uri for validity.
     * @param uri The uri that should be validated
     * @param mayContainEmptyParts Whether the Uri may contain empty parts
     * @throws an ApplicationError if the Uri is not valid
     */
    public static void validate(String uri, boolean mayContainEmptyParts) throws ApplicationError {
        boolean isValid = tryValidate(uri, mayContainEmptyParts);
        if (!isValid)
            throw new ApplicationError(ApplicationError.INVALID_URI);
    }
    
    /**
     * Checks a WAMP Uri for validity.
     * @param uri The uri that should be validated
     * @throws an ApplicationError if the Uri is not valid
     */
    public static void validate(String uri) throws ApplicationError {
        boolean isValid = tryValidate(uri);
        if (!isValid)
            throw new ApplicationError(ApplicationError.INVALID_URI);
    }
}
