package cz.vutbr.fit.distributedrepository.util;

public enum FileExtension {

    CAP(".cap"),
    PCAP(".pcap");

    private String extension;

    FileExtension(String extension) {
        this.extension = extension;
    }

    public String toString() {
        return this.extension;
    }

}
