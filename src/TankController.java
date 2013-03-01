import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TankController implements KeyListener {
	private TankWorld world;

	public TankController(TankWorld w) {
		world = w;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		
		//Directional Keys
		if (code == KeyEvent.VK_DOWN){
			world.nexttankdir = TankWorld.DOWN;
		} else if (code == KeyEvent.VK_UP){
			world.nexttankdir = TankWorld.UP;
		}
		
		//Shoot button
		if (code == KeyEvent.VK_SPACE){
			world.reloading = false;
			if (world.ammo > 0){
				world.missiles.add(new Missile(world.tankx, world.tanky));
				world.ammo -= 1;
			}
		}
		
		//Reload button
		if (code == KeyEvent.VK_R){
			world.reloading = true;
		}
		
		//Upgrade buttons 1, 2, 3
		if (code == KeyEvent.VK_1 && world.currency >= 1000 && world.ammo_used == false){
			world.ammo_used = true;
			world.ammo = 40;
			world.ammocap = 40;
			world.currency -= 1000;
		}
		if (code == KeyEvent.VK_2 && world.currency >= 3000 && world.strongbullets_used == false){
			world.strongbullets_used = true;
			world.hitpoint = 2;
			world.currency -= 3000;
		}
		if (code == KeyEvent.VK_3 && world.currency >= 7000){
			world.airstrike();
			world.currency -= 7000;
		}
		
		//Start game key
		if (code == KeyEvent.VK_ENTER){
			world.titlescreen = false;
		}

	}

	@Override
	public void keyReleased(KeyEvent ke) {
		int code = ke.getKeyCode();
		if (code == KeyEvent.VK_DOWN){
			world.nexttankdir = TankWorld.STILL;
		}
		if (code == KeyEvent.VK_UP){
			world.nexttankdir = TankWorld.STILL;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
