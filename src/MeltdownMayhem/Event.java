package MeltdownMayhem;

import java.util.ArrayList;
import java.util.Random;

import Entity.Enemy;
/**
 * De Event class behandeld momenteel de spawning van de Enemies en zijn Pathfinding.
 * Er is een kans dat deze classe zal verdwijnen in de toekomst.
 */
public class Event {

	static Random rng = new Random();
	
	/** 
	 * Botsing-algoritme: Enemies in elkaars 'botsingsgebied' komen samen in een ArrayList.
	 * Nadien worden de enemies weggeduuwt van hun zwaartepunt, voor elke gevormde ArrayList.
	 */
	public static ArrayList<ArrayList<Enemy>> getEnemiesInCollision(ArrayList<Enemy> enemyList) {
		ArrayList <ArrayList<Enemy>> enemiesInCollision = new ArrayList<ArrayList<Enemy>>();
		boolean isEnemyInCollision = false;
		
		for (int n = 0; n < enemyList.size(); n ++) {
			for (int m = n + 1; m < enemyList.size(); m ++) {
				isEnemyInCollision = false;
				
				if (Extra.distance(enemyList.get(n).x, enemyList.get(n).y, enemyList.get(m).x, enemyList.get(m).y) < Enemy.COLLISION_AREA_FACTOR * (Enemy.margin-10)) {
					
					for (ArrayList<Enemy> L: enemiesInCollision) {
						if (L.contains(enemyList.get(n))) {
							if (L.contains(enemyList.get(m))) {
								isEnemyInCollision = true;
								break;
							}
							L.add(enemyList.get(m));
							isEnemyInCollision = true;
							break;
						} else if (L.contains(enemyList.get(m))) {
							L.add(enemyList.get(n));
							isEnemyInCollision = true;
							break;
						}
					}
					
					if (!isEnemyInCollision) {
						// Maak een nieuwe rij in de geneste ArrayList enemiesInCollision
						enemiesInCollision.add(new ArrayList<Enemy>());
						enemiesInCollision.get(enemiesInCollision.size() - 1).add(enemyList.get(n));
						enemiesInCollision.get(enemiesInCollision.size() - 1).add(enemyList.get(m));
					}
				}
			}
		}
		return enemiesInCollision;
	}
	
	// Push Enemies away from each other to avoid Collision (except if the Enemy is in a rampage, then it pushes others away)
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