package io.github.glynch.jollama.support;

import okhttp3.Response;

public interface ResponseStatusErrorHandler {

    void handleError(Response response);

}
