package dev.kazusato.tokencli.sub;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.kazusato.tokencli.command.GenerateTokenTool;
import picocli.CommandLine;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@CommandLine.Command(name = "generate")
public class GenerateToken implements Runnable {

    @CommandLine.Option(names = {"-d", "--days"}, required = true, description = "Days to expire.")
    private int daysToExpire;

    @Override
    public void run() {
        byte[] encKey = readEncryptionKey();
        final Algorithm algorithm = Algorithm.HMAC256(encKey);
        final Date now = new Date();
        final Date expiresAt = new Date(now.getTime() + daysToExpire * 24 * 60 * 60 * 1000L);

        final String jwt = JWT.create()
                .withIssuer("kazusato")
                .withAudience("kazusato")
                .withIssuedAt(now)
                .withNotBefore(now)
                .withExpiresAt(expiresAt)
                .withClaim("custom_type", "AAA")
                .sign(algorithm);

        System.out.println(jwt);
    }

    private byte[] readEncryptionKey() {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(GenerateTokenTool.KEYFILE))) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) > 0) {
                baos.write(buf, 0, len);
            }
            return Base64.getDecoder().decode(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
