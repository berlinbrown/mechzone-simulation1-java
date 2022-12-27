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

 */
package org.berlin.mechzone.game;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class Entity extends EntityBase {

    private static final Logger LOGGER = Logger.getLogger(Entity.class);

    // / the cell's current location
    private int x, y;

    // / which direction did we move in previously?
    private int last_x, last_y;

    /*
     * encoding of an 8-neighbourhood: 1 2 3 0 8 4 7 6 5
     */
    private static final int EIGHT_X[] = { -1, -1, 0, 1, 1, 1, 0, -1, 0 };
    private static final int EIGHT_Y[] = { 0, -1, -1, -1, 0, 1, 1, 1, 0 };

    /**
     * Default constructor
     */
    public Entity(int x_loc, int y_loc, int cell_type, int cell_state, Vector<Entity> cell_list,
                  GameCellSlot cell_grid[][]) {

        super(cell_type, cell_state);
        if (cell_grid[x_loc][y_loc].queryEmpty()) {
            x = x_loc;
            y = y_loc;
            cell_list.addElement(this);
            cell_grid[x][y].makeOccupied(this);

        } else {
            // couldn't create! (square was occupied)
            throw new Error("Couldn't create, square is occupied!");
        }
    }

    public String toString() {
        return super.toString();
    }

    /**
     * access function returning x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * access function returning y-coordinate
     */
    public int getY() {
        return y;
    }


    /**
     * move to an 8-neighbourhood empty square subject to all bonds being
     * maintained (8-connectivity)
     */
    public void makeMove(int n_x, int n_y, GameCellSlot cell_grid[][]) {
        // which of the 8 possible moves is valid? (empty and maintains
        // bonds)
        boolean valid_move[] = new boolean[8];
        int n_valid_moves = 0;
        int tx, ty;
        for (int i = 0; i < 8; i++) {
            tx = x + EIGHT_X[i];
            ty = y + EIGHT_Y[i];
            if (tx >= 0 && tx < n_x && ty >= 0 && ty < n_y && cell_grid[tx][ty].queryEmpty()) {
                valid_move[i] = true;
                n_valid_moves++;
            } else
                valid_move[i] = false;
        }

        if (n_valid_moves > 0) {
            int choices[] = new int[n_valid_moves];
            int j = 0;
            for (int i = 0; i < 8; i++) {
                if (valid_move[i]) {
                    choices[j++] = i;
                }
            }
            int which = (int) Math.floor(Math.random() * (float) n_valid_moves);
            int move = choices[which];

            // move there
            moveTo(x + EIGHT_X[move], y + EIGHT_Y[move], cell_grid);
        }

    }

    private void moveTo(int new_x, int new_y, GameCellSlot cell_grid[][]) {
        // move there
        cell_grid[x][y].makeEmpty();
        x = new_x;
        y = new_y;
        cell_grid[x][y].makeOccupied(this);
    }

    /**
     * Draws the cell
     */
    public void draw(Graphics g, float scale, GameCellSlot cell_grid[][], boolean fast) {
        g.setColor(getColour());
        g.fillRect((int) (x * scale), (int) (y * scale), (int) scale, (int) scale);

        // draw our bonds
        g.setColor(Color.black);
        int hx, hy;
        hx = (int) ((x + 0.5) * scale);
        hy = (int) ((y + 0.5) * scale);

        // draw our state (if enough room)
        if (scale >= 12) {
            final Integer i = getState();
            final String type = this.getStringType();
            final String str = type + i.toString();
            g.drawString(str, (int) ((x * scale) + 2), (int) ((y * scale) + scale - 2));
        }
    }

} // End of the class //