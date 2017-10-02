package com.tpprod.stupor;

import java.util.*;
import java.awt.event.*;

public class KeyListen implements KeyListener
{
    public ArrayList<Integer> currentKeys = new ArrayList<Integer>(); 
    
    private boolean keyPressed;
    
    public void keyTyped(KeyEvent e) {
    }
    
    public void keyReleased(KeyEvent e) {
        currentKeys.remove(currentKeys.indexOf(e.getKeyCode()));
    }
    
    public void keyPressed(KeyEvent e) {
        keyPressed = false;
        for (int item : currentKeys) {
            if (e.getKeyCode() == item) {
                keyPressed = true;
            }
        }
        if (!keyPressed) {
            currentKeys.add(e.getKeyCode());
        }
    }
    
}
