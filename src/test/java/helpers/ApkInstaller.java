package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApkInstaller {

    public static void reinstall(String udid, String appPackage, String apkPath) {
        String apkAbsolute = resolveApkToAbsolutePath(apkPath);

        adbRunIgnoreExitCode(udid, "uninstall", appPackage);
        adbRun(udid, "install", "-r", "-g", apkAbsolute);
    }

    private static String resolveApkToAbsolutePath(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("APK path is empty");
        }

        File f = new File(path);
        if (f.isAbsolute()) return f.getAbsolutePath();

        URL res = ApkInstaller.class.getClassLoader().getResource(path);
        if (res != null) {
            try {
                if (!"file".equalsIgnoreCase(res.getProtocol())) {
                    return copyResourceToTemp(path);
                }
                return Path.of(res.toURI()).toFile().getAbsolutePath();
            } catch (Exception e) {
            }
        }
        return new File(System.getProperty("user.dir"), path).getAbsolutePath();
    }

    private static String copyResourceToTemp(String resourcePath) {
        try (var in = ApkInstaller.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);

            Path tmp = Files.createTempFile("apk-", "-" + new File(resourcePath).getName());
            Files.copy(in, tmp, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return tmp.toFile().getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy resource APK to temp: " + resourcePath, e);
        }
    }

    private static void adbRun(String udid, String... args) {
        int code = adbRunInternal(udid, false, args);
        if (code != 0) throw new RuntimeException("adb failed, exitCode=" + code);
    }

    private static void adbRunIgnoreExitCode(String udid, String... args) {
        adbRunInternal(udid, true, args);
    }

    private static int adbRunInternal(String udid, boolean ignoreExitCode, String... args) {
        try {
            String[] cmd = new String[args.length + 3];
            cmd[0] = "adb";
            cmd[1] = "-s";
            cmd[2] = udid;
            System.arraycopy(args, 0, cmd, 3, args.length);

            Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println("[adb] " + line);
                }
            }

            int code = p.waitFor();
            if (!ignoreExitCode && code != 0) return code;
            return code;

        } catch (Exception e) {
            throw new RuntimeException("Failed to run adb command", e);
        }
    }
}