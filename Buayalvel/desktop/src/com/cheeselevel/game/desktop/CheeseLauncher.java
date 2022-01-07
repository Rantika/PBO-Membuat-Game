package com.cheeselevel.game.desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.cheeselevel.game.BuayaGame;

public class CheeseLauncher
{
    public static void main (String[] args)
    {
        BuayaGame myProgram = new BuayaGame();
        LwjglApplication launcher = new LwjglApplication(
                myProgram );
    }
}
