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

// SquirmCellSlot.java

/**
 * Cell Slot.
 */
public class SquirmCellSlot {

    protected boolean has_occupant;
    protected SquirmCell occupant;

    // Public constructor initializes size of grid
    public SquirmCellSlot() {
        has_occupant = false;
    }

    public void makeOccupied(SquirmCell occ) {
        // !has_occupant is a necessary condition for calling this function
        if (has_occupant)
            throw new Error("SquirmCellSlot::makeOccupied : already occupied!");

        has_occupant = true;
        occupant = occ;
    }

    public void makeEmpty() {
        has_occupant = false;
    }

    public boolean queryEmpty() {
        return !has_occupant;
    }

    public SquirmCell getOccupant() {
        // has_occupant is a necessary condition for calling this function
        if (!has_occupant)
            throw new Error("SquirmCellSlot::getOccupant : no occupant!");

        return occupant;
    }
} // End of the Class //