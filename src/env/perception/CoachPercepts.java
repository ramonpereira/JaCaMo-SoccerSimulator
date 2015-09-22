package perception;

import enums.Formation;

public class CoachPercepts extends Percepts {
	
	private Formation formation;
	
	public CoachPercepts(String name, Formation formation) {
		super(name);
		this.formation = formation;
	}
	
	public void setFormation(Formation formation) {
		this.formation = formation;
	}
	
	public Formation getFormation() {
		return formation;
	}
}
