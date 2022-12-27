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
package org.berlin.mechzone;

import org.berlin.mechzone.game.MechZoneSimulationPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Main wrapper frame around the application.
 */
public class MainFrame extends JFrame {

    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 900;

    private static final int GRAPHIC_HEIGHT = 600;

    private static final int WIDTH_BUFFER = 30;

    private final JTextArea messages = new JTextArea();

    public MainFrame() {
        super();
    }

    /**
     * Set up the main frame.
     */
    public void setup() {
        // Setup current frame
        this.setTitle("Mech Simulation");
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setJMenuBar(this.createMenuBar());
        this.setLocation(20, 20);
        this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        this.setFocusable(true);
        this.setVisible(true);

        // Current object is main graphicPanel, add graphic jpanel to graphicPanel
        final MechZoneSimulationPanel graphicPanel = this.simulation();

        // Add graphic panel and resize current frame
        this.add(graphicPanel);

        // Add scroll pane under graphic area
        this.add(this.createMessageArea());

        graphicPanel.setTextArea(this.messages);

        // Add status bar final
        this.add(this.createStatusBar());

        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        graphicPanel.init();
        graphicPanel.start();
    }

    public MechZoneSimulationPanel simulation() {
        final MechZoneSimulationPanel graphicPanel = new MechZoneSimulationPanel();
        graphicPanel.setPreferredSize(new Dimension(FRAME_WIDTH, GRAPHIC_HEIGHT));
        graphicPanel.setVisible(true);
        return graphicPanel;
    }

    /**
     * Factory method to Create the MenuBar.
     */
    public JMenuBar createMenuBar() {
        final JMenuBar menuBar;
        final JMenu menu;
        final JMenuItem menuItem;

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription("File Option");
        menuBar.add(menu);
        menuItem = new JMenuItem("Open", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Open a file");
        menuItem.setActionCommand("open");
        menu.add(menuItem);

        return menuBar;
    }

    /**
     * Create read-only scrollable message area as jlabel.
     */
    public JScrollPane createMessageArea() {
        messages.setEditable(false);
        final JScrollPane scrollMessageArea = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollMessageArea.setForeground(Color.black);
        scrollMessageArea.setPreferredSize(new Dimension(FRAME_WIDTH-WIDTH_BUFFER, 180));
        messages.setText("Messages...");
        return scrollMessageArea;
    }

    public JPanel createStatusBar() {
        final JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(FRAME_WIDTH-WIDTH_BUFFER, 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        final JLabel statusLabel = new JLabel("Status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        return statusPanel;
    }

}
