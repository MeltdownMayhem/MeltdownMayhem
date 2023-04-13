package MeltdownMayhem;

import java.util.ArrayList;
import java.util.Random;

import Entity.Enemy;
import Entity.RadiationOrb;
import Entity.Rage;

public class Event {
	static Random rng = new Random();
	
	// Enemy Spawning
	public static void spawnEnemy() {
		boolean enoughSpaceToSpawn = false;
		int spawning_x = 0;
		
		while (enoughSpaceToSpawn) {
			spawning_x = 2 * Enemy.margin + rng.nextInt(GamePanel.BOARD_WIDTH - 4 * Enemy.margin) + GamePanel.BOARD_START;
			for (Enemy E: GamePanel.enemyList) {
				if (Extra.distance(spawning_x, -20, E.x, E.y) > 4 * Enemy.enemyRadius) {
					enoughSpaceToSpawn = true;
					break;
				}
			}
		}
		if (rng.nextDouble()> 0.85) {
			GamePanel.enemyList.add(new Rage(spawning_x));
		} else {
			GamePanel.enemyList.add(new RadiationOrb(spawning_x));
		}
	}
	
	// Enemy collision with other Enemies go together in an nested ArrayList
	public static ArrayList<ArrayList<Enemy>> getEnemiesInCollision() {
		ArrayList <ArrayList<Enemy>> enemiesInCollision = new ArrayList<ArrayList<Enemy>>();
		boolean isEnemyInCollision = false;
		
		for (int n = 0; n < GamePanel.enemyList.size(); n ++) {
			for (int m = n + 1; m < GamePanel.enemyList.size(); m ++) {
				isEnemyInCollision = false;
				
				if (Extra.distance(GamePanel.enemyList.get(n).x, GamePanel.enemyList.get(n).y, GamePanel.enemyList.get(m).x, GamePanel.enemyList.get(m).y) < Enemy.COLLISION_AREA_FACTOR * Enemy.enemyRadius) {
					
					for (ArrayList<Enemy> L: enemiesInCollision) {
						if (L.contains(GamePanel.enemyList.get(n))) {
							if (L.contains(GamePanel.enemyList.get(m))) {
								isEnemyInCollision = true;
								break;
							}
							L.add(GamePanel.enemyList.get(m));
							isEnemyInCollision = true;
							break;
						} else if (L.contains(GamePanel.enemyList.get(m))) {
							L.add(GamePanel.enemyList.get(n));
							isEnemyInCollision = true;
							break;
						}
					}
					
					if (!isEnemyInCollision) {
						// Maak een nieuwe rij in de geneste ArrayList enemiesInCollision
						enemiesInCollision.add(new ArrayList<Enemy>());
						enemiesInCollision.get(enemiesInCollision.size() - 1).add(GamePanel.enemyList.get(n));
						enemiesInCollision.get(enemiesInCollision.size() - 1).add(GamePanel.enemyList.get(m));
					}
				}
			}
		}
		return enemiesInCollision;
	}
	
	// Push Enemies away from each other to avoid Collision (except if the Enemy is in a rampage, then IT pushes others away)
	public static void avoidEnemyCollision(ArrayList<ArrayList<Enemy>> enemiesInCollision) {
		int center_x, center_y;
		int speedAfterCollision;
		
		for (ArrayList<Enemy> L: enemiesInCollision) {
			center_x = 0;
			center_y = 0;
			
			for (Enemy E: L) {
				center_x += E.x;
				center_y += E.y;
			}
			center_x /= L.size();
			center_y /= L.size();
			
			for (Enemy E: L) {
				if (E.rampage == false) {
					if (E.spawning) {
						E.vx = E.x - center_x;
						E.vx /= Extra.distance(E.x, E.y, center_x, center_y);
						speedAfterCollision = 1 + rng.nextInt(4);
						E.vx *= speedAfterCollision;
						
						E.vy = 1 + rng.nextInt(3);
						
					} else {
						E.vx = E.x - center_x;
						E.vy = E.y - center_y;
						
						// snelheid wordt genormaliseerd
						E.vx /= Extra.distance(E.x, E.y, center_x, center_y);
						E.vy /= Extra.distance(E.x, E.y, center_x, center_y);
						
						// snelheid wordt nu vermenigvuldigd met een getal tussen 1 en 4;
						speedAfterCollision = 1 + rng.nextInt(4);
						E.vx *= speedAfterCollision;
						E.vy *= speedAfterCollision;
					}
				}
			}
		}
	}
}