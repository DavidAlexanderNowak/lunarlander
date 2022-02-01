package lunarlander.data;

import java.io.Serializable;

public class Rocket implements Serializable {
	private static final long serialVersionUID = 5L;
	private Point position;
	private Point speed;
	private Point gravitation;
	private double acceleration;
	private double angle;
	private double rotationRate = 2;
	private boolean landed;
	private boolean alive;

	public enum Direction {
		LEFT, RIGHT
	}

	public Rocket(double gravitation) {
		initialise(gravitation);
	}

	private void initialise(double gravitation) {
		this.gravitation = new Point(false, 0, gravitation);
		acceleration = 2 * gravitation;
		position = new Point(false, 0, 0);
		speed = new Point(false, 0, 0);
		angle = 270;
		landed = false;
		alive = true;
	}

	public void accelerate() {
		speed.addVector(new Point(true, acceleration, angle * Math.PI / 180));
	}

	public void steer(Direction direction) {
		switch (direction) {
		case LEFT:
			angle += rotationRate * -1;
			break;
		case RIGHT:
			angle += rotationRate;
			break;
		default:
		}
		angle = keepDegreesInRange(angle);
	}

	private double keepDegreesInRange(double value) {
		if (value > 360) {
			value -= 360;
		}
		if (value < 0) {
			value += 360;
		}
		return value;
	}

	/**
	 * to be used or the HUD
	 * 
	 * @return returns orientation in Range of -180 to 180
	 */
	public String getTrueOrientation() {
		int orientation = (int) angle;
		// 270 to 90 is 0 to 180
		// 90 to 0 and 360 to 270 is 0 to -180
		if (orientation == 270) {
			return "0";
		}
		if (270 >= orientation && orientation >= 90) {
			int value = (int) (keepDegreesInRange(orientation - 270) - 180);
			value = 180 - value;
			return "-" + value;
		}
		return "+" + (int) (keepDegreesInRange(orientation - 270));
	}

	public void positionUpdate() {
		speed.addVector(gravitation);
		position.addVector(speed);
	}

	public void sideExit(int leftBorder, int rightBorder) {
		if (getPositionCenter().getX() < leftBorder) {
			position.setX(rightBorder - (double) 50);
		}
		if (getPositionCenter().getX() > rightBorder) {
			position.setX(leftBorder - (double) 50);
		}
	}

	public void resetOrientation() {
		if (260 < angle && angle < 280) {
			angle = 270;
		}
	}

	public Point getPosition() {
		return position;
	}

	public double getAngle() {
		return this.angle;
	}

	public Point getPositionCenter() {
		Point center = new Point(position);
		center.addVector(new Point(false, 50, 50));
		return center;
	}

	public Point getSpeed() {
		return speed;
	}

	public boolean isLanded() {
		return landed;
	}

	public void setLanded(boolean landed) {
		this.landed = landed;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}