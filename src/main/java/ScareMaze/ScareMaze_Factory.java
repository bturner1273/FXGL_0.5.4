package ScareMaze;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;

public class ScareMaze_Factory implements EntityFactory {
		@Spawns("player")
		public Entity newPlayer(SpawnData data){
			return Entities.builder()
					.with(new simpleTranslateControl())
					.from(data)
					.type(ScareMaze_Types.CIRCLE)
					.with(new CollidableComponent(true))
					.renderLayer(RenderLayer.TOP)
					.viewFromNode(new Circle(30, Color.BLUE))
					.bbox(new HitBox(BoundingShape.circle(25)))
					.build();
		}
		
		@Spawns("mapTile")
		public Entity newMap(SpawnData data){
			return Entities.builder()
					.viewFromNode(new Rectangle(70, 70, Color.RED))
					.bbox(new HitBox(BoundingShape.box(65, 65)))
					.with(new CollidableComponent(true))
					.type(ScareMaze_Types.WALL)
					.from(data)
					.renderLayer(RenderLayer.BACKGROUND)
					.build();
		}
		@Spawns("goal")
		public Entity newGoal(SpawnData data){
			return Entities.builder()
					.viewFromNode(new Rectangle(70, 70, Color.GOLD))
					.from(data)
					.renderLayer(RenderLayer.TOP)
					.bbox(new HitBox(BoundingShape.circle(27)))
					.with(new CollidableComponent(true))
					.type(ScareMaze_Types.GOAL)
					.build();
		}

	}


