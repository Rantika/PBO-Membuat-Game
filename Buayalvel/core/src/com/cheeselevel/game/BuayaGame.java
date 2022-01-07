package com.cheeselevel.game;

import com.badlogic.gdx.Game;



public class BuayaGame extends Game {
    @Override
    public void create() {
        setScreen(new BuayaMenu(this));
    }
}
