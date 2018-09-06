package SpaceInvaders;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.AttractableComponent;
import com.almasb.fxgl.extra.entity.components.AttractorComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.extra.entity.components.OffscreenCleanComponent;
import com.almasb.fxgl.extra.entity.components.ProjectileComponent;
import com.almasb.fxgl.extra.entity.components.RandomMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;  

public class SpaceInvaderFactory implements EntityFactory{
	@Spawns("enemy")
	public Entity newEnemy(SpawnData data) {
		HealthComponent health = new HealthComponent(1);
		return Entities.builder()
				.from(data)
				.type(SpaceInvaderTypes.ENEMY)
				.with(new CollidableComponent(true))
				.viewFromTexture("SpaceInvaders/enemyShip.png")
				.with(new RandomMoveComponent(200,200))
				.with(health)
				.with(new AttractableComponent(50))
				.bbox(new HitBox(BoundingShape.box(98, 50)))
				.renderLayer(RenderLayer.TOP)
				.build();
	}

	@Spawns("player")
	public Entity newPlayer(SpawnData data) {
		return Entities.builder()
				.from(data)
				.with(new CollidableComponent(true))
				.bbox(new HitBox(BoundingShape.box(99, 75)))
				.type(SpaceInvaderTypes.PLAYER)
				.viewFromTexture("SpaceInvaders/player.png")
				.renderLayer(RenderLayer.TOP)
				.with(new AttractableComponent(70))
				.with(new KeepOnScreenComponent(true,true))
				.build();
	}
	
	@Spawns("small asteroid")
	public Entity newSmallAsteroid(SpawnData data) {
		return Entities.builder()
				.from(data)
				.with(new CollidableComponent(true))
				.type(SpaceInvaderTypes.ASTEROID)
				.with(new ProjectileComponent(new Point2D(FXGLMath.random(-1, 1), 1), FXGLMath.random(150, 500)))
				.viewFromTexture("SpaceInvaders/meteorSmall.png")
				.bbox(new HitBox(BoundingShape.box(44, 42)))
				.with(new AttractorComponent(150,150))
				.renderLayer(RenderLayer.TOP)
				.build();
				
	}
	
	@Spawns("large asteroid")
	public Entity newLargeAsteroid(SpawnData data) {
		return Entities.builder()
				.from(data)
				.viewFromTexture("SpaceInvaders/meteorBig.png")
				.with(new ProjectileComponent(new Point2D(FXGLMath.random(-1, 1), 1), FXGLMath.random(150, 500)))
				.type(SpaceInvaderTypes.LARGEASTEROID)
				.bbox(new HitBox(BoundingShape.box(136, 111)))
				.with(new CollidableComponent(true))
				.with(new OffscreenCleanComponent())
				.with(new AttractorComponent(300,300))
				.renderLayer(RenderLayer.TOP)
				.build();
	}
	
	@Spawns("laserBeam")
	public Entity newLaserBeam(SpawnData data) {
		return Entities.builder()
				.from(data)
				.with(new CollidableComponent(true))
				.with(new ProjectileComponent(new Point2D(0,-1), 500))
				.bbox(new HitBox(BoundingShape.box(9, 33)))
				.viewFromTexture("SpaceInvaders/laserGreen.png")
				.type(SpaceInvaderTypes.PROJECTILE)
				.with(new OffscreenCleanComponent())
				.renderLayer(RenderLayer.BACKGROUND)
				.build();
	}
	
	@Spawns("laserBlast")
	public Entity newLaserBlast(SpawnData data) {
		return Entities.builder()
				.from(data)
				.type(SpaceInvaderTypes.PROJECTILE)
				.viewFromTexture("SpaceInvaders/laserGreenShot.png")
				.with(new CollidableComponent(true))
				.renderLayer(RenderLayer.TOP)
				.bbox(new HitBox(BoundingShape.box(56, 54)))
				.build();
	}
}
