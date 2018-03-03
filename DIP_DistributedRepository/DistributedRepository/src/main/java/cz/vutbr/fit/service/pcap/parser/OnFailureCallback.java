package cz.vutbr.fit.service.pcap.parser;

@FunctionalInterface
public interface OnFailureCallback {

    public void doOnFailure(Throwable throwable);

}
