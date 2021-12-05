/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jamalam360
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.notify.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.jamalam360.notify.NotifyModInit;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ContactInformation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class Utils {
    static final Gson GSON = new Gson();

    public static String getContactWithContent(ContactInformation information, String content) {
        for (Map.Entry<String, String> entry : information.asMap().entrySet()) {
            if (entry.getValue().contains(content)) {
                return entry.getValue();
            }
        }

        return "";
    }

    public static String getKeyFromJson(JsonReader reader, String key) throws IOException {
        String name = reader.nextName();
        while (!name.equals(key) && reader.hasNext()) {
            reader.skipValue();
            name = reader.nextName();
        }

        return reader.nextString();
    }

    public static JsonReader openJsonFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        return GSON.newJsonReader(new InputStreamReader(url.openStream()));
    }

    public static boolean isIgnored(ModContainer mod) {
        return NotifyModInit.getConfig().blacklist.contains(mod.getMetadata().getId()) || mod.getMetadata().getAuthors().stream().anyMatch(p -> p.getName().equals("FabricMC")); // No FAPI :crab:
    }

    public static int getLoadedNonIgnoredModCount() {
        int count = 0;

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            if (!isIgnored(mod)) {
                count++;
            }
        }

        return count;
    }
}
