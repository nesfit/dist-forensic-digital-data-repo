package cz.vutbr.fit.distributedrepository.service.pcap.parser;

@FunctionalInterface
public interface OnFailureCallback {

    public void doOnFailure(Throwable throwable);

}
