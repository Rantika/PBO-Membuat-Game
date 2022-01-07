package com.cheeselevel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;

public class BuayaLevel implements Screen {
	public Stage mainStage;
	private Stage uiStage;
	private AnimatedActor buayay;
	private BaseActor ikan;
	private BaseActor floor;
	private BaseActor winText;
	private boolean win;

	private float timeElapsed;
	private Label timeLabel;
	// game world dimensions
	final int MAPWIDTH = 800;
	final int MAPHEIGHT = 800;
	// window dimensions
	final int VIEWWIDTH = 640;
	final int VIEWHEIGHT = 480;
	private final int SPEED = 300;

	public Game game;
	public BuayaLevel(Game g)
	{
		game = g;
		create();
	}

	public void create(){
		timeElapsed = 0 ;
		BitmapFont font = new BitmapFont();
		String text = "Time: 0 ";
		LabelStyle style = new LabelStyle(font ,Color.NAVY );
		timeLabel = new Label(text,style);
		timeLabel.setFontScale(2);
		timeLabel.setPosition(500,440);

		win = false;
		mainStage = new Stage();
		uiStage = new Stage();


		floor = new BaseActor();
		floor.setTexture(new Texture(Gdx.files.internal("tiles-800-800.jpeg")));
		floor.setPosition(0, 0);
		mainStage.addActor(floor);

		ikan = new BaseActor();
		ikan.setTexture(new Texture(Gdx.files.internal("cheese.png")));
		ikan.setOrigin(ikan.getWidth()/2, ikan.getHeight()/2);
		ikan.setPosition(400,300);
		mainStage.addActor(ikan);
		uiStage.addActor(timeLabel);
		buayay = new AnimatedActor();

		TextureRegion[] frames = new TextureRegion[4];
		for (int n = 0; n < 4; n++)
		{
			String fileName = "mouse" + n + ".png";
			Texture tex = new Texture(Gdx.files.internal(fileName));
			tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			frames[n] = new TextureRegion( tex );
		}
		Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);

		Animation anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);

		buayay.setAnimation( anim );
		buayay.setOrigin( buayay.getWidth()/2, buayay.getHeight()/2 );
		buayay.setPosition( 20, 20 );
		mainStage.addActor(buayay);

		winText = new BaseActor();
		winText.setTexture(new Texture(Gdx.files.internal("you-win.png")));
		winText.setPosition(170,60);
		winText.setVisible(false);
		uiStage.addActor(winText);


	}

	@Override
	public void render(float delta){
		buayay.velocityX = 0;
		buayay.velocityY = 0;

		if (Gdx.input.isKeyPressed(Keys.LEFT))
			buayay.velocityX -= SPEED;
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			buayay.velocityX += SPEED;
		else if (Gdx.input.isKeyPressed(Keys.UP))
			buayay.velocityY += SPEED;
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
			buayay.velocityY -= SPEED;

		float dt = Gdx.graphics.getDeltaTime();
		mainStage.act(dt);
		uiStage.act(dt);
		if(!win){
			timeElapsed += dt;
			timeLabel.setText( "Time: " + (int)timeElapsed );
		}

		if ( buayay.getX() > MAPWIDTH  - buayay.getWidth()){
			buayay.setX(MAPWIDTH-buayay.getWidth());
		}

		Rectangle ikanRectangle = ikan.getBoundingRectangle();
		Rectangle buayayRectangle = buayay.getBoundingRectangle();


		if (!win && ikanRectangle.overlaps(buayayRectangle))
		{

			win = true;
			Action spinShrinkFadeOut = Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360, 1),
					Actions.scaleTo(0,0,1),
					Actions.fadeOut(1)
			);
			ikan.addAction(spinShrinkFadeOut);

			Action fadeInColorCycleForever  = Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever(Actions.sequence(

							Actions.color(new Color(1,0,0,1),1),
							Actions.color(new Color(0,0,1,1),1)
							)
					)

			);
			winText.addAction(fadeInColorCycleForever);
		}

		//draw graphics
		Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Camera cam = mainStage.getCamera();
		// center camera on player
		cam.position.set( buayay.getX() + buayay.getOriginX(),
				buayay.getY() + buayay.getOriginY(), 0 );
		// bound camera to layout
		cam.position.x = MathUtils.clamp(cam.position.x,
				VIEWWIDTH/2, MAPWIDTH - VIEWWIDTH/2);
		cam.position.y = MathUtils.clamp(cam.position.y,
				VIEWHEIGHT/2, MAPHEIGHT - VIEWHEIGHT/2);
		cam.update();
		mainStage.draw();
		buayay.setX( MathUtils.clamp( buayay.getX(), 0, MAPWIDTH
				- buayay.getWidth() ));
		buayay.setY( MathUtils.clamp( buayay.getY(), 0, MAPHEIGHT
				- buayay.getHeight() ));
		if (Gdx.input.isKeyPressed(Keys.M))
			game.setScreen( new BuayaMenu(game) );

		uiStage.draw();

	}
	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
