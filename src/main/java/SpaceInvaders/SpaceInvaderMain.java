package SpaceInvaders;

import java.util.ArrayList;
import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.extra.entity.components.ExpireCleanComponent;
import com.almasb.fxgl.extra.entity.components.RandomMoveComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.util.Duration;
import javafx.scene.effect.BlendMode;


public class SpaceInvaderMain extends GameApplication{
	Entity player, entity;
	javafx.scene.image.Image particle;
	final int maxEnemyCount = 10;
	int speed = 5;
    //private ParticleEmitter emitter;
	
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setTitle("Space Invaders");
		settings.setVersion("");
		settings.setManualResizeEnabled(true);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void asteroidExplode(Entity ast, double astXScale, double astYScale) {
		ast.getComponents().clear();
		AnimationChannel explode = new AnimationChannel("Explosions/explosion-6.png", 8, 384/8, 48, Duration.millis(500), 0, 7);
		AnimatedTexture explosion = new AnimatedTexture(explode);		
		explosion.setScaleX(astXScale);
		explosion.setScaleY(astYScale);
		if(explosion != null) ast.getViewComponent().setAnimatedTexture(explosion, true, true);
		else ast.removeFromWorld();
	}
	public void enemyExplode(Entity enemy, double expScaleX, double expScaleY) {
		enemy.removeComponent(RandomMoveComponent.class);
		AnimationChannel explode = new AnimationChannel("Explosions/explosion-4.png", 12, 1536/12, 128, Duration.millis(500), 0, 11);
		AnimatedTexture explosion = new AnimatedTexture(explode);
		explosion.setScaleX(expScaleX);
		explosion.setScaleY(expScaleY);
		if(explosion != null) enemy.getViewComponent().setAnimatedTexture(explosion, false, true);
		else enemy.removeFromWorld();
	}
	@Override
	protected void initPhysics() {
		PhysicsWorld physicsWorld = getPhysicsWorld();
		physicsWorld.addCollisionHandler(new CollisionHandler(SpaceInvaderTypes.LARGEASTEROID, SpaceInvaderTypes.PROJECTILE) {
			public void onCollision(Entity ast, Entity proj) {
				getGameWorld().removeEntity(proj);
				asteroidExplode(ast, 2.5, 2.5);
			}
//			Particle Effects Testing (Didn't use because it slows the thread)
//			
//			public void onCollision(Entity ast, Entity proj) {
//				getGameWorld().removeEntity(ast);
//				getGameWorld().removeEntity(proj);
//				emitter = ParticleEmitters.newExplosionEmitter(100);
//		        emitter.setSize(40, 80);
//		        emitter.setSourceImage(getAssetLoader().loadTexture("explode.png").getImage());
//
//		        entity = Entities.builder()
//		                .at(ast.getCenter().getX()-50, ast.getCenter().getY()-50)
//		                .with(new ParticleControl(emitter))
//		                .buildAndAttach();
//		        getMasterTimer().runOnceAfter(() -> {
//		        		getGameWorld().removeEntity(entity);
//		        }, Duration.millis(200));
//			}
		});
		physicsWorld.addCollisionHandler(new CollisionHandler(SpaceInvaderTypes.ASTEROID, SpaceInvaderTypes.PROJECTILE) {
			public void onCollision(Entity ast, Entity proj) {
				getGameWorld().removeEntity(proj);
				asteroidExplode(ast, 1, 1);
			}
		});
		physicsWorld.addCollisionHandler(new CollisionHandler(SpaceInvaderTypes.ENEMY, SpaceInvaderTypes.PROJECTILE) {
			public void onCollision(Entity enemy, Entity proj) {
				getGameWorld().removeEntity(proj);
				if(enemy.getComponent(HealthComponent.class).isDead()) {
					enemyExplode(enemy, .5, .5);
					getGameState().increment("enemyCount", -1);
				}else {
					enemy.getComponent(HealthComponent.class).takeDamage(1);
					enemy.setView(getAssetLoader().loadTexture("SpaceInvaders/enemyShip.png").multiplyColor(Color.PALEVIOLETRED));
				}
			}
		});
		physicsWorld.addCollisionHandler(new CollisionHandler(SpaceInvaderTypes.ASTEROID, SpaceInvaderTypes.ENEMY) {
			public void onCollision(Entity ast, Entity enemy) {
				getGameWorld().removeEntity(enemy);
				asteroidExplode(ast, 1.5, 1.5);
				getGameState().increment("enemyCount", -1);
			}
		});
		physicsWorld.addCollisionHandler(new CollisionHandler(SpaceInvaderTypes.LARGEASTEROID, SpaceInvaderTypes.ENEMY) {
			public void onCollision(Entity ast, Entity enemy) {
				getGameWorld().removeEntity(enemy);
				asteroidExplode(ast, 2.7, 2.7);
				getGameState().increment("enemyCount", -1);
			}
		});
	}
	@Override
	protected void initGame() {
		//set entity factory because I capitalized the package name disabling annotations
		getGameWorld().addEntityFactory(new SpaceInvaderFactory());
		//set global player entity to the spawn of factories player function
		player = getGameWorld().spawn("player", getWidth()/2, getHeight()-75);
//		Entity enemy = getGameWorld().spawn("enemy", getWidth()/2, getHeight()/2);
//		enemy.addControl(new PointAtEntityControl(player));
//		getGameWorld().spawn("enemy", getWidth()/2, getHeight()/2);
//		Entity enemy = getGameWorld().spawn("enemy", getWidth()/2, getHeight()/2);
//		testing
//		enemy.addControl(new PointAtEntityControl(player));
//		Texture a = getAssetLoader().loadTexture("SpaceInvaders/enemyShip.png").multiplyColor(Color.DARKGOLDENROD);
//		enemy.setView(a);
		//player.getViewComponent().turnOnDebugBBox(true);
		//getGameScene().getViewport().bindToFit(getWidth()/2, getHeight()/2, player);
		getMasterTimer().runAtInterval(() ->{
			getGameWorld().spawn("small asteroid", FXGLMath.random(0, getWidth()), -50);
			getGameWorld().spawn("large asteroid", FXGLMath.random(0, getWidth()), -100);
		}, Duration.seconds(FXGLMath.random(5, 10)));
		getMasterTimer().runAtInterval(() ->{
			enemySpawn();
		}, Duration.millis(500));
	}
	protected void enemySpawn() {
		if(!getGameState().getInt("enemyCount").equals(maxEnemyCount)) {
			/*Entity a = */getGameWorld().spawn("enemy", getGameState().getInt("enemyCount") % 2 == 0  ? -100 : getWidth()+100, FXGLMath.random(0, getHeight()/2));
//			a.addControl(new PointAtEntityControl(player));
			getGameState().increment("enemyCount", +1);
		}
	}
	@Override
	protected void initUI() {
		//set background color
				getGameScene().addGameView(new EntityView(new Rectangle(getWidth(),getHeight(), Color.rgb(51, 0, 51))), RenderLayer.BACKGROUND);
				
		Texture[] stars = {getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starBig.png"),
				getAssetLoader().loadTexture("SpaceInvaders/Background/starSmall.png")};
		for(int i = 0; i < stars.length-1; i++) {
			EntityView toAdd = new EntityView(stars[FXGLMath.random(0, stars.length-1)]);
			toAdd.setTranslateX(Math.random()*getWidth());
			toAdd.setTranslateY(Math.random()*getHeight());
			getGameScene().addGameView(toAdd, RenderLayer.BACKGROUND);
		}	
	}
	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("enemyCount", 0);
	}
	
	boolean done = true;
	@Override
	protected void initInput() {
		Input input = getInput();
		input.addAction(new UserAction("shoot") {
			@Override
			protected void onAction() {
				Entity laser = getGameWorld().spawn("laserBeam", (player.getX() + player.getWidth()/2 - 5), player.getY());
				Entity laserBlast = getGameWorld().spawn("laserBlast", (player.getX() + player.getWidth()/2 - 28), player.getY()-25);
//				testing
				//laser.getViewComponent().turnOnDebugBBox(true);
				laser.rotateBy(90);
				input.mockKeyRelease(KeyCode.SPACE);
				getMasterTimer().runOnceAfter(() -> {
					getGameWorld().removeEntity(laserBlast);
				}, Duration.millis(80));
			}
		}, KeyCode.SPACE);
		input.addAction(new UserAction("move left") {
			@Override
			protected void onActionBegin() {
				player.setViewFromTexture("SpaceInvaders/playerLeft.png");
			}
			@Override
			protected void onAction() {
				player.translateX(-1*speed);
			}
			@Override
			protected void onActionEnd() {
				player.setViewFromTexture("SpaceInvaders/player.png");
			}
		}, KeyCode.LEFT);
		input.addAction(new UserAction("move right") {
			@Override
			protected void onActionBegin() {
				player.setViewFromTexture("SpaceInvaders/playerRight.png");
			}
			@Override
			protected void onAction() {
				player.translateX(speed);
			}
			@Override
			protected void onActionEnd() {
				player.setViewFromTexture("SpaceInvaders/player.png");
			}
		}, KeyCode.RIGHT);
		input.addAction(new UserAction("move up") {
			@Override
			protected void onAction() {
				if(player.getY() > 400) {
				player.translateY(-1*speed);
				}
			}
		}, KeyCode.UP);
		input.addAction(new UserAction("move down") {
			@Override
			protected void onAction() {
				player.translateY(speed);
			}
		}, KeyCode.DOWN);
		input.addAction(new UserAction("speed up") {
			@Override
			protected void onAction() {
				particle = getAssetLoader().loadTexture("SpaceInvaders/player.png").getImage();
				Entities.builder()
                .at(player.getCenter().subtract(particle.getWidth() / 2, particle.getHeight() / 2))
                .viewFromNode(new EntityView(new Texture(particle)))
                .renderLayer(RenderLayer.BACKGROUND)
                .with(new ExpireCleanComponent(Duration.seconds(0.2)).animateOpacity())
                .buildAndAttach();
				speed = 10;
			}
			@Override
			protected void onActionEnd() {
				speed = 5;
			}
		}, KeyCode.V);
	}
}
