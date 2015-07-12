package edu.theinterests.cellsim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Display extends JComponent {
    private int whichDay = 0;

    public Display(int whichDay) {
        this.whichDay = whichDay;
    }

    public void paint(Graphics g) {
        SimSave daysSave  = Main.results;
        ArrayList<World> days = (ArrayList<World>) daysSave.getList();
        //for (World day : days) {
            World day = days.get(whichDay);
            for (int i = 0; i < Main.DIM; i++) {
                for (int j = 0; j < Main.DIM; j++) {
                    int x = (int) (day.getCell(i, j).getGene() * 100.0);
                    int y = day.getCell(i, j).getEnergy();
                    if (y > 1023) {
                        y = 1023;
                    }
                    if (y < 0) {
                        y = 0;
                    }

                    g.setColor(new Color((255 * x) / 100, (255 * (100 - x)) / 100, 0));
                    g.fillRect(i * Main.SIMULATION_CELL_SIZE, j * Main.SIMULATION_CELL_SIZE, Main.SIMULATION_CELL_SIZE, Main.SIMULATION_CELL_SIZE);

                    int energy = 255 - (int)Math.floor(y / 4);
                    g.setColor(new Color(energy, energy, energy));
                    g.fillRect(i * Main.SIMULATION_CELL_SIZE + Main.SIMULATION_CELL_SIZE / 2, j * Main.SIMULATION_CELL_SIZE + Main.SIMULATION_CELL_SIZE / 2, Main.SIMULATION_CELL_SIZE / 2, Main.SIMULATION_CELL_SIZE / 2);
                }
            }
        //}
    }
}