package isp.lab10.exercise1;

import java.util.Objects;

public class Aircraft implements Runnable {
    private String id;
    private int altitude;
    private AircraftState aircraftState;
    private boolean isLanded = false;
    private long untilCruisingTimeMillis;

    public Aircraft(String id) {
        this.id = id;
        this.altitude = 0;
        aircraftState = AircraftState.On_Stand;
    }

    @Override
    public void run() {
        while (!isLanded) {
            switch (aircraftState) {
                case On_Stand: {
                    printAircraftState("On_Stand");
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case Taxing: {
                    try {
                        printAircraftState("Taxing");
                        Thread.sleep(10000);
                        setAircraftState(AircraftState.Taking_Off);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case Taking_Off: {
                    try {
                        printAircraftState("Taking_Off");
                        Thread.sleep(5000);
                        setAircraftState(AircraftState.Ascending);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case Ascending: {
                    printAircraftState("Ascending(0 m)");
                    for (int i = 1; i <= altitude; i++) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        printAircraftState("Ascending(" + i * 1000 + " m)");
                    }
                    setAircraftState(AircraftState.Cruising);
                    break;
                }
                case Cruising: {
                    printAircraftState("Cruising");
                    untilCruisingTimeMillis = System.currentTimeMillis();
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    setAircraftState(AircraftState.Descending);
                    break;
                }
                case Descending: {
                    printAircraftState("Descending(" + altitude * 1000 + " m)");
                    for (int i = 1; i <= altitude; i++) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        printAircraftState("Descending(" + (altitude - i) * 1000 + " m)");
                    }
                    setAircraftState(AircraftState.Landed);
                    break;
                }
                case Landed: {
                    printAircraftState("Landed");
                    System.out.println("Time spent cruising: " + (System.currentTimeMillis() - untilCruisingTimeMillis) + " ms");
                    isLanded = true;
                }
            }
        }
    }

    public void printAircraftState(String state) {
        System.out.println("Aircraft " + this.id + " is in " + state);
    }

    public void setAircraftState(AircraftState aircraftState) {
        this.aircraftState = aircraftState;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "id='" + id + '\'' +
                ", altitude=" + altitude +
                ", aircraftState=" + aircraftState +
                ", isLanded=" + isLanded +
                '}';
    }


    public String receiveAtcCommand(AtcCommand command) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (command instanceof TakeOffCommand) {
            synchronized (this) {
                if (aircraftState == AircraftState.On_Stand) {
                    aircraftState = AircraftState.Taxing;
                    this.altitude = ((TakeOffCommand) command).getAltitude();
                    this.notify();
                    return "Success";
                } else return "Aircraft is not On_Stand";
            }
        } else if (command instanceof LandCommand) {
            synchronized (this) {
                if (aircraftState == AircraftState.Cruising) {
                    aircraftState = AircraftState.Descending;
                    this.notify();
                    return "Success";
                } else return "Aircraft is not Cruising";
            }
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        return Objects.equals(id, aircraft.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, altitude, aircraftState, isLanded);
    }
}
