package su.mvitsvk.client.service;

import su.mvitsvk.common.NetworkObject;

public interface NetworkService {

    public void connect();
    public void disconect();
    public void sendCMD (NetworkObject obj);
    public boolean getIsrun();
    public void setIsrun(boolean isrun);

}
