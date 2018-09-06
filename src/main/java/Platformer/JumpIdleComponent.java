package Platformer;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;

import javafx.util.Duration;

@Required(PhysicsComponent.class)
public class JumpIdleComponent extends Component {
	double scalePos, scaleNeg;
	Texture jump_up;
	Texture jump_down;
	Texture idle;
	double x_allowance;
	double y_allowance;
	boolean right = true;
	
	@Override
	public void onUpdate(double tpf) {		
		//set left or right
		if(entity.getComponent(PhysicsComponent.class).getVelocityX() < -x_allowance) {
			setScaleAll(scaleNeg);
			right = false;
		}else if(entity.getComponent(PhysicsComponent.class).getVelocityX() > x_allowance) {
			setScaleAll(scalePos);
			right = true;
		}
		
		//jump
		if(entity.getComponent(PhysicsComponent.class).getVelocityY() < -y_allowance) {
			entity.setView(jump_up);
		}
		else if(entity.getComponent(PhysicsComponent.class).getVelocityY() > y_allowance) {
			entity.setView(jump_down);
		}else {
			
			//run animations are handled in initInput as this many animations
			//are not meant to be run in a component without the use of states
			
			//idle
			if(entity.getComponent(PhysicsComponent.class).getVelocityY() < y_allowance &&
					entity.getComponent(PhysicsComponent.class).getVelocityY() > -y_allowance &&
					entity.getComponent(PhysicsComponent.class).getVelocityX() > -x_allowance &&
					entity.getComponent(PhysicsComponent.class).getVelocityX() < x_allowance) {
			
					entity.setView(idle);
		}
		}
	}
	
	public void setYAllowance(double plus_minus_range) {
		y_allowance = plus_minus_range;
	}
	
	public double getYAllowance() {
		return y_allowance;
	}
	public void setXAllowance(double plus_minus_range) {
		x_allowance = plus_minus_range;
	}
	
	public double getXAllowance() {
		return x_allowance;
	}
	
	public void setJumpUpImage(String image_path) {
		jump_up = FXGL.getAssetLoader().loadTexture(image_path);
	}
	
	public void setJumpUpImage(Texture img) {
		jump_up = img;
	}
	
	public void setJumpDownImage(String image_path) {
		jump_down = FXGL.getAssetLoader().loadTexture(image_path);
	}
	
	public void setJumpDownImage(Texture img) {
		jump_down = img;
	}
	
	public void setIdleImage(AnimatedTexture anim) {
		idle = anim;
	}

	public void setIdleImage(Texture img) {
		idle = img;
	}
	
	public void setIdleImage(String image_path) {
		idle = FXGL.getAssetLoader().loadTexture(image_path);
	}
	
	public void setScaleAll(double scaler) {
		if(scaler < 0) {
			scaleNeg = scaler;
			scalePos = -scaler;
		}else {
			scaleNeg = -scaler;
			scalePos = scaler;
		}
		jump_up.setScaleX(scaler);
		jump_down.setScaleX(scaler);
		idle.setScaleX(scaler);
		
		if(scaler > 0) {
			jump_up.setScaleY(scaler);
			jump_down.setScaleY(scaler);
			idle.setScaleY(scaler);
		}
	}
	
	public double getScale() {
		return scalePos;
	}
	
}
