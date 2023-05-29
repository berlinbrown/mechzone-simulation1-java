/*
 Copyright (c) 2022 Berlin Brown

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.

 based on artificial chemistry model
 */
package org.berlin.mechzone.game;

import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The class manages a 2D grid of SquirmCellSlots and a list of
 * SquirmCells.
 */
public class GameGraphicsGrid {

    /**
     * Array of SquirmCellSlots that may or may not have SquirmCells in
     */
    protected GameCellSlot cell_grid[][];

    /**
     * the x and y size of the cell_grid array
     */
    protected int n_x, n_y;

    /**
     * list of the SquirmCells that exist in the grid
     */
    protected Vector<Entity> cell_list;

    /**
     * a count of the time steps elapsed
     */
    private int count = 0;

    private static final int N_CELLS = 200;

    public int getCount() {
        return count;
    }

    public String getContents(int x, int y) {
        // check for within area
        if (x < 0 || x >= n_x || y < 0 || y >= n_y)
            return "";

        // check cell slot not empty
        if (cell_grid[x][y].queryEmpty()) {
            return "";
        }

        String msg = "";
        Entity cell = cell_grid[x][y].getOccupant();
        msg += cell.getStringType();
        msg += cell.getState();
        // msg+=" ("+cell.getTimeSinceLastReaction()+")";
        return msg;
    }

    /**
     * Public constructor initializes size of grid and creates a simple world
     */
    public GameGraphicsGrid(int x, int y) {
        n_x = x;
        n_y = y;

        // initialize the 2D grid of slots
        cell_grid = new GameCellSlot[n_x][n_y];
        int i, j;
        for (i = 0; i < n_x; i++) {
            for (j = 0; j < n_y; j++) {
                cell_grid[i][j] = new GameCellSlot();
            }
        }

        cell_list = new Vector<>();
        initSimple();
    }

    // ----------------------------------------------------------

    /**
     * straightforward drawing of the grid and its contents
     */
    public void draw(final Graphics g, float scale, boolean fast) {
        // ask all the cells to draw themselves
        for (final Enumeration<Entity> e = cell_list.elements(); e.hasMoreElements(); ) {
            ((Entity) e.nextElement()).draw(g, scale, cell_grid, fast);
        }



        // draw the time step counter on top (to the farthest to the front)
        g.drawString(String.valueOf(count), 10, 10);
    }

    // ----------------------------------------------------------

    /**
     * initialize some simple creatures
     */
    public void initSimple() {
        // initialise an arbitrarily long string        
        // initialise a long string
        {
            Entity e = new Entity(10, n_y / 2 + 0, 0, 8, cell_list, cell_grid);
            Entity a = new Entity(10, n_y / 2 + 1, 2, 1, cell_list, cell_grid);
            Entity b = new Entity(10, n_y / 2 + 2, 3, 1, cell_list, cell_grid);
            Entity c = new Entity(10, n_y / 2 + 3, 4, 1, cell_list, cell_grid);
            Entity f = new Entity(10, n_y / 2 + 4, 1, 1, cell_list, cell_grid);
        }

        // initialize the world with some raw material (unconnected molecules)
        int px, py;
        for (int i = 0; i < N_CELLS; i++) {
            // find an empty square
            px = (int) Math.floor(Math.random() * (float) n_x);
            py = (int) Math.floor(Math.random() * (float) n_y);
            if (cell_grid[px][py].queryEmpty()) {
                new Entity(px, py, EntityBase.getRandomType(), 0, cell_list, cell_grid);
            }
        }
        // just for now, add extra 'a' cells to help memebrane growth along        
    }

    /**
     * give each cell a chance to move, in strict order
     */
    public void doTimeStep() {
        Entity cell;
        for (final Enumeration<Entity> e = cell_list.elements(); e.hasMoreElements(); ) {
            cell = (Entity) e.nextElement();
            cell.makeMove(n_x, n_y, cell_grid);
        }
    }

} // End of the class //