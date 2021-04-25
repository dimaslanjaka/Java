import java.util.Random;
import java.util.Scanner;

/**
 * Simulates a slot machine game.
 * <p>
 * See Blackboard for instructions.
 *
 * @author YOUR NAME HERE
 */
public class SlotMachine {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random random = new Random();
        int balance = 50;
        int betAmount = 1;
        int lines = 1;
        int totalBet = 1;
        int randomValue = 1;

        String[] slotValues = new String[100];
        for (int t = 0; t < 60; t++) {
            slotValues[t] = "---";
        }
        for (int b = 60; b < 85; b++) {
            slotValues[b] = "o^o";
        }
        for (int c = 85; c < 95; c++) {
            slotValues[c] = "BAR";
        }
        for (int d = 95; d < 100; d++) {
            slotValues[d] = "7";
        }
        //String[] slotValues = {"---", "o^o", "BAR", "7"};
        String[][] slotMachine = new String[3][5];
        System.out.println("Would you like to play? Yes/No");

        while(input.hasNextLine()){
            String choice = input.nextLine();
            while (choice.equalsIgnoreCase("yes")) {
                System.out.print("Enter bet amount (1-5): ");
                betAmount = Integer.parseInt(input.nextLine());
                System.out.print("Enter number of lines to play (1-3): ");
                lines = Integer.parseInt(input.nextLine());
                totalBet = betAmount * lines;
                balance = balance - totalBet;
                System.out.println("Total bet: " + totalBet);
                System.out.println();
                System.out.println("===================================");
                for (int i = 0; i < 3; i++) {
                    for (int x = 0; x < 5; x++) {
                        randomValue = random.nextInt(100);
                        slotMachine[i][x] = slotValues[randomValue];
                        System.out.print(slotMachine[i][x] + "\t");
                    }
                    System.out.println();
                }
                System.out.println("===================================");
                int countCherry = 0;
                int countBar = 0;
                int countSeven = 0;
                int cherryEarn = 0;
                int barEarn = 0;
                int sevenEarn = 0;
                int totalWin = 0;
                for (int j = 0; j < lines; j++) {
                    for (int w = 0; w < 5; w++) {
                        String temp = slotMachine[j][w];
                        switch (temp) {
                            case "o^o":
                                countCherry++;
                                break;
                            case "BAR":
                                countBar++;
                                break;
                            case "7":
                                countSeven++;
                                break;
                        }
                    }

                    if (countCherry == 2) {
                        cherryEarn = totalBet * 1;
                        balance += cherryEarn;
                    } else if (countCherry == 3) {
                        cherryEarn = totalBet * 2;
                        balance += cherryEarn;
                    } else if (countCherry == 4) {
                        cherryEarn = totalBet * 3;
                        balance += cherryEarn;
                    } else if (countCherry == 5) {
                        cherryEarn = totalBet * 4;
                        balance += cherryEarn;
                    }
                    if (countBar == 2) {
                        barEarn = totalBet * 2;
                        balance += barEarn;
                    } else if (countBar == 3) {
                        barEarn = totalBet * 4;
                        balance += barEarn;
                    } else if (countBar == 4) {
                        barEarn = totalBet * 6;
                        balance += barEarn;
                    } else if (countBar == 5) {
                        barEarn = totalBet * 8;
                        balance += barEarn;
                    }
                    if (countSeven == 2) {
                        sevenEarn = totalBet * 4;
                        balance += sevenEarn;
                    } else if (countSeven == 3) {
                        sevenEarn = totalBet * 6;
                        balance += sevenEarn;
                    } else if (countSeven == 4) {
                        sevenEarn = totalBet * 8;
                        balance += sevenEarn;
                    } else if (countSeven == 5) {
                        sevenEarn = totalBet * 10;
                        balance += sevenEarn;
                    }
                    countCherry = 0;
                    countBar = 0;
                    countSeven = 0;
                    totalWin = cherryEarn + barEarn + sevenEarn;
                }
                System.out.println("Total Win: " + totalWin);
                System.out.println("Current balance: " + balance);
                System.out.println("Do you wish to keep playing?");
                choice = input.nextLine();
            }
        }

    }
}

