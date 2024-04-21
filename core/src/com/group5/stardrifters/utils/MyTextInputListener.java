package com.group5.stardrifters.utils;


import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.utils.Disposable;

public class MyTextInputListener implements TextInputListener {
   @Override
   public void input (String text) {
      System.out.println("Input: " + text);
   }

   @Override
   public void canceled () {
   }
}

