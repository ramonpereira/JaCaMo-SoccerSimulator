package perception;

import jason.environment.grid.Location;

public class PlayerPercepts extends Percepts {

	private Location location;
	private String role;
	private boolean hasBall;
	private Location ballLocation;

	public PlayerPercepts(String name){
		super(name);
	}
	
	public PlayerPercepts(String name, Location location, String role, String strategy, String team, Location ballLocation){
		super(name);
		
		this.location = location;
		this.role = role;
		this.strategy = strategy;
		this.team = team;
		this.ballLocation = ballLocation;
		
		if(this.location.equals(this.ballLocation)){
			this.hasBall = true;
		} else this.hasBall = false;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isHasBall() {
		return hasBall;
	}

	public void setHasBall(boolean hasBall) {
		this.hasBall = hasBall;
	}

	public Location getBallLocation() {
		return ballLocation;
	}

	public void setBallLocation(Location ballLocation) {
		this.ballLocation = ballLocation;
	}
}
