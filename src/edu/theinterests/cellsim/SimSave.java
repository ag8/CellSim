package edu.theinterests.cellsim;

import java.util.ArrayList;
import java.util.List;

public class SimSave { //Literally just a list of worlds for each day
    private List<World> list = new ArrayList<World>();

    public SimSave() { //The sweetest constructor is the emptiest constructor

    }

    public List<World> getList() {
        return this.list;
    }

    public void addDay(World world) {
        this.list.add(world);
    }
}
