package edu.theinterests.cellsim;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private float gene;
    private int energy;
    private Coords coords;
    private List<Cell> neighbors = new ArrayList<Cell>();

    public Cell(float gene, int energy, Coords coords) {
        this.gene = gene;
        this.energy = energy;
        this.coords = coords;
    }

    public void analyzeNeighbors() {
        //     y-1 y y+1
        // x-1 [1][2][3]
        //  x  [0][C][4]
        // x+1 [7][6][5]

        neighbors.clear();
        neighbors.add(Main.torus.getCell(coords.getX() + 0, coords.getY() - 1));
        neighbors.add(Main.torus.getCell(coords.getX() - 1, coords.getY() - 1));
        neighbors.add(Main.torus.getCell(coords.getX() - 1, coords.getY() + 0));
        neighbors.add(Main.torus.getCell(coords.getX() - 1, coords.getY() + 1));
        neighbors.add(Main.torus.getCell(coords.getX() + 0, coords.getY() + 1));
        neighbors.add(Main.torus.getCell(coords.getX() + 1, coords.getY() + 1));
        neighbors.add(Main.torus.getCell(coords.getX() + 1, coords.getY() + 0));
        neighbors.add(Main.torus.getCell(coords.getX() + 1, coords.getY() - 1));
    }

    public Coords getCoords() {
        return this.coords;
    }

    public float getGene() {
        return this.gene;
    }

    public int getEnergy() {
        return this.energy;
    }

    public List<Cell> getNeighbors() {
        return this.neighbors;
    }

    public Cell getRandomNeighbor() {
        return this.neighbors.get(Utils.randInt(0, 7));
    }


    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setGene(float gene) {
        this.gene = gene;
    }
}
