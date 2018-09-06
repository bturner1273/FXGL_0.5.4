package SpaceInvaders;

import com.almasb.fxgl.entity.component.Component;

public class HealthComponent extends Component{
private int health;
	
	public HealthComponent(int health) throws IllegalArgumentException{
		if(health <= 0) {
			throw new IllegalArgumentException("Health must be a positive integer");
		}else this.health = health;
	}
	
	public HealthComponent() {
		this.health = 100;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void takeDamage(int damage) {
		if(health - damage > 0) {
			health -= damage;
		}else health = 0;
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean isDead() {
		return (health == 0);
	}
}
