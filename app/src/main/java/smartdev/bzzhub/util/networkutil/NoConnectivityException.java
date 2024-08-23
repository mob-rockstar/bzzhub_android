package smartdev.bzzhub.util.networkutil;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "Network is not connected";
    }
}
