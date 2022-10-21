package lunarlander.data;

import lunarlander.constants.Constants;

public class Rocket {
	private Point position;
	private Point speed;
	private Point gravitation;
	private double acceleration;
	private double orientation;
	private double rotationRate;
	private boolean landed;
	private boolean alive;
	private double scale;

	public enum Direction {
		LEFT, RIGHT
	}

	public Rocket(double gravitation) {
		initialise(gravitation);
	}

	public void reset() {
		initialise(gravitation.getLength());
	}

	private void initialise(double gravitation) {
		this.gravitation = new Point(false, 0, gravitation);
		rotationRate = Constants.DEFAULT_ROTATION_RATE;
		acceleration = rotationRate * gravitation;
		position = new Point(false, 0, 0);
		speed = new Point(false, 0, 0);
		orientation = Constants.DEFAULT_ORIENTATION;
		landed = false;
		alive = true;
	}

	public void accelerate() {
		speed.addVector(new Point(true, acceleration, orientation * Math.PI / 180));
	}

	public void steer(Direction direction) {
		switch (direction) {
		case LEFT:
			orientation += rotationRate * -1;
			break;
		case RIGHT:
			orientation += rotationRate;
			break;
		default:
		}
		orientation = keepDegreesInRange(orientation);
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
		int trueOrientation = (int) this.orientation;
		if (trueOrientation == Constants.DEFAULT_ORIENTATION) {
			return "0";
		}
		if (Constants.DEFAULT_ORIENTATION >= trueOrientation
				&& trueOrientation >= Constants.DEFAULT_ORIENTATION - 180) {
			int value = (int) (keepDegreesInRange((double) trueOrientation - Constants.DEFAULT_ORIENTATION) - 180);
			value = 180 - value;
			return "-" + value;
		}
		return "+" + (int) (keepDegreesInRange((double) trueOrientation - 270));
	}

	public void positionUpdate() {
		speed.addVector(gravitation);
		position.addVector(speed);
	}

	public void sideExit(int leftBorder, int rightBorder) {
		if (getPositionCenter().getX() < leftBorder) {
			position.setX(rightBorder - (double) Constants.DEFAULT_POSITION_OFFSET);
		}
		if (getPositionCenter().getX() > rightBorder) {
			position.setX(leftBorder - (double) Constants.DEFAULT_POSITION_OFFSET);
		}
	}

	public void resetOrientation() {
		if (Constants.DEFAULT_ORIENTATION - 10 < orientation && orientation < Constants.DEFAULT_ORIENTATION + 10) {
			orientation = Constants.DEFAULT_ORIENTATION;
		}
	}

	public Point getPosition() {
		return position;
	}

	public double getOrientation() {
		return this.orientation;
	}

	public Point getPositionCenter() {
		Point center = new Point(position);
		center.addVector(
				new Point(false, Constants.DEFAULT_POSITION_OFFSET * scale, Constants.DEFAULT_POSITION_OFFSET * scale));
		return center;
	}

	public Point getSpeed() {
		return speed;
	}

	/**
	 * returns the velocity as double directly.
	 * 
	 * @return
	 */
	public double getVelocity() {
		return speed.getLength();
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

	public void setScale(double scale) {
		this.scale = scale;
	}

}