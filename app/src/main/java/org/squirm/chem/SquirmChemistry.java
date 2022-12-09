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
package org.squirm.chem;

// SquirmChemistry.java

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

class SquirmChemistry {

    private static final Logger LOGGER = Logger.getLogger(SquirmChemistry.class);
    
    private Vector<SquirmReaction> reactions;

    public SquirmChemistry() {
        reactions = new Vector<SquirmReaction>();
    }

    public void removeAllReactions() {
        reactions.removeAllElements();
    }

    public void addReaction(final SquirmReaction r) {
        reactions.addElement(r);
    }

    public void react(SquirmCellSlot cell_grid[][], SquirmCell cell, Vector<SquirmCell> neighbours) {
        // try all the reactions in turn
        for (Enumeration<SquirmReaction> e = reactions.elements(); e.hasMoreElements();) {
            tryReaction(cell_grid, cell, neighbours, (SquirmReaction) e.nextElement());
        }
    }

    protected void tryReaction(SquirmCellSlot cell_grid[][], SquirmCell cell, Vector<SquirmCell> neighbours,
            SquirmReaction r) {
        tryReaction(cell_grid, cell, neighbours, r.us_type, r.us_state, r.current_bond, r.them_type, r.them_state,
                r.future_us_state, r.future_bond, r.future_them_state);
    }

    protected void tryReaction(final SquirmCellSlot cell_grid[][], SquirmCell cell, Vector<SquirmCell> neighbours,
            char us_type, int us_state, boolean current_bond, char them_type, int them_state, int future_us_state,
            boolean future_bond, int future_them_state) {

        // us_type is one of {e,f,a,b,c,d,x}
        if (us_type != 'e' && us_type != 'f' && us_type != 'a' && us_type != 'b' && us_type != 'c' && us_type != 'd'
                && us_type != 'x')
            throw new Error("SquirmChemistry::Reaction() : invalid us_type");

        // them_type is one of {e,f,a,b,c,d,x,y}
        if (them_type != 'e' && them_type != 'f' && them_type != 'a' && them_type != 'b' && them_type != 'c'
                && them_type != 'd' && them_type != 'x' && them_type != 'y')
            throw new Error("SquirmChemistry::Reaction() : invalid them_type");

        // sanity check on the states requested
        if (us_state < 0 || them_state < 0 || future_us_state < 0 || future_them_state < 0)
            throw new Error("SquirmChemistry::tryReaction() : states less than zero not permitted");

        // are we the right kind of cell for this reaction?
        if ((us_type != 'x' && cell.isTypeAndState(us_type, us_state)) || (us_type == 'x' && cell.isState(us_state))) {
            // do we have a neighbour (bonded/not) that is the right kind for
            // this reaction?
            Vector<SquirmCell> search_from = current_bond ? cell.getBonds() : neighbours;
            Vector<SquirmCell> ns;
            // if them_type specified then search for it
            if (them_type != 'x' && them_type != 'y')
                ns = getThoseOfTypeAndState(search_from, them_type, them_state);
            // if unspecified but to be same as us_type then search for it
            else if (them_type == 'x' && us_type == 'x')
                ns = getThoseOfTypeAndState(search_from, cell.getType(), them_state);
            // must be unspecified
            else if ((them_type == 'x' && us_type != 'x') || them_type == 'y')
                ns = getThoseOfState(search_from, them_state);
            else
                throw new Error("SquirmChemistry::tryReaction() : unexpected case statement");
            // try the reaction on each of the possibles
            for (Enumeration<SquirmCell> e = ns.elements(); e.hasMoreElements();) {
                SquirmCell n = (SquirmCell) e.nextElement();
                // reactions can happen if the two cells are right next to each
                // other (share a face) or over a diagonal (share a corner) if
                // the other diagonal
                // doesn't have a bond
                boolean can_react = false;

                if (rightNextToEachOther(cell, n))
                    can_react = true;
                else {
                    // if either other diagonal square is empty then OK
                    if (cell_grid[cell.getX()][n.getY()].queryEmpty() || cell_grid[n.getX()][cell.getY()].queryEmpty())
                        can_react = true;
                    else {
                        // otherwise, if there is no bond between diagonals then
                        // still OK
                        SquirmCell cellA = cell_grid[cell.getX()][n.getY()].getOccupant();
                        SquirmCell cellB = cell_grid[n.getX()][cell.getY()].getOccupant();
                        if (!cellA.getBonds().contains(cellB))
                            can_react = true;
                    }
                }

                if (can_react) {
                    // make or break bonds as specified
                    if (current_bond && !future_bond) {
                        LOGGER.info("Breaking bond : UsType=" + us_type + " UsState=" + us_state + " ThemType=" + them_type + " ThemState=" + them_state);
                        cell.breakBondWith(n);
                    } else if (!current_bond && future_bond) {
                        LOGGER.info("Making bond : UsType=" + us_type + " UsState=" + us_state + " ThemType=" + them_type + " ThemState=" + them_state);                                               
                        cell.makeBondWith(n);
                    }
                    // set our states to their new values
                    cell.setState(future_us_state);
                    n.setState(future_them_state);
                    break;
                }
            }
        }
    }

    protected Vector<SquirmCell> getThoseOfTypeAndState(Vector<SquirmCell> cells, char type, int state) {
        return getThoseOfTypeAndState(cells, SquirmCellProperties.getType(type), state);
    }

    protected Vector<SquirmCell> getThoseOfTypeAndState(Vector<SquirmCell> cells, int type, int state) {
        // do any of these cells match the type and state specified?
        // if so return all that match (empty list if none)

        Vector<SquirmCell> v = new Vector<SquirmCell>();
        SquirmCell c;
        for (Enumeration<SquirmCell> enumx = cells.elements(); enumx.hasMoreElements();) {
            c = ((SquirmCell) enumx.nextElement());
            if (type != -1) {
                if (c.isTypeAndState(type, state))
                    v.addElement(c);
            } else if (c.isState(state))
                v.addElement(c);
        }

        return v;
    }

    protected Vector<SquirmCell> getThoseOfState(final Vector<SquirmCell> cells, int state) {
        // do any of these cells match the state specified? (any type)
        // if so return all that match (empty list if none)

        final Vector<SquirmCell> v = new Vector<SquirmCell>();
        SquirmCell c;
        for (Enumeration<SquirmCell> enumx = cells.elements(); enumx.hasMoreElements();) {
            c = ((SquirmCell) enumx.nextElement());
            if (c.isState(state)) {
                v.addElement(c);
            }
        }

        return v;
    }

    protected boolean rightNextToEachOther(SquirmCell cell1, SquirmCell cell2) {
        return (Math.abs(cell1.getX() - cell2.getX()) + Math.abs(cell1.getY() - cell2.getY()) < 2);
    }

} // End of the class //