package de.mm.portfoliooptimizerclassic;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * The licence texts that travel inside the APK.
 *
 * <p>{@code app/build.gradle.kts} copies them from the repository root into the
 * assets under {@code legal/}, so the app shows exactly what the repository
 * ships. They have to be in the APK because the APK is passed around without the
 * repository: PolyForm Noncommercial and Apache 2.0 both require recipients to
 * receive the licence text and the notices.</p>
 */
final class LegalNotices {

    private static final String ASSET_DIR = "legal/";
    private static final String REQUIRED_NOTICE_PREFIX = "Required Notice:";

    /** Asset names in the order the full texts are shown. */
    static final String[] FILES = {
            "LICENSE.txt",
            "THIRD-PARTY-NOTICES.md",
            "Apache-2.0.txt",
            "commons-math3-NOTICE.txt",
            "commons-math3-LICENSE.txt",
    };

    private LegalNotices() {
    }

    /** The {@code Required Notice:} line of the licence, or an empty string. */
    static String requiredNotice(Context context) {
        try {
            return requiredNoticeIn(read(context, FILES[0]));
        } catch (IOException e) {
            return "";
        }
    }

    /** First line starting with {@code Required Notice:}, or an empty string. */
    static String requiredNoticeIn(String licenceText) {
        if (licenceText == null) return "";
        for (String line : licenceText.split("\\R")) {
            if (line.startsWith(REQUIRED_NOTICE_PREFIX)) return line.trim();
        }
        return "";
    }

    /** Every licence text, one after the other, each under its file name. */
    static String fullText(Context context) {
        StringBuilder all = new StringBuilder();
        for (String name : FILES) {
            if (all.length() > 0) all.append("\n\n");
            all.append("==== ").append(name).append(" ====\n\n");
            try {
                all.append(read(context, name));
            } catch (IOException e) {
                all.append("(could not be read: ").append(e.getMessage()).append(')');
            }
        }
        return all.toString();
    }

    private static String read(Context context, String name) throws IOException {
        try (InputStream in = context.getAssets().open(ASSET_DIR + name);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder text = new StringBuilder();
            char[] buffer = new char[8192];
            int n;
            while ((n = reader.read(buffer)) != -1) text.append(buffer, 0, n);
            return text.toString();
        }
    }
}
