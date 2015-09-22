package artifacts;

import model.SoccerFieldModel;
import perception.CoachPercepts;
import perception.PlayerPercepts;
import view.SoccerFieldView;
import cartago.AgentId;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;
import enums.Direction;

public class SoccerFieldArtifact extends Artifact {

	private SoccerFieldModel model;
	private SoccerFieldView view;
	private boolean kickingBall = false;
	private boolean matchOver = false;
	private boolean breakTime = false;
	private boolean breakMatch = false;
	private static long TIME_MATCH = 120000;
	//private static long TIME_MATCH = 20000;
	private long startTimeMatch = 0;
	
	@OPERATION
	public void init() {
		this.model = new SoccerFieldModel(this);
		this.view = new SoccerFieldView(this.model);
		this.view.out("Score: " + this.model.getScore());
		this.view.out(this.model.getTeamsFormation());
		view.repaint();
		System.out.println("------ Init: Soccer Artifact ------");
		defineObsProperty("score", this.model.getScore());
		defineObsProperty("ballPosX", this.model.getBallLocation().x);
		defineObsProperty("ballPosY", this.model.getBallLocation().y);
		defineObsProperty("kickingBall", this.kickingBall);
	}
	
	@OPERATION
	public void start(String agentName) {
		AgentId agent = getOpUserId();
		System.out.println("+Start: " + agentName);
		
		if(agentName.contains("player")){
			PlayerPercepts agentPerceps = this.model.getPlayers().get(agentName);
			signal(agent, "initPercepts", agentPerceps.getTeam(), agentPerceps.getRole(), agentPerceps.getStrategy());
			signal(agent, "myPos", agentPerceps.getLocation().x, agentPerceps.getLocation().y);
		} else {
			CoachPercepts agentPerceps = this.model.getCoachs().get(agentName);
			signal(agent, "initPercepts", agentPerceps.getTeam(), agentPerceps.getStrategy(), agentPerceps.getFormation().toString());			
		}
		
		this.startTimeMatch = System.currentTimeMillis();
	}
	
	@OPERATION
	public void moveTo(String dir) {
		this.isBreakTime();
		if(breakTime){
			this.model.breakTime();
			this.breakTime = false;
			return;
		}
		if(matchOver || this.isMatchOver()) return;
		if(kickingBall) return;
		
		AgentId agent = getOpUserId();
		//System.out.println(agent + "-> moveTo[" + dir + "]");
		
		if(dir.equals("random")){
			this.model.moveRandom(agent.getAgentName());
		} else if(dir.equals("right")){
			this.model.move(Direction.RIGHT, agent.getAgentName());
		} else if(dir.equals("left")){
			this.model.move(Direction.LEFT, agent.getAgentName());
		} else if(dir.equals("ball")){
			this.model.moveToBall(agent.getAgentName());
		}
		this.update(agent);
	}
	
	@OPERATION
	public void kickBall(String type, String dir) {
		this.isBreakTime();
		if(breakTime){
			this.model.breakTime();
			this.breakTime = false;
			return;
		}
		if(matchOver || this.isMatchOver()) return;
		if(kickingBall) return;
		
		kickingBall = true;
		ObsProperty kickingBall = getObsProperty("kickingBall");
		kickingBall.updateValue(this.kickingBall);
		
		AgentId agent = getOpUserId();
		//System.out.println("KickBall[" + type + "]: " + dir);

		if(type.equals("away")){
			if(dir.equals("right")) this.model.kickBallAway(Direction.RIGHT, agent.getAgentName());
			else this.model.kickBallAway(Direction.LEFT, agent.getAgentName());
		} else if(type.equals("goal")){
			if(dir.equals("right")) this.model.kickBallToGoal(Direction.RIGHT, agent.getAgentName());
			else this.model.kickBallToGoal(Direction.LEFT, agent.getAgentName());
		}
		ObsProperty ballPosX  = getObsProperty("ballPosX");
		ObsProperty ballPosY  = getObsProperty("ballPosY");
		ballPosX.updateValue(model.getBallLocation().x);
		ballPosY.updateValue(model.getBallLocation().y);
		
		this.kickingBall = false;
		kickingBall.updateValue(this.kickingBall);
		
		this.update(agent);
	}
	
	@OPERATION
	public void changeStrategy() {
		AgentId agent = getOpUserId();
		System.out.println(agent + ": changeStrategy");
		this.model.breakTime();
	}
	
	@OPERATION
	public void getBallX() {
		AgentId agent = getOpUserId();
		signal(agent, "ballPosX", this.model.getBallLocation().x);
	}
	
	@OPERATION
	public void getBallY() {
		AgentId agent = getOpUserId();
		signal(agent, "ballPosY", this.model.getBallLocation().y);
	}
	
	@OPERATION
	public void kickingBall() {
		AgentId agent = getOpUserId();
		signal(agent, "kickingBall", this.kickingBall);
	}
	
	private void update(AgentId agent){
		PlayerPercepts agentPerceps = this.model.getPlayers().get(agent.getAgentName());
		signal(agent, "myPos", agentPerceps.getLocation().x, agentPerceps.getLocation().y);
		
		ObsProperty score = getObsProperty("score");
		score.updateValue(this.model.getScore());
	}
	
	private void isBreakTime(){
		if( (System.currentTimeMillis() - startTimeMatch) >= TIME_MATCH/2 && !this.breakMatch){
			this.startTimeMatch = System.currentTimeMillis();
			this.breakTime = true;
			this.breakMatch = true;
		}
	}
	
	private boolean isMatchOver(){
		if( (System.currentTimeMillis() - startTimeMatch) > TIME_MATCH/2){
			this.model.whoIsTheWinner();
			this.matchOver = true;
			return true;
		}
		return false;
	}
	
	public SoccerFieldModel getModel() {
		return model;
	}
	
	public SoccerFieldView getView() {
		return view;
	}
}