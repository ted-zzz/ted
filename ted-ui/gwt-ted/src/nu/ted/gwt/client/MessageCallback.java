package nu.ted.gwt.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class MessageCallback<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        System.out.println(caught);
        Window.alert("An error occurred: " + caught.getMessage());
    }

}
