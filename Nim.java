// Name: Xiaojia Xu
// VUnetID: xuxj
// Email: xiaojia.j.xu@vanderbilt.edu
// Class: CS 1101 - Vanderbilt University
// Honor statement: I have neither given nor received unauthorized help on this assignment.
// Date: Nov 17/2016

// Description: This program allows the user to play a game of Nim. A file with indicated
// piles and associated sizes are extracted.

import java.io.*;
import java.util.*;

public class Nim {

    public static void main(String[] args)
            throws FileNotFoundException {

        Scanner input = new Scanner(System.in);
        String inputFileName = askFileName(input) + ".txt";
        int pileCnt = pileCnt(inputFileName);
        int[] piles = makePile(inputFileName, pileCnt);

        int checksum = sumPiles(piles, 0);
        boolean play = true;
        while (play) {
            display(piles);
            piles = userTurn(piles, input);
            if (sumPiles(piles, 1) == 0) {
                System.out.println("Congratulations! You win!");
                play = false;
            }
            piles = compTurn(piles);
            if (sumPiles(piles, 1) == 0) {
                System.out.println("Sorry - computer win.");
                play = false;
            }
        }

        System.out.println("\nEnd of program.");
    }

    /**
     *Prompts the user to enter the file name and checks if file exists.
     *
     * @param input Scanner object for keyboard input
     * @return      String containing the file name
     */
    public static String askFileName(Scanner input) {

        boolean notFound = true;
        String fileName = "";

        while(notFound){
            System.out.print("Enter name of input file - no extension please: ");
            fileName = input.nextLine();
            File inputFile = new File(fileName+".txt");
            if(inputFile.exists()){
                notFound = false;
            }else{
                System.out.println("File does not exist. Please try again.");
                notFound = true;
            }
        }

        return fileName;
    }

    /**
     * counts the number piles, and will exit program if input file is empty.
     * @param fileName  String containing file name
     * @return          integer number of piles
     * @throws FileNotFoundException
     */
    public static int pileCnt(String fileName)
            throws FileNotFoundException{

        int pileCnt = 0;
        Scanner read = new Scanner(new File(fileName));
        if(read.hasNext()){
            String[] readPile = read.nextLine().split("n");
            for(int i = 0; i < readPile.length; i++){
                //System.out.println(readPile[i]);
                Scanner sizes = new Scanner(readPile[i]);
                int bins = 0;
                while(sizes.hasNextInt()){
                    int iPileSize = sizes.nextInt();
                    bins++;
                }
                if(bins == 1){
                    pileCnt++;
                }
            }
        }else{
            System.out.println("Sorry, there are no sticks for starting a game. " +
                    "Try with another file.");
            System.exit(-1);
        }

        return pileCnt;
    }

    /**
     * creats an integer array containing the number of sticks for each pile
     * @param fileName  input file name
     * @param pileCnt   number of piles
     * @return          integer array with same size as number of piles
     * @throws FileNotFoundException
     */
    public static int[] makePile(String fileName, int pileCnt)
            throws FileNotFoundException{

        int[] piles = new int[pileCnt];
        String[] readPile = new Scanner(new File(fileName)).nextLine().split("n");
        int ind = 0;
        int iPileSize = 0;

        for(int i = 0; i < readPile.length; i++){
            Scanner sizes = new Scanner(readPile[i]);
            int bins = 0;
            while(sizes.hasNextInt()){
                iPileSize = sizes.nextInt();
                bins++;
            }
            if(bins == 1){
                if(iPileSize > Integer.MAX_VALUE){
                    System.exit(-1);
                }
                piles[ind] = iPileSize;
                ind++;
            }else{
                System.out.println("Record #: " + (i+1) + " Error:" +
                        readPile[i].replace('\\',' '));
            }
        }

        System.out.println("There are " + pileCnt + " valid piles.");
        return piles;
    }

    /**
     * adds up the total number of sticks in all piles
     * @param piles integer array containing sticks
     * @return      sum of sticks
     */
    public static int sumPiles(int[] piles, int start) {
        int sum = 0;
        for(int i = 0; i < piles.length; i++){
            sum += piles[i];
        }
        if(start == 0 && sum == 0){
            System.out.println("Sorry, there are no sticks for starting a game. " +
                    "Try with another file.");
            System.exit(-1);
        }
        return sum;
    }

    /**
     * displays the information to the user
     * @param piles integer array containing info of piles
     */
    public static void display(int[] piles) {

        System.out.println("Nim Game - CS1101 Fall 2016:");
        for(int i = 0; i < piles.length; i++) {
            String header = "Pile " + (i+1) + "[" +piles[i] +"]:";
            String sticks = "";
            int plus = 15 - header.length();
            for (int k = 0; k < plus; k++){
                header += ' ';
            }
            for (int j = 0; j < piles[i]; j++){
                sticks += '|';
            }
            System.out.println(header+sticks);
        }
    }

    /**
     * processes user's turn to remove sticks
     * @param piles integer array containing info of piles
     * @param input scanner for keyboard entry
     * @return      updated integer array containing info of piles
     */
    public static int[] userTurn(int[] piles, Scanner input) {

        boolean invalid = true;
        int pileInd = 0;
        int remove = 0;
        while(invalid){
            System.out.println("\nEnter pile# and quantity removed in that pile:");
            pileInd = input.nextInt();
            remove = input.nextInt();
            boolean pileValid = pileInd > 0 && pileInd <= piles.length;
            if(pileValid){
                if (remove > 0 && remove <= piles[pileInd-1]){
                    invalid = false;
                }else{
                    System.out.println("Quantity not available in this pile.");
                }
            }else{
                System.out.println("Quantity not available in this pile.");
            }
        }

        piles[pileInd-1] -= remove;
        return piles;
    }

    /**
     * processes computer's turn
     * @param piles integer array containing info of piles
     * @return      updated integer array containing info of piles
     */
    public static int[] compTurn(int[] piles) {

        int nimSum = NimAdd(piles[0], piles[1]);
        for (int i = 2; i < piles.length; i++){
            nimSum = NimAdd(nimSum, piles[i]);
        }
        int pileInd = 0;
        int remove = 0;
        for (int j = 0; j < piles.length; j++){
            int iSum = NimAdd(piles[j], nimSum);
            if (iSum < piles[j]){
                pileInd = j;
                remove = piles[j] - iSum;
            }
        }

        System.out.printf("Computer removes %d from pile %d\n", remove, (pileInd+1));
        piles[pileInd] -= remove;
        return piles;
    }

    /**
     * executes nim addition
     * @param i1    integer 1
     * @param i2    integer 2
     * @return      nim sum of the two input integers
     */
    public static int NimAdd(int i1, int i2) {

        int length = 0;
        int l1 = findLen(i1);
        int l2 = findLen(i2);
        if (l1 > l2){
            length = l1;
        }else{
            length = l2;
        }

        int[] b1 = toBinary(i1, length);
        int[] b2 = toBinary(i2, length);
        int[] Sum = new int[length];
        for(int i = 0; i < length; i++){
            if(b1[i] == 0 && b2[i] == 1){
                Sum[i] = 1;
            }else if(b1[i] == 1 && b2[i] == 0){
                Sum[i] = 1;
            }else {
                Sum[i] = 0;
            }
        }

        int sum = toInt(Sum);
        return sum;
    }

    /**
     * finds length of the binary number of an integer
     * @param num   integer number
     * @return      length
     */
    public static int findLen(int num) {
        int len = 0;
        while(num > 0){
            num /= 2;
            len++;
        }
        return len;
    }

    /**
     * converts integer to binary number
     * @param num       integer number
     * @param length    length of binary number
     * @return          integer array containing the binary number
     */
    public static int[] toBinary(int num, int length){
        int binary[] = new int[length];
        for (int i = 0; i < length; i++){
            binary[i] = num%2;

            num /= 2;
        }
        return binary;
    }

    /**
     * converts binary number to an integer
     * @param Sum   integer array containing the binary number to be converted
     * @return      converted integer
     */
    public static int toInt(int[] Sum) {
        int sum = 0;
        int val = 0;
        for(int i = 0; i < Sum.length; i++){
            if(Sum[i] == 1){
                val = (int) Math.pow(2,i) ;
            }else{
                val = 0;
            }
            sum += val;
        }
        return sum;
    }

}
