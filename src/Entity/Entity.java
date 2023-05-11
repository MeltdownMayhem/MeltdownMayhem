package Entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import MeltdownMayhem.Extra;

/**
 * The Entity class is a Superclass for all classes with moving elements.
 * Subclasses: Enemy, Human, Drone, Ammunition, Barrel, PowerUp
 */
public abstract class Entity {

	public int x, y;
	public double vx, vy;
	public int width, height, lives;
	protected double SPEED_COEFFICIENT = 1; // Changing the speed of all non-player entities
	
	// Hitbox
	public Rectangle hitbox;
	public int hitboxRadius = 0; // If 0, not a circle-shaped-hitbox, so the collision will look for a Rectangle.
	protected boolean hasSpawnProtection;
	
	// Every entity has its own counter and number for the getImage function (image_animations)
	int spriteCounter = 0;
	int spriteNum = 0;
	
	// Technical variables
	static Random rng = new Random();
	
	// Default update method when not overwritten in subclasses
	public void update() {
		this.x += (int)(SPEED_COEFFICIENT * vx);
		this.y += (int)(SPEED_COEFFICIENT * vy);
	}
	
	//-----------------------------------IMAGE_ANIMATIONS-----------------------------------
	// Give it a list of images and a list of time-intervals, and it will return you the image that needs to be drawn
	public BufferedImage getImage(List<BufferedImage> imageList, List<Integer> timeIntervalList) {
		spriteCounter++;
		
		if (spriteCounter >= timeIntervalList.get(spriteNum)) {
			spriteNum++;
			spriteCounter = 0;
			if (spriteNum >= imageList.size()) {
				spriteNum = 0;
			}
		}
		return imageList.get(spriteNum);
	}
	
	//------------------------------------ENTITY_COLLISIONS----------------------------------
	// Between 2 rectangles
	public boolean rectCollision(Entity entity) {
		return this.hitbox.intersects(entity.hitbox);
	}
	// Between 2 circles
	public boolean radiusCollision(Entity entity) {
		return Extra.distance(entity.x, entity.y, this.x, this.y) < this.hitboxRadius + entity.hitboxRadius;
	}
	// Between a circle and a rectangle
	public boolean radiusRectCollision(Entity entity) {
		return (this.x - this.hitboxRadius < entity.hitbox.x + entity.hitbox.width && this.x + this.hitboxRadius > entity.hitbox.x && this.y - this.hitboxRadius < entity.hitbox.y + entity.hitbox.height && this.y + this.hitboxRadius > entity.hitbox.y);
	}
	
	// Decides what shapes the Entity-hitboxes are and checks for Collision
	public boolean collision(Entity entity) {
		if (this.hitboxRadius == 0) {
			if (entity.hitboxRadius == 0) {
				return this.rectCollision(entity);
			} else {
				return entity.radiusRectCollision(this);
			}
		} else {
			if (entity.hitboxRadius == 0) {
				return this.radiusRectCollision(entity);
			} else {
				return this.radiusCollision(entity);
			}
		}
	}
}