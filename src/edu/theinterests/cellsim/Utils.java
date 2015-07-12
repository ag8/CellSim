package edu.theinterests.cellsim;

import java.util.Random;

public class Utils {
    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static double randomInRange(double min, double max) {
        return Math.random() < 0.5 ? ((1-Math.random()) * (max-min) + min) : (Math.random() * (max-min) + min);
    }

    public static int getTotalEnergy(World day) {
        int e = 0;
        for (int i = 0; i < Main.DIM; i++) {
            for (int j = 0; j < Main.DIM; j++) {
                e += day.getCell(i, j).getEnergy();
            }
        }
        return e;
    }

    public static int getNumberOfCooperators(World day) {
        int n = 0;
        for (int i = 0; i < Main.DIM; i++) {
            for (int j = 0; j < Main.DIM; j++) {
                if (day.getCell(i, j).getGene() < 0.5) {
                    n++;
                }
            }
        }
        return n;
    }

    public static int getNumberOfUncooperators(World day) {
        int n = 0;
        for (int i = 0; i < Main.DIM; i++) {
            for (int j = 0; j < Main.DIM; j++) {
                if (day.getCell(i, j).getGene() > 0.5) {
                    n++;
                }
            }
        }
        return n;
    }

    /**
     * Cloning method. Copies a world into one with new pointers.
     *
     * @param world The world to clone
     * @return A new world cloned from the parameter
     */
    public static World clone(World world) {
        int d = world.getDim();
        World newWorld = new World(d);
        Cell[][] cells = new Cell[d][d];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                float thisGene = world.getCell(i, j).getGene();
                int thisEnergy = world.getCell(i, j).getEnergy();
                Cell newCell = new Cell(thisGene, thisEnergy, new Coords(i, j));

                cells[i][j] = newCell;
            }
        }
        newWorld.setCells(cells);
        return newWorld;
    }
}
