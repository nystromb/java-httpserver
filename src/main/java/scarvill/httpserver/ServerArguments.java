package scarvill.httpserver;

import java.util.Arrays;
import java.util.List;

public class ServerArguments {
    public static final int DEFAULT_PORT = 5000;
    public static final String DEFAULT_PUBLIC_DIRECTORY = "some directory";

    public int port = DEFAULT_PORT;
    public String publicDirectory = DEFAULT_PUBLIC_DIRECTORY;
    private List<String> argList;

    public ServerArguments(String[] args) {
        argList = Arrays.asList(args);
        setPortIfSpecified();
        setPublicDirectoryIfSpecified();
    }

    private void setPortIfSpecified() {
        int portFlagIndex = argList.indexOf("-p");

        if (flagIsFollowedByArgument(portFlagIndex)) {
            port = Integer.parseInt(argList.get(portFlagIndex + 1));
        }
    }

    private void setPublicDirectoryIfSpecified() {
        int directoryFlagIndex = argList.indexOf("-d");
        
        if (flagIsFollowedByArgument(directoryFlagIndex)) {
            publicDirectory = argList.get(directoryFlagIndex + 1);
        }
    }

    private boolean flagIsFollowedByArgument(int portFlagIndex) {
        return portFlagIndex != -1 && (portFlagIndex + 1) < argList.size();
    }
}
