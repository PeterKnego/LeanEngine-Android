/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import java.io.IOException;

/**
 * Exception wrapping the {@link LeanError}.
 */
public class LeanException extends IOException {

    private LeanError error;
    private String message;

     public LeanException(LeanError leanError) {
        this.error = leanError;
    }

    public LeanException(LeanError.Error error) {
        this.error = new LeanError(error);
    }

     public LeanException(LeanError.Error error, String message) {
         this.error = new LeanError(error);
         this.message = message;
     }

    public LeanError getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
