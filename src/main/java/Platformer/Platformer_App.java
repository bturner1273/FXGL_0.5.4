package Platformer;

import java.util.Map;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.extra.entity.components.HealthComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Platformer_App extends GameApplication {
	Entity player;
	Text health;
	int jumpNum = 5;
	boolean level2 = false;
	final int PLAYER_SPEED = 400;

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setTitle("Platformer");
		settings.setVersion("");
		settings.setWidth(15*70);
		settings.setHeight(7*70);
	}
	
	@Override
	protected void initPhysics() {
		PhysicsWorld physicsWorld = getPhysicsWorld();
		physicsWorld.addCollisionHandler(new CollisionHandler(Platformer_Types.PLAYER, Platformer_Types.COIN) {
			@Override 
			public void onCollision(Entity player, Entity coin) {
				coin.removeFromWorld();
				getGameState().increment("score", 1);
			}
		});
		
		physicsWorld.addCollisionHandler(new CollisionHandler(Platformer_Types.PLAYER, Platformer_Types.LAVA) {
			@Override
			public void onCollision(Entity player, Entity lava) {
				player.getComponent(HealthComponent.class).setValue(player.getComponent(HealthComponent.class).getValue()-1);
				if(player.getComponent(HealthComponent.class).getValue() == 0) player.removeFromWorld();
				
			}
		});
		physicsWorld.addCollisionHandler(new CollisionHandler(Platformer_Types.PLAYER, Platformer_Types.DOOR) {
			@Override
			public void onCollision(Entity player, Entity door) {
				int health_val = player.getComponent(HealthComponent.class).getValue();
				if(!level2) {	
					level2 = true;
					getGameWorld().setLevelFromMap("map_2.tmx");
					spawnPlayer(health_val);
				}else {
					getGameWorld().clear();
					Text winText = new Text();
					winText.setFont(Font.font ("Arial Bold", 75));
					winText.textProperty().set("Victory Royal!");
					winText.setFill(Color.BLACK);
					winText.setTranslateX(getWidth()/2-250);
					winText.setTranslateY(getHeight()/2);
					getGameScene().addUINode(winText);
				}
			}
		});
	}
	
	protected void spawnPlayer(int health_val) {
		player = getGameWorld().spawn("player", 200, 1300);
		player.getComponent(HealthComponent.class).setValue(health_val);
		health.textProperty().bind(player.getComponent(HealthComponent.class).valueProperty().asString());
		getGameScene().getViewport().bindToEntity(player, getWidth()/2-150, getHeight()/2-200);
	}
	
	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("score", 0);
	}
	
	@Override
	protected void initUI() {
		getGameScene().setBackgroundColor(Color.LIGHTBLUE);
		Text score = new Text();
		score.setFont(Font.font ("Arial Bold", 35));
		score.textProperty().bind(getGameState().intProperty("score").asString());
		score.setFill(Color.BLACK);
		score.setTranslateX(getWidth()-50);
		score.setTranslateY(getHeight()-15);
		getGameScene().addUINode(score);
		Text score_label = new Text();
		score_label.textProperty().set("Score:");
		score_label.setFont(Font.font ("Arial Bold", 35));
		score_label.setFill(Color.BLACK);
		score_label.setTranslateX(getWidth()-150);
		score_label.setTranslateY(getHeight()-15);
		getGameScene().addUINode(score_label);
		health = new Text();
		health.setFont(Font.font ("Arial Bold", 35));
		health.textProperty().bind(player.getComponent(HealthComponent.class).valueProperty().asString());
		health.setFill(Color.BLACK);
		health.setTranslateX(getWidth()-50);
		health.setTranslateY(getHeight()-45);
		getGameScene().addUINode(health);
		Text health_label = new Text();
		health_label.setFont(Font.font ("Arial Bold", 35));
		health_label.textProperty().set("Health:");
		health_label.setFill(Color.BLACK);
		health_label.setTranslateX(getWidth()-165);
		health_label.setTranslateY(getHeight()-45);
		getGameScene().addUINode(health_label);
	}
	
	@Override
	protected void initInput() {
		AnimatedTexture running = new AnimatedTexture(
				new AnimationChannel("Platformer/run_sheet.png", 5, 1535/5, 445, Duration.millis(500), 0, 4));
		AnimatedTexture idle = new AnimatedTexture(
				new AnimationChannel("Platformer/stand_sheet.png", 2, 606/2, 431, Duration.millis(300), 0, 1));
		
		
		Input input = getInput();
		input.addAction(new UserAction("run_right") {
			@Override
			protected void onActionBegin() {
				running.setScaleX(.25);
				running.setScaleY(.25);
				player.getViewComponent().setAnimatedTexture(running, true, false);
			}
			@Override
			protected void onAction() {
				if(player.getComponentOptional(PhysicsComponent.class).isPresent())
				player.getComponent(PhysicsComponent.class).setVelocityX(PLAYER_SPEED);
			}
			@Override
			protected void onActionEnd() {
				idle.setScaleX(.25);
				idle.setScaleY(.25);
				player.getViewComponent().setAnimatedTexture(idle, true, false);
			}
		}, KeyCode.RIGHT);
		
		input.addAction(new UserAction("run_left") {
			@Override
			protected void onActionBegin() {
				running.setScaleX(-.25);
				running.setScaleY(.25);
				player.getViewComponent().setAnimatedTexture(running, true, false);
			}
			@Override
			protected void onAction() {
				if(player.getComponentOptional(PhysicsComponent.class).isPresent())
				player.getComponent(PhysicsComponent.class).setVelocityX(-PLAYER_SPEED);
			}
			@Override
			protected void onActionEnd() {
				idle.setScaleX(-.25);
				idle.setScaleY(.25);
				player.getViewComponent().setAnimatedTexture(idle, true, false);
			}
		}, KeyCode.LEFT);
		
		input.addAction(new UserAction("jump") {
			@Override
			protected void onAction() {
				if(jumpNum > 0) {
					jumpNum--;
					player.getComponent(PhysicsComponent.class).setVelocityY(-500);
				}
			}
		}, KeyCode.UP);
	}
	
	@Override
	protected void initGame() {
		getGameWorld().addEntityFactory(new Platformer_Factory());
		getGameWorld().setLevelFromMap("map_1.tmx");
		player = getGameWorld().spawn("player", 100, 100);
		getGameScene().getViewport().bindToEntity(player, getWidth()/2-150, getHeight()/2-200);
		
		getMasterTimer().runAtInterval(() -> {
			if(player.getComponentOptional(PhysicsComponent.class).isPresent()) {
				if(player.getComponent(PhysicsComponent.class).isOnGround()) {
					jumpNum = 5;
				}
			}
		}, Duration.millis(30));
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
