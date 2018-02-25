package cz.vutbr.fit.stats;

import java.util.Date;

public class FileStats {

    private String filename;
    private Date startTime;
    private Date endTime;
    private long duration;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public static class Builder {

        private FileStats fileStats;

        public Builder() {
            fileStats = new FileStats();
        }

        public Builder filename(String filename) {
            fileStats.setFilename(filename);
            return this;
        }

        public Builder startTime(Date startTime) {
            fileStats.setStartTime(startTime);
            return this;
        }

        public Builder endTime(Date endTime) {
            fileStats.setEndTime(endTime);
            return this;
        }

        public Builder duration(long duration) {
            fileStats.setDuration(duration);
            return this;
        }

        public FileStats build() {
            return fileStats;
        }

    }

}
