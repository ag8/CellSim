package edu.theinterests.cellsim;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Main {
    public static World torus;

    //VARIABLES

    public static int DIM = 25;
    public static int STARTING_ENERGY = 100;
    public static int LIVING_ENERGY = 40;
    public boolean done = false;

    public static int DOUBLE_COOPERATION_BONUS = 14;
    public static int SINGLE_DISCOOPERATION_BONUS = 10;
    public static int SINGLE_COOPERATION_BONUS = 0;
    public static int DOUBLE_DISCOOPERATION_BONUS = 3;

    public static int UPPER_LIMIT = 1023;

    public static float DEATH_MUTATION = 0.25F;
    public static float DAY_MUTATION = 0.00F;

    public static int DURATION = 100;

    public static int SIMULATION_CELL_SIZE = 30;
    public static int SIMULATION_BORDER_PADDING = 3;

    public static SimSave results = new SimSave();

    public static int DELAY = 25;

    public static void main(String[] args) {
        new Main().setup();
        new Main().run();
        try {
            new Main().analyze();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    private void setup() {
        torus = new World(DIM);

        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                Cell currentCell = torus.getCells()[i][j];
                currentCell.analyzeNeighbors(); //Setup of cells
            }
        }
    }

    private void run() {
        //Add original state
        results.addDay(Utils.clone(torus));

        int counter = 0;
        while (!done) {
            //System.out.println(counter);
            counter++;
            if (counter > DURATION) {break;}

            //Day.

            //Beginning of day: Each cell loses energy on life. Each cell checks for new neighbors. Small daily mutation.
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    Cell currentCell = torus.getCells()[i][j];
                    currentCell.setEnergy(currentCell.getEnergy() - LIVING_ENERGY);
                    if (currentCell.getEnergy() > UPPER_LIMIT) {
                        currentCell.setEnergy(UPPER_LIMIT);
                    }
                    currentCell.analyzeNeighbors();
                    currentCell.setGene(currentCell.getGene() + (float)Utils.randomInRange(-DAY_MUTATION, DAY_MUTATION));
                    if (currentCell.getGene() < 0) {
                        currentCell.setGene(0);
                    }
                    if (currentCell.getGene() > 1) {
                        currentCell.setGene(1);
                    }
                }
            }

            //Next part: each cell chooses a neighbor, and plays the Prisoner's Dilemma game.
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    Cell currentCell = torus.getCells()[i][j];
                    Cell other = currentCell.getRandomNeighbor();

                    boolean good1 = false;
                    boolean good2 = false;

                    if (currentCell.getGene() < Utils.randomInRange(0.0, 1.0)) {
                        good1 = true;
                    }
                    if (other.getGene() < Utils.randomInRange(0.0, 1.0)) {
                        good2 = true;
                    }

                    if (good1 && good2) { //Both cooperate
                        currentCell.setEnergy(currentCell.getEnergy() + DOUBLE_COOPERATION_BONUS);
                        other.setEnergy(other.getEnergy() + DOUBLE_COOPERATION_BONUS);
                    }
                                          //One cooperates
                    if (good1 && !good2) {
                        currentCell.setEnergy(currentCell.getEnergy() + SINGLE_COOPERATION_BONUS);
                        other.setEnergy(other.getEnergy() + SINGLE_DISCOOPERATION_BONUS);
                    }
                    if (!good1 && good2) {
                        currentCell.setEnergy(currentCell.getEnergy() + SINGLE_DISCOOPERATION_BONUS);
                        other.setEnergy(other.getEnergy() + SINGLE_COOPERATION_BONUS);
                    }
                    if (!good1 && !good2) {
                        currentCell.setEnergy(currentCell.getEnergy() + DOUBLE_DISCOOPERATION_BONUS);
                        other.setEnergy(other.getEnergy() + DOUBLE_DISCOOPERATION_BONUS);
                    }
                }
            }

            //Last step. Check if a cell is dead. If it is, replace with a new cell, whose gene and energy is the average of the surrounding cells. Remove avg/8 from each of the surrounding cells.
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    Cell currentCell = torus.getCells()[i][j];
                    int energy = currentCell.getEnergy();
                    if (energy < 0) { //Cell is dead
                        currentCell.analyzeNeighbors();
                        float avgEnergy = 0;
                        float avgGene = 0;
//                        System.out.println("DAY " + counter + "; CELL (" + i + ", " + j + ").");
//                        System.out.print("The neighbors are: (" + currentCell.getNeighbors().size() + ")");
                        for (Cell neighbor : currentCell.getNeighbors()) {
//                            System.out.print(neighbor.getEnergy() + " ");
                            avgEnergy += neighbor.getEnergy();
                            avgGene += neighbor.getGene();
                        }
                        //Divide by the number of cells
                        avgEnergy  = avgEnergy / 8;
//                        System.out.print("...so the average is " + avgEnergy);
//                        System.out.println();
                        avgGene = avgGene / 8;

                        for (Cell neighbor : currentCell.getNeighbors()) {
                            neighbor.setEnergy((int)(neighbor.getEnergy() - avgEnergy / 8));
                        }

                        currentCell.setEnergy((int)avgEnergy);
                        currentCell.setGene(avgGene + (float)Utils.randomInRange(-DEATH_MUTATION, DEATH_MUTATION));
                        if (currentCell.getGene() < 0) {
                            currentCell.setGene(0);
                        }
                        if (currentCell.getGene() > 1) {
                            currentCell.setGene(1);
                        }
                    }
                }
            }

            //Last part: save results.
            results.addDay(Utils.clone(torus));
            //System.out.println(results.getList().size());
        }
    }

    private void analyze() throws InterruptedException {
        System.out.println(Utils.getNumberOfCooperators(results.getList().get(DURATION - 1)));
        System.out.println(Utils.getNumberOfUncooperators(results.getList().get(DURATION - 1)));

//        System.out.println(results.getList().size());
        /*
        int d = 0;
        for (World day : results.getList()) {
            System.out.println("WELCOME TO DAY " + d + "!_____________________________________________________");
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    System.out.print("["+ day.getCell(i, j).getEnergy() + "] ");
                }
                System.out.println();
            }
            d++;
        }
        */

        //Display animation
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(new Dimension(DIM * SIMULATION_CELL_SIZE + 6 * SIMULATION_BORDER_PADDING, DIM * SIMULATION_CELL_SIZE + 16 * SIMULATION_BORDER_PADDING));
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setTitle("Day " + 0 + " of " + results.getList().size() + "");
        Display display = new Display(0);
        window.getContentPane().add(display);

        for (int i = 1; i < results.getList().size(); i++) {
            Thread.sleep(DELAY);
            window.setTitle("Day " + i + " of " + results.getList().size() + " || Total energy: " + Utils.getTotalEnergy(results.getList().get(i)));
            window.getContentPane().remove(display);
            display = new Display(i);
            display.repaint();
            window.getContentPane().add(display);
            window.repaint();
            window.setVisible(true);

        }
    }
}
