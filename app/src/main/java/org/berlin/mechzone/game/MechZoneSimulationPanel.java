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

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;

import java.awt.geom.Path2D;

/**
 * Main Class for JFrame Squirm Java Graphics Component.
 */
public class MechZoneSimulationPanel extends JPanel
        implements Runnable, MouseListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(MechZoneSimulationPanel.class);

    private static final String NEWLINE = System.getProperty("line.separator");

    private Thread gameThread = null;

    protected GameGraphicsGrid gameGrid;

    protected final int gridSizeX = 50;
    protected final int gridSizeY = 50;
    protected final int drawingSizeX = 800;
    protected final int drawingSizeY = 600;
    protected final float scale = drawingSizeX / (float) gridSizeX;

    protected Image offscreenImage = null;
    protected Graphics off_g = null;

    private static final int FAST = 1;
    private int delay = 240;
    private boolean paused = false;
    private int draw_every = 1;

    private String error_msg;
    private boolean error_thrown = false;

    private String inspect_msg = "x";
    private int inspect_msg_x = 20, inspect_msg_y = 20;

    private String current_cell;
    private long counter = 0;

    private JTextArea textArea;

    public MechZoneSimulationPanel() {
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        //this.setFocusTraversalKeysEnabled(false);
        this.requestFocusInWindow();

        try {
            gameGrid = new GameGraphicsGrid(gridSizeX, gridSizeY);
        } catch (Error e) {
            error_thrown = true;
            error_msg = e.getMessage();
        }
        error_msg = new String();
    }

    public void togglePaused() {
        paused = !paused;
    }

    public void setDelay(int d) {
        delay = d;
    }

    public void setDrawOnlyEvery(int every) {
        draw_every = every;
    }

    public void removeAllReactions() {
        error_msg = "";
    }

    /**
     * The init method is called by the AWT when an applet is first loaded or
     * reloaded.
     */
    public void init() {

        Dimension size = this.getSize();
        LOGGER.info("Getting size at init: " + size);
        resize(drawingSizeX, drawingSizeY);
        size = this.getSize();

        if (offscreenImage == null) {
            offscreenImage = createImage(drawingSizeX, drawingSizeY);
            // may need gjt.Util.waitForImage(this, offscreenImage);
            off_g = offscreenImage.getGraphics();
        }
        this.addKeyListener(this);
        this.setFocusable(true);
        //this.setFocusTraversalKeysEnabled(false);
        this.requestFocusInWindow();

    }

    public void destroy() {
    }

    public int x = 20;
    public int y = 20;

    /**
     * Squirm Paint Handler, set background, render cells
     */
    public void paint(final Graphics g) {
        if (off_g == null) {
            return;
        }
        // Clear the background
        off_g.setColor(Color.white);
        off_g.fillRect(0, 0, drawingSizeX, drawingSizeY);

        // Draw the cells
        gameGrid.draw(off_g, scale, delay <= FAST);

        // draw grid after elements
        for (int i = 0; i < 40; i++) {
            off_g.drawLine((i * 10), 0, (i * 10), drawingSizeY);

            off_g.drawLine(0, (i * 10), drawingSizeX, (i * 10));
        }

        final Graphics2D g2 = (Graphics2D) off_g;

        // Render player:
        final Path2D myPath = new Path2D.Double();
        double firstX = ((30 / 2.0) * (1 - 1 / Math.sqrt(3)));
        double firstY = (3.0 * 30 / 4.0);
        myPath.moveTo(firstX+x, firstY+y);
        myPath.lineTo(30 - firstX+x, firstY+y);
        myPath.lineTo(30 / 2.0+x, (30 / 4.0)+y);
        myPath.closePath();
        g2.fill(myPath);  // fill my triangle

        // Show the result.
        g.drawImage(offscreenImage, 0, 0, this);
        counter++;
        if ((counter % 100) == 0) {
            LOGGER.info("Counter update : value=" + counter);
        }
    }

    public void update(Graphics g) {
        paint(g);
        if (error_thrown) {
            g.drawString(error_msg, 10, 100);
        }
        g.drawString(inspect_msg, inspect_msg_x, inspect_msg_y);
        g.drawString(current_cell, 20, 120);
    }

    /**
     * The start() method is called when the page containing the applet first
     * appears on the screen.
     */
    public void start() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /**
     * The stop() method is called when the page containing the applet is no
     * longer on the screen.
     */
    public void stop() {
        if (gameThread != null) {
            gameThread = null;
        }
    }

    /**
     * THREAD SUPPORT The run() method is called when the applet's thread is
     * started.
     */
    public void run() {
        while (true) {
            try {
                try {
                    if (!paused) {
                        gameGrid.doTimeStep();
                    }
                } catch (Error e) {
                    error_msg = e.getMessage();
                    error_thrown = true;
                }
                if (gameGrid.getCount() % draw_every == 0) {
                    repaint();
                }
                Thread.sleep(delay);
            } catch (final InterruptedException e) {
                stop();
            }
        }
    }

    public void setTextArea(final JTextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * MOUSE SUPPORT: The mouseMove() method is called if the mouse cursor moves
     * over the applet's portion of the screen and the mouse button isn't being
     * held down.
     */
    public boolean mouseMove(Event evt, int x, int y) {
        // find which slot we're pointing at
        int slot_x = (int) ((float) x / scale);
        int slot_y = (int) ((float) y / scale);
        inspect_msg = gameGrid.getContents(slot_x, slot_y);
        inspect_msg_x = x;
        inspect_msg_y = y - 3;
        return true;
    }

    void eventOutput(final String eventDescription, final MouseEvent e) {
        if (textArea == null) {
            return;
        }
        textArea.append(eventDescription + " detected on "
                + e.getComponent().getClass().getName()
                + " " + e.getX() + " " + e.getY()
                + "." + NEWLINE);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void mousePressed(final MouseEvent e) {
        eventOutput("Mouse pressed (# of clicks: "
                + e.getClickCount() + ")", e);
    }

    public void mouseReleased(final MouseEvent e) {
        eventOutput("Mouse released (# of clicks: "
                + e.getClickCount() + ")", e);
    }

    public void mouseEntered(final MouseEvent e) {
        eventOutput("Mouse entered", e);
    }

    public void mouseExited(final MouseEvent e) {
        eventOutput("Mouse exited", e);
    }

    public void mouseClicked(final MouseEvent e) {
        eventOutput("Mouse clicked (# of clicks: "
                + e.getClickCount() + ")", e);
    }

    @Override
    public void keyTyped(final KeyEvent ke) {
        System.out.println(">>>> enter key");
    }

    @Override
    public void keyPressed(final KeyEvent ke) {
        final int c = ke.getKeyCode();
        System.out.println(">>>> " + c);
        if (c == KeyEvent.VK_LEFT) {
            x -= 5;
        }
        if (c == KeyEvent.VK_RIGHT) {
            x += 5;
        }
        if (c == KeyEvent.VK_UP) {
            y -= 5;
        }
        if (c == KeyEvent.VK_DOWN) {
            y += 5;
        }
        repaint();
        System.out.println(">>>> " + x);
    }

    @Override
    public void keyReleased(final KeyEvent ke) {
    }

} // End of the class //
