package isp.lab10.exercise1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ATC {
    private List<Aircraft> aircrafts = new ArrayList<>();

    public void addAircraft(String id) {
        Aircraft aircraft = new Aircraft(id);
        this.aircrafts.add(Integer.parseInt(id), aircraft);
        new Thread(aircraft).start();
    }

    public void sendCommand(String aircraftId, AtcCommand command) {
        String result = this.aircrafts.get(Integer.parseInt(aircraftId)).receiveAtcCommand(command);
        System.out.println(result);
    }

    public void showAircrafts() {
        for (Aircraft aircraft : aircrafts) {
            System.out.println(aircraft.toString());
        }
    }

    public void showMenu() {
        System.out.println("1.Show Aircrafts.......");
        System.out.println("2.Command aircraft.....");
        System.out.println("3.Show Menu............");
    }

    public static void main(String[] args) {
        ATC atc = new ATC();
        LandCommand landCommand = new LandCommand();
        atc.addAircraft("0");
        atc.addAircraft("1");
        atc.addAircraft("2");

        atc.showMenu();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int option = scanner.nextInt();
            switch (option) {
                case 1: {
                    atc.showAircrafts();
                    break;
                }
                case 2: {
                    int aircraftId;
                    int command;
                    System.out.println("Aircraft id : ");
                    aircraftId = scanner.nextInt();
                    while (!atc.aircrafts.contains(new Aircraft(aircraftId + ""))) {
                        System.out.println("Aircraft not found");
                        System.out.println("Aircraft id: ");
                        aircraftId = scanner.nextInt();
                    }

                    System.out.println("Give command for: " + atc.aircrafts.get(aircraftId).toString());
                    System.out.println("1. Take off");
                    System.out.println("2. Land");
                    command = scanner.nextInt();
                    while (command > 2 || command < 1) {
                        System.out.println("Incorrect.");
                        System.out.println("Give command for: " + atc.aircrafts.get(aircraftId).toString());
                        System.out.println("1. Take off");
                        System.out.println("2. Land");
                        command = scanner.nextInt();
                    }
                    if (command == 1) {
                        int altitude = 0;
                        System.out.println("Enter altitude: ");
                        altitude = scanner.nextInt();
                        while (altitude < 1 || altitude > 10) {
                            System.out.println("Enter altitude: ");
                            altitude = scanner.nextInt();
                        }
                        TakeOffCommand takeOffCommand = new TakeOffCommand(altitude);
                        atc.sendCommand(aircraftId + "", takeOffCommand);
                    }
                    if (command == 2) atc.sendCommand(aircraftId + "", landCommand);
                    break;
                }
                case 3: {
                    atc.showMenu();
                    break;
                }
            }
        }
    }

}

