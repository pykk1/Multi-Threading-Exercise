package isp.lab10.exercise1;

public class TakeOffCommand extends AtcCommand {
    private int altitude;

    public TakeOffCommand(int altitude) {
        this.altitude = altitude;
    }

    public int getAltitude() {
        return altitude;
    }
}
