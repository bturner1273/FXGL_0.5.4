package Platformer;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.extra.entity.components.HealthComponent;


import javafx.util.Duration;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Platformer_Factory implements EntityFactory {
	@Spawns("lava")
	public Entity newLava(SpawnData data) {
		return Entities.builder()
				.from(data)
				.type(Platformer_Types.LAVA)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new CollidableComponent(true))
				.with(new PhysicsComponent())
				.build();
	}
	
	@Spawns("")
	public Entity new__(SpawnData data) {
		return Entities.builder().build();
	}
	
	@Spawns("door")
	public Entity newDoor(SpawnData data) {
		return Entities.builder()
				.from(data)
				.type(Platformer_Types.DOOR)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new CollidableComponent(true))
				.build();
	}
	
	@Spawns("platform")
	public Entity newPlatform(SpawnData data) {
		return Entities.builder()
				.from(data)
				.type(Platformer_Types.PLATFORM)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new PhysicsComponent())
				.build(); 
	}
	
	@Spawns("coin")
	public Entity newCoin(SpawnData data) {
		AnimatedTexture coin = new AnimatedTexture(new AnimationChannel("Platformer/coin_sheet.png", 3, 384/3, 128, Duration.millis(300), 0, 2));
		return Entities.builder()
				.at(data.getX()-50, data.getY()-50)
				.type(Platformer_Types.COIN)
				.viewFromAnimatedTexture(coin)
				.bbox(new HitBox(BoundingShape.circle(10)))
				.with(new CollidableComponent(true))
				.build();
	}
	
	@Spawns("player")
	public Entity newPlayer(SpawnData data) {
		AnimationChannel idle = new AnimationChannel("Platformer/stand_sheet.png", 2, 606/2, 431, Duration.millis(300), 0, 1);
		AnimatedTexture pic = new AnimatedTexture(idle);
		pic.setScaleX(.25);
		pic.setScaleY(.25);
		
		HealthComponent health = new HealthComponent(99);
		
		AnimatedTexture run = new AnimatedTexture(new AnimationChannel("Platformer/run_sheet.png", 5, 1535/5, 445, Duration.millis(500), 0, 4));
		run.setScaleX(.25);
		run.setScaleY(.25);
		
		JumpIdleComponent jump = new JumpIdleComponent();
		jump.setJumpDownImage("Platformer/jump-fall.png");
		jump.setJumpUpImage("Platformer/jump-up.png");
		jump.setIdleImage(pic);
		jump.setXAllowance(10);
		jump.setYAllowance(10);
		jump.setScaleAll(.25);
		
		PhysicsComponent physics = new PhysicsComponent();
		BodyDef bd = new BodyDef();
		bd.setType(BodyType.DYNAMIC);
		bd.setFixedRotation(true);
		FixtureDef fd = new FixtureDef();
		fd.setDensity(1f);
		fd.setRestitution(.01f);
		fd.setFriction(20f);
		physics.setBodyDef(bd);
		physics.setFixtureDef(fd);
		physics.addGroundSensor(new HitBox(new Point2D(115, 162), BoundingShape.box(606/8, 431/4)));
		
		return Entities.builder()
				.from(data)
				.viewFromAnimatedTexture(pic)
				.type(Platformer_Types.PLAYER)
				.bbox(new HitBox(new Point2D(115, 162), BoundingShape.box(606/8, 431/4)))
				.with(physics)
				.with(jump)
				.with(health)
				.with(new CollidableComponent(true))
				.build();
	}
	
}
