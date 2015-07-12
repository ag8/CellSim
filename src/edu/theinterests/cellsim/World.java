package edu.theinterests.cellsim;

import java.util.Random;

public class World {
    private int xDim;
    private int yDim;
    private Cell[][] cells = new Cell[Main.DIM][Main.DIM];

    /**
     * @param d - the dimensions of the world
     */
    public World(int d) {
        this.xDim = d;
        this.yDim = d;
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                float tempGene = new Random().nextFloat();
                cells[i][j] = new Cell(tempGene, Main.STARTING_ENERGY, new Coords(i, j));
            }
        }
    }

    public int getDim() {
        return xDim; //xDim and yDim are equal
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public Cell getCell(int x, int y) { //Hey, modulos are beautiful!
        return cells[(x % xDim + xDim) % xDim]
                [(y % yDim + yDim) % yDim];
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }
}
