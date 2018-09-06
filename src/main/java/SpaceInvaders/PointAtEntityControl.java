package SpaceInvaders;

import javafx.geometry.Point2D;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class PointAtEntityControl extends Component{
private Entity pointAt;
	
	public PointAtEntityControl(Entity pointAt) {
		this.pointAt = pointAt;
	}

	@Override
	public void onUpdate(double tpf) {
		this.getEntity().getRotationComponent().rotateToVector(pointAt.getCenter().multiply(.25));
		this.getEntity().rotateBy(tpf-52);
	}
}
