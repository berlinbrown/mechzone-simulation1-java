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
package org.berlin.mechzone;

import org.apache.log4j.Logger;

// SquirmReaction.java

class Reaction {

    private static final Logger LOGGER = Logger.getLogger(Reaction.class);
    
    public char us_type;
    public int us_state;
    public boolean current_bond;
    public char them_type;
    public int them_state;
    public int future_us_state;
    public boolean future_bond;
    public int future_them_state;

    public Reaction(char us_type, int us_state, boolean current_bond, char them_type, int them_state,
                    int future_us_state, boolean future_bond, int future_them_state) {
        this.us_type = us_type;
        this.us_state = us_state;
        this.current_bond = current_bond;
        this.them_type = them_type;
        this.them_state = them_state;
        this.future_us_state = future_us_state;
        this.future_bond = future_bond;
        this.future_them_state = future_them_state;        
    }
};