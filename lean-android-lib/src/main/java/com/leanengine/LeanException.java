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

    LeanException(LeanError leanError) {
        this.error = leanError;
    }

    LeanException(LeanError.Type type) {
        this.error = new LeanError(type);
    }

    LeanException(LeanError.Type type, String message) {
        this.error = new LeanError(type, message);
    }

    /**
     * Returns the wrapped {@link LeanError}.
     *
     * @return The {@link LeanError} that this exception is wrapping.
     */
    public LeanError getError() {
        return error;
    }
}
