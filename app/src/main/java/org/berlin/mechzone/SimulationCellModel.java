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

   based on artificial chemistry model- tim hutton
 */
package org.berlin.mechzone;

// SquirmCellProperties.java

import java.awt.Color;

class SimulationCellModel {
    
    private int type;
    private int state;
    protected int time_since_last_reaction = 0;
    
    private static final int MAX_TYPES = 6; // 0-5
    private static final int MAX_STATES = 11; // this is controversial right now

    protected static final int MAX_AGE = 1000;

    private Color TYPE_COLOURS[] = { Color.red.brighter(), Color.green, Color.orange, Color.gray,
            Color.cyan, Color.blue.brighter() };

    public static int getRandomType() {
        return (int) Math.floor(Math.random() * MAX_TYPES);
    }

    public int getRandomCodonType() {
        return (int) Math.floor(Math.random() * (MAX_TYPES - 2)) + 2;
    }

    public int getRandomState() {
        return (int) Math.floor(Math.random() * MAX_STATES);
    }

    public SimulationCellModel(int t, int s) {
        if (t < 0 || t >= MAX_TYPES)
            throw new Error("SquirmCellProperties::SquirmCellProperties : type not in valid range");
        type = t;
        state = s;
    }

    public Color getColour() {
        return TYPE_COLOURS[type];
    }

    public int getType() {
        return type;
    }

    public int getState() {
        return state;
    }

    public String getStringType() {
        switch (type) {
        case 0:
            return "e";
        case 1:
            return "f";
        case 2:
            return "a";
        case 3:
            return "b";
        case 4:
            return "c";
        case 5:
            return "d";
        default:
            throw new Error("SquirmCellProperties::getStringType : type out of range");
        }
    }

    public int getTimeSinceLastReaction() {
        return time_since_last_reaction;
    }
    public boolean isType(int t) {
        return type == t;
    }

    public boolean isType(char t) {
        return type == getType(t);
    }

    public boolean isState(int s) {
        return state == s;
    }

    public boolean isTypeAndState(int t, int s) {
        return isType(t) && isState(s);
    }

    public boolean isTypeAndState(char t, int s) {
        return isType(t) && isState(s);
    }

    public void setState(int s) {
        // if this is a change then reset counter
        if (state != s) {
            time_since_last_reaction = 0;
            state = s;
        }
    }

    public static int getType(char t) {
        switch (t) {
        case 'e':
            return 0;
        case 'f':
            return 1;
        case 'a':
            return 2;
        case 'b':
            return 3;
        case 'c':
            return 4;
        case 'd':
            return 5;
        default:
            throw new Error("SquirmCellProperties::getType : unknown type!");
        }
    }
    
    public String toString() {
        return "[Super.SquirmCell : " + getStringType() + state + " / type=" + getStringType() + " state=" + state + "]"; 
    }
    
} // End of the class //