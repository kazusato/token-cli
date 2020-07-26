package dev.kazusato.tokencli.sub;

import dev.kazusato.tokencli.command.GenerateTokenTool;
import picocli.CommandLine;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@CommandLine.Command(name = "storekey")
public class StoreKey implements Runnable {

    @Override
    public void run() {
        try {
            final SecretKey key = KeyGenerator.getInstance("HmacSHA256").generateKey();
            final byte[] base64Key = Base64.getEncoder().encode(key.getEncoded());
            storeEncryptionKey(base64Key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void storeEncryptionKey(byte[] base64KeyBytes) {
        Path file = GenerateTokenTool.KEYFILE.toPath();

        Set<OpenOption> options = new HashSet<OpenOption>();
        options.add(StandardOpenOption.CREATE_NEW);
        options.add(StandardOpenOption.APPEND);

        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

        try (SeekableByteChannel sbc = Files.newByteChannel(file, options, attr)) {
            ByteBuffer buf = ByteBuffer.wrap(base64KeyBytes);
            sbc.write(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
