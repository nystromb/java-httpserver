package scarvill.httpserver.cobspec;

import java.util.Arrays;
import java.util.List;

public class CommandLineArguments {
    public static final int DEFAULT_PORT = 5000;
    public static final String DEFAULT_PUBLIC_DIRECTORY = "./cob_spec/public";

    private final String PORT_FLAG = "-p";
    private final String DIRECTORY_FLAG = "-d";

    private int port = DEFAULT_PORT;
    private String publicDirectory = DEFAULT_PUBLIC_DIRECTORY;
    private List<String> argList;

    public CommandLineArguments(String[] args) {
        argList = Arrays.asList(args);
        setPortIfSpecified();
        setPublicDirectoryIfSpecified();
    }

    public int getPort() {
        return port;
    }

    public String getPublicDirectory() {
        return publicDirectory;
    }

    private void setPortIfSpecified() {
        int portFlagIndex = argList.indexOf(PORT_FLAG);

        if (flagIsFollowedByArgument(portFlagIndex)) {
            port = Integer.parseInt(argList.get(portFlagIndex + 1));
        }
    }

    private void setPublicDirectoryIfSpecified() {
        int directoryFlagIndex = argList.indexOf(DIRECTORY_FLAG);

        if (flagIsFollowedByArgument(directoryFlagIndex)) {
            publicDirectory = argList.get(directoryFlagIndex + 1);
        }
    }

    private boolean flagIsFollowedByArgument(int portFlagIndex) {
        return portFlagIndex != -1 && (portFlagIndex + 1) < argList.size();
    }
}
