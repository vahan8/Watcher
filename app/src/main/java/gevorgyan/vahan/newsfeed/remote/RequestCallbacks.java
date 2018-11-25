package gevorgyan.vahan.newsfeed.remote;

import java.io.Serializable;

public interface RequestCallbacks extends Serializable {
    void onSuccess(Object response);

    void onFailure();
}
