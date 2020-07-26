package dev.kazusato.tokencli.command;

import dev.kazusato.tokencli.sub.GenerateToken;
import dev.kazusato.tokencli.sub.StoreKey;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "gentoken", subcommands = {StoreKey.class, GenerateToken.class})
public class GenerateTokenTool implements Runnable {

    public static final File KEYFILE = new File(new File(System.getProperty("user.home")), ".gentoken");

    @Override
    public void run() {
        System.out.println("This tool generates a JWT token.");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GenerateTokenTool()).execute(args);
        System.exit(exitCode);
    }

}
