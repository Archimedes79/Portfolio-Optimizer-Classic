package de.mm.portfoliooptimizerclassic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Guards the licence obligations that are easy to lose without noticing: the texts
 * the app packages must exist, carry what the licences require, and the app must
 * not pretend to be a web browser towards Yahoo.
 *
 * <p>Unit tests run in the {@code app/} directory, so the repository root is one
 * level up.</p>
 */
public class LegalComplianceTest {

    private static final File ROOT = new File("..");

    /** The repository file that {@code app/build.gradle.kts} packs under this asset name. */
    private static File repoFileFor(String assetName) {
        switch (assetName) {
            case "LICENSE.txt":
                return new File(ROOT, "LICENSE");
            case "THIRD-PARTY-NOTICES.md":
                return new File(ROOT, "THIRD-PARTY-NOTICES.md");
            default:
                return new File(new File(ROOT, "LICENSES"), assetName);
        }
    }

    private static String read(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }

    @Test
    public void requiredNoticeIsTheLineThatStartsWithIt() {
        String text = "Intro\r\nRequired Notice: Copyright (c) 2026 Someone (https://example.com)\r\nMore";
        assertEquals("Required Notice: Copyright (c) 2026 Someone (https://example.com)",
                LegalNotices.requiredNoticeIn(text));
    }

    @Test
    public void requiredNoticeIsEmptyWhenAbsent() {
        assertEquals("", LegalNotices.requiredNoticeIn("no notice here"));
        assertEquals("", LegalNotices.requiredNoticeIn(null));
    }

    @Test
    public void licenceFileCarriesARequiredNotice() throws IOException {
        // PolyForm Noncommercial obliges recipients of any copy to receive this line.
        assertFalse(LegalNotices.requiredNoticeIn(read(repoFileFor("LICENSE.txt"))).isEmpty());
    }

    @Test
    public void everyPackagedLegalFileExistsInTheRepository() {
        for (String name : LegalNotices.FILES) {
            File file = repoFileFor(name);
            assertTrue(name + " is packed into the APK but missing at " + file, file.isFile());
            assertTrue(name + " is empty", file.length() > 0);
        }
    }

    @Test
    public void apacheTextIsTheCompleteLicence() throws IOException {
        String text = read(repoFileFor("Apache-2.0.txt"));
        assertTrue(text.contains("Apache License"));
        assertTrue(text.contains("Version 2.0, January 2004"));
        assertTrue(text.contains("END OF TERMS AND CONDITIONS"));
    }

    @Test
    public void commonsMathNoticesAreComplete() throws IOException {
        // The jar's own copies are stripped from the APK, so these must ship instead.
        String notice = read(repoFileFor("commons-math3-NOTICE.txt"));
        assertTrue(notice.contains("Orekit"));

        String licence = read(repoFileFor("commons-math3-LICENSE.txt"));
        assertTrue(licence.contains("Minpack"));
        assertTrue(licence.contains("Makoto Matsumoto"));
        assertTrue(licence.contains("Ernst Hairer"));
        assertTrue(licence.contains("Frances Y. Kuo"));
    }

    @Test
    public void thirdPartyNoticesCarryTheMinpackAcknowledgement() throws IOException {
        String text = read(repoFileFor("THIRD-PARTY-NOTICES.md"));
        assertTrue(text.contains("University of Chicago, as Operator of Argonne National"));
        assertTrue(text.contains("Orekit"));
    }

    @Test
    public void userAgentSaysWhatTheAppIsAndDoesNotPoseAsABrowser() {
        String agent = YahooFinanceService.USER_AGENT;
        assertTrue(agent.startsWith("PortfolioOptimizerClassic"));
        assertTrue(agent.contains("github.com/Archimedes79/Portfolio-Optimizer-Classic"));
        assertFalse(agent.contains("Mozilla"));
        assertFalse(agent.contains("Chrome"));
    }
}
