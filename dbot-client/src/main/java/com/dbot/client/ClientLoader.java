/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.dbot.client;

import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.Supplier;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientLoader implements Supplier<Applet> {
    private static File PATCHED_CACHE = new File(DBot.DBOT_DIR, "patched.jar");

    private Applet applet = null;

    public Applet loadRs() {
        if (applet != null) {
            return applet;
        }

        try {
            applet = loadPatch();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return applet;
    }

    private Applet loadPatch() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        File file = PATCHED_CACHE;

        if (!file.exists()) {
            file = new File(PATCHED_CACHE.getName());
        }

        URL url = file.toURI().toURL();
        URLClassLoader rsClassLoader = new URLClassLoader(new URL[]{url}, ClientLoader.class.getClassLoader());

        ConfigLoader config = new ConfigLoader();
        config.fetch();

        String initialClass = config.getProperty(ConfigLoader.INITIAL_CLASS).replace(".class", "");
        Class<?> clientClass = rsClassLoader.loadClass(initialClass);

        Applet rs = (Applet) clientClass.newInstance();
        rs.setStub(new RSStub(config));

        return rs;

    }

    @SneakyThrows
    @Override
    public Applet get() {
        return loadRs();
    }
}
