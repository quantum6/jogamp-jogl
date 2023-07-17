/**
 * Copyright 2018 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */

package jogamp.nativewindow;

import java.io.File;
import java.security.PrivilegedAction;

import com.jogamp.common.util.SecurityUtil;

/**
 * Heuristics about Broadcom (BCM) VideoCore (VC) existence and usage
 */
public class BcmVCArtifacts {
    static final boolean hasVCLib;
    static final boolean hasVC4ModLocation;
    static final boolean hasDriCard0File;

    static {
        final File vcLibLocation = new File(
            "/opt/vc/lib/libbcm_host.so");
        final File vc4ModLocation = new File(
            "/sys/module/vc4");
        final File driCard0Location = new File(
            "/dev/dri/card0");
        final boolean[] res = new boolean [3];
        SecurityUtil.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    res[0] = vcLibLocation.isFile();
                    res[1] = vc4ModLocation.isDirectory();
                    res[2] = driCard0Location.exists(); // not a normal file
                    return null;
                } } );
        hasVCLib = res[0];
        hasVC4ModLocation = res[1];
        hasDriCard0File = res[2];
    }

    /**
     * @return True if proprietary BCM VC IV is probably being present
     */
    public static final boolean guessVCIVPresent(final boolean debug) {
        if( debug ) {
            System.err.println("guessVCIVPresent: hasVCLib "+hasVCLib);
        }
        return hasVCLib;
    }
    /**
     * @return True if proprietary BCM VC IV is probably being used and not Xorg drivers
     */
    public static final boolean guessVCIVUsed(final boolean debug) {
        if( debug ) {
            System.err.println("guessVCIVUsed: hasVCLib = "+hasVCLib+" && !hasVC4ModLocation = !"+hasVC4ModLocation+" && !hasDriCard0File = !"+hasDriCard0File);
        }
        return hasVCLib && !hasVC4ModLocation && !hasDriCard0File;
    }
}
