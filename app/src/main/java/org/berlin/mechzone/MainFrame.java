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

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Main wrapper frame around the application.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        super();
    }

    /**
     * Set up the main frame.
     */
    public void setup() {
        // Setup current frame
        this.setLayout(new GridLayout(3, 1));
        this.setTitle("Squirm Artificial Chemistry");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setJMenuBar(this.createMenuBar());
        this.setLocation(20, 20);
        this.setPreferredSize(new Dimension(800, 900));

        this.setFocusable(true);
        this.setVisible(true);

        // Current object is main graphicPanel, add graphic jpanel to graphicPanel
        final Squirm graphicPanel = new Squirm();
        graphicPanel.setLayout(new GridLayout(0, 1));

        graphicPanel.setPreferredSize(new Dimension(800, 600));
        graphicPanel.setVisible(true);

        // Add graphic panel and resize current frame
        this.add(graphicPanel);

        // Add scroll pane under graphic area
        this.add(this.createMessageArea());

        // Add status bar final
        this.add(this.createStatusBar());

        this.resize(800, 900);
        graphicPanel.init();
        graphicPanel.start();
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
     * Create read-only scrollabe message area as jlabel.
     */
    public JScrollPane createMessageArea() {
        final JLabel messages = new JLabel();
        final JScrollPane scrollMessageArea = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollMessageArea.setForeground(Color.black);
        scrollMessageArea.setPreferredSize(new Dimension(800, 80));
        messages.setText("Data...");
        return scrollMessageArea;
    }

    public JPanel createStatusBar() {
        final JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(800, 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("Status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        return statusPanel;
    }

}
