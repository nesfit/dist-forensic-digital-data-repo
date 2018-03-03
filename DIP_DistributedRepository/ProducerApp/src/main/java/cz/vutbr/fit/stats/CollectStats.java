package cz.vutbr.fit.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CollectStats {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectStats.class);

    private Map<UUID, FileStats> fileStatsMap = new HashMap<>();
    private Date lastEndTime;
    private int countOfFiles;
    private int receivedResponses;
    private int totalTime;

    private static volatile CollectStats instance;

    private CollectStats() {
    }

    public static CollectStats getInstance() {
        if (instance == null)
            synchronized (CollectStats.class) {
                if (instance == null)
                    instance = new CollectStats();
            }
        return instance;
    }

    public void appendFile(UUID uuid, FileStats fileStats) {
        fileStatsMap.put(uuid, fileStats);
    }

    public void setEndTime(UUID uuid, Date endTime) {
        FileStats fileStats = fileStatsMap.get(uuid);

        if (fileStats == null) {
            // Exclude received old messages which were not set during current launch
            return;
        }

        long duration;
        if (lastEndTime != null) {
            duration = (endTime.getTime() - lastEndTime.getTime()) / 1000;
        } else {
            duration = (endTime.getTime() - fileStats.getStartTime().getTime()) / 1000;
        }

        fileStats.setDuration(duration);

        lastEndTime = endTime;
        receivedResponses++;
    }

    public void finalStats() {
        if (countOfFiles != receivedResponses) {
            return;
        }
        LOGGER.debug("----------------------------------------------");
        fileStatsMap.forEach((key, value) -> LOGGER.debug("Time: " + value.getDuration() + " " + value.getFilename()));
        for (Map.Entry<UUID, FileStats> entry : fileStatsMap.entrySet()) {
            totalTime += entry.getValue().getDuration();
        }
        LOGGER.debug("---------------------");
        LOGGER.debug("Total time: " + totalTime);
    }

    public void setCountOfFiles(int countOfFiles) {
        this.countOfFiles = countOfFiles;
    }

}
