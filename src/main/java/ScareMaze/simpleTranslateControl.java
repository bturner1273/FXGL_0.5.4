package ScareMaze;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.entity.components.PositionComponent;

@Required(PositionComponent.class)
public class simpleTranslateControl extends Component {
	// note that this component is injected automatically
	private PositionComponent position;
	private double speed = 0;

	@Override
	public void onUpdate(double tpf) {
		// TODO Auto-generated method stub
		speed = tpf * 60;
	}

	public void up() {
		position.translateY(-5 * speed);
	}

	public void down() {
		position.translateY(5 * speed);
	}

	public void left() {
		position.translateX(-5 * speed);
	}

	public void right() {
		position.translateX(5 * speed);
	}
}