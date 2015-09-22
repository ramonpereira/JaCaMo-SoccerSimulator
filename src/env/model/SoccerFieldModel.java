package model;

import jason.environment.grid.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import artifacts.SoccerFieldArtifact;
import perception.CoachPercepts;
import perception.PlayerPercepts;
import enums.Direction;
import enums.Formation;
import enums.Team;

public class SoccerFieldModel {
		
	private SoccerFieldArtifact soccerArtifact;
	
	public static long SLEEP = 5;
	
	public static int NUM_PLAYERS = 20;
	public static int NUM_COACHS = 2;
	
	public static int GRID_WIDTH = 50;
	public static int GRID_HEIGHT = 25;
	
	private Location ballLocation = new Location((GRID_WIDTH-1)/2, GRID_HEIGHT/2);
	private Map<String, PlayerPercepts> players;
	private Map<String, CoachPercepts> coachs;
	
	private String teamsFormation = "";
	private boolean startMatchRed = true;
	
	private boolean kickingBall = false;
	
    private int scoreRed = 0;
    private int scoreBlue = 0;
    private String score;
    
    public SoccerFieldModel(SoccerFieldArtifact artifact) {
    	this.soccerArtifact = artifact;
    	this.score = scoreRed + "x" + scoreBlue;
    	
    	this.players = new HashMap<String, PlayerPercepts>();
    	for(int i=1; i<=NUM_PLAYERS; i++)
    		this.players.put("player"+i, new PlayerPercepts("player"+i));
    	
    	this.coachs = new HashMap<String, CoachPercepts>();
    	CoachPercepts perceptsCoach1 = new CoachPercepts("coach", System.currentTimeMillis() % 2 == 0 ? Formation.attack1 : Formation.attack2);
    	perceptsCoach1.setStrategy("attacking");
    	perceptsCoach1.setTeam("red");
    	
    	CoachPercepts perceptsCoach2 = new CoachPercepts("coach_1", System.currentTimeMillis() % 5 == 0 ? Formation.defensive2 : (System.currentTimeMillis() % 3 == 0 ? Formation.defensive3 : Formation.defensive4));
    	perceptsCoach2.setStrategy("defending");
    	perceptsCoach2.setTeam("blue");
    	this.coachs.put("coach", perceptsCoach1);
    	this.coachs.put("coach_1", perceptsCoach2);
    	
    	startTeamsMatch();
    }
    
    public void startTeamsMatch(){
    	CoachPercepts coachRed = this.coachs.get("coach");
    	CoachPercepts coachBlue= this.coachs.get("coach_1");
    	
    	this.defineFormation(Team.RED, coachRed.getFormation());
    	this.defineFormation(Team.BLUE, coachBlue.getFormation());
    	
    	this.ballLocation = new Location((GRID_WIDTH-1)/2, GRID_HEIGHT/2);
    	
    	if(startMatchRed) startMatch(Team.BLUE);
    	else startMatch(Team.RED);
    	
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void startTeamsBreakTime(){
    	this.ballLocation = new Location((GRID_WIDTH-1)/2, GRID_HEIGHT/2);
    	
    	if(startMatchRed) startMatch(Team.BLUE);
    	else startMatch(Team.RED);
    	
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private void defineFormation(Team team, Formation formation){
    	switch (formation) {
			case attack1:
				this.formationAttack1Positions(team);
				break;
			case attack2:
				this.formationAttack2Positions(team);
				break;
			case defensive1:
				this.formationDefensive1Positions(team);
				break;
			case defensive2:
				this.formationDefensive2Positions(team);
				break;
			case defensive3:
				this.formationDefensive3Positions(team);
				break;
			case defensive4:
				this.formationDefensive4Positions(team);
				break;
		}
    }

    public void startMatch(Team team){
    	if(team == Team.RED){
    		PlayerPercepts percepts9 = this.players.get("player6");
    		percepts9.setLocation(new Location((GRID_WIDTH-1)/2, GRID_HEIGHT/2));
    		
    		PlayerPercepts percepts10 = this.players.get("player10");
    		percepts10.setLocation(new Location(((GRID_WIDTH-1)/2)-1, (GRID_HEIGHT/2)-1));
    	} else {
    		PlayerPercepts percepts9 = this.players.get("player16");
    		percepts9.setLocation(new Location((GRID_WIDTH-1)/2, GRID_HEIGHT/2));
    		
    		PlayerPercepts percepts10 = this.players.get("player20");
    		percepts10.setLocation(new Location(((GRID_WIDTH-1)/2)-1, (GRID_HEIGHT/2)-1));
    	}
    }
    
    /*Attack Positions*/
    
    /*3-4-3*/
    public void formationAttack1Positions(Team team){
    	int i = 0;
    	int index = getFormationParameters(team)[0];
    	int xDef  = getFormationParameters(team)[1];
    	int xMid  = getFormationParameters(team)[2];
    	int xAtt  = getFormationParameters(team)[3];
    	int pos = 0;
    	String playerTeam = "";
		if(team == Team.BLUE) playerTeam = "blue";
		else playerTeam = "red";
    	
    	while (i < 10){
    		if(i==3) pos = 2;
    		if(i==7) pos = 3;

    		PlayerPercepts percepts = this.players.get("player"+(index));
    		percepts.setTeam(playerTeam);
    		percepts.setBallLocation(this.ballLocation);
    		percepts.setStrategy("attacking");
    		
    		if(i < 3){
    			percepts.setLocation(new Location(xDef, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("df");
    			pos+=6;
    		} else if(i<7){
    			percepts.setLocation(new Location(xMid, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("mf");
    			pos+=3;
    		} else {
    			percepts.setLocation(new Location(xAtt, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("sf");
    			pos+=3;
    		}
    		index++;
    		i++;
    	}
    	teamsFormation += "\n -> Team: " + team + "\n\tFormation:3-4-3";
    }
    
    /*3-3-4*/
    public void formationAttack2Positions(Team team){
    	int i = 0;
    	int index = getFormationParameters(team)[0];
    	int xDef  = getFormationParameters(team)[1];
    	int xMid  = getFormationParameters(team)[2];
    	int xAtt  = getFormationParameters(team)[3];
    	int pos = 0;
    	String playerTeam = "";
		if(team == Team.BLUE) playerTeam = "blue";
		else playerTeam = "red";
    	
    	while (i < 10){
    		if(i==3) pos = 2;
    		if(i==6) pos = 1;
    		
    		PlayerPercepts percepts = this.players.get("player"+(index));
    		percepts.setTeam(playerTeam);
    		percepts.setBallLocation(this.ballLocation);
    		percepts.setStrategy("attacking");
    		
    		if(i < 3){
    			percepts.setLocation(new Location(xDef, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("df");
    			pos+=6;
    		} else if(i<6){
    			percepts.setLocation(new Location(xMid, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("mf");
    			pos+=4;
    		} else {
    			percepts.setLocation(new Location(xAtt, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("sf");
    			pos+=4;
    		}
    		index++;
    		i++;
    	}
    	teamsFormation += "\n -> Team: " + team + "\n\tFormation:3-3-4";
    }
   
    /*-------- Defensive Positions --------*/
    
    /*5-4-1*/
    public void formationDefensive1Positions(Team team){
    	int i = 0;
    	int index = getFormationParameters(team)[0];
    	int xDef  = getFormationParameters(team)[1];
    	int xMid  = getFormationParameters(team)[2];
    	int xAtt  = getFormationParameters(team)[3];
    	int pos = 1;
    	String playerTeam = "";
		if(team == Team.BLUE) playerTeam = "blue";
		else playerTeam = "red";
    	
    	while (i < 10){
    		if(i==5) pos = 1;
    		if(i==9) pos = 7;
    		
    		PlayerPercepts percepts = this.players.get("player"+(index));
    		percepts.setTeam(playerTeam);
    		percepts.setBallLocation(this.ballLocation);
    		percepts.setStrategy("defending");
    		
    		if(i < 5){
    			percepts.setLocation(new Location(xDef, pos+(GRID_HEIGHT/6)));
    			percepts.setRole("df");
    			pos+=4;
    		} else if(i < 9) {
    			percepts.setLocation(new Location(xMid, pos+(GRID_HEIGHT/6)));
    			percepts.setRole("mf");
    			pos+=4;
    		} else {
    			percepts.setLocation(new Location(xAtt, pos+(GRID_HEIGHT/6)));
    			percepts.setRole("sf");
    		}
    		index++;
    		i++;
    	}
    	teamsFormation += "\n -> Team: " + team + "\n\tFormation:5-4-1";
    }

    /*5-3-2*/
    public void formationDefensive2Positions(Team team){
    	int i = 0;
    	int index = getFormationParameters(team)[0];
    	int xDef  = getFormationParameters(team)[1];
    	int xMid  = getFormationParameters(team)[2];
    	int xAtt  = getFormationParameters(team)[3];
    	int pos = 0;
    	String playerTeam = "";
		if(team == Team.BLUE) playerTeam = "blue";
		else playerTeam = "red";
    	
    	while (i < 10){
    		if(i==5) pos = 2;
    		if(i==8) pos = 3;
    		
    		PlayerPercepts percepts = this.players.get("player"+(index));
    		percepts.setTeam(playerTeam);
    		percepts.setBallLocation(this.ballLocation);
    		percepts.setStrategy("defending");
    		
    		if(i < 5){
    			percepts.setLocation(new Location(xDef, pos+(GRID_HEIGHT/6)));
    			percepts.setRole("df");
    			pos+=4;
    		} else if(i < 8) {
    			percepts.setLocation(new Location(xMid, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("mf");
    			pos+=5;
    		} else{
    			percepts.setLocation(new Location(xAtt, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("sf");
    			pos+=7;
    		}

    		index++;
    		i++;
    	}
    	teamsFormation += "\n -> Team: " + team + "\n\tFormation:5-3-2";
    }
    
    /*4-4-2*/
    public void formationDefensive3Positions(Team team){
    	int i = 0;
    	int index = getFormationParameters(team)[0];
    	int xDef  = getFormationParameters(team)[1];
    	int xMid  = getFormationParameters(team)[2];
    	int xAtt  = getFormationParameters(team)[3];
    	int pos = 0;
    	String playerTeam = "";
		if(team == Team.BLUE) playerTeam = "blue";
		else playerTeam = "red";
    	
    	while (i < 10){
    		if(i==4) pos = 2;
    		if(i==8) pos = 3;
    		
    		PlayerPercepts percepts = this.players.get("player"+(index));
    		percepts.setTeam(playerTeam);
    		percepts.setBallLocation(this.ballLocation);
    		percepts.setStrategy("defending");
    		
    		if(i < 4){
    			percepts.setLocation(new Location(xDef, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("df");
    			pos+=5;
    		} else if(i < 8) {
    			percepts.setLocation(new Location(xMid, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("mf");
    			pos+=5;
    		} else{
    			percepts.setLocation(new Location(xAtt, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("sf");
    			pos+=4;
    		}
    		index++;
    		i++;
    	}
    	teamsFormation += "\n -> Team: " + team + "\n\tFormation:4-4-2";
    }
    
    /*4-5-1*/
    public void formationDefensive4Positions(Team team){
    	int i = 0;
    	int index = getFormationParameters(team)[0];
    	int xDef  = getFormationParameters(team)[1];
    	int xMid  = getFormationParameters(team)[2];
    	int xAtt  = getFormationParameters(team)[3];
    	int pos = 0;
    	String playerTeam = "";
		if(team == Team.BLUE) playerTeam = "blue";
		else playerTeam = "red";
		
    	while (i < 10){
    		if(i==4) pos = 2;
    		if(i==8) pos = 3;
    		
    		PlayerPercepts percepts = this.players.get("player"+(index));
    		percepts.setTeam(playerTeam);
    		percepts.setBallLocation(this.ballLocation);
    		percepts.setStrategy("defending");
    		
    		if(i < 4){
    			percepts.setLocation(new Location(xDef, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("df");
    			pos+=5;
    		} else if(i < 8) {
    			percepts.setLocation(new Location(xMid, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("mf");
    			pos+=4;
    		} else{
    			percepts.setLocation(new Location(xAtt, pos+(GRID_HEIGHT/5)));
    			percepts.setRole("sf");
    			pos+=7;
    		}
    		index++;
    		i++;
    	}
    	teamsFormation += "\n -> Team: " + team + "\n\tFormation:4-5-1";
    }
    
    /**
     * @param Team team
     * @return int[]: [0] index - [1] xDef - [2] xMid - [3] xAtt
     */
    private int[] getFormationParameters(Team team){
    	int[] parameters = new int[]{0,0,0,0};
    	
    	/* [0] index - [1] xDef - [2] xMid - [3] xAtt */
    	parameters[0] = (team == Team.RED ? 1 : 11);
    	parameters[1] = (team == Team.RED ?  5 : 44);
    	parameters[2] = (team == Team.RED ?  11 : 38);
    	parameters[3] = (team == Team.RED ?  16 : 33);
    	
    	return parameters;
    }
    
    /**
     * Move to Ball
     * @param agId
     */
    public void moveToBall(String agent) {
    	this.sleep();
    	Random rand = new Random();
    	int moveTo = rand.nextInt(10);
    	if(moveTo < 6){
    		moveRandom(agent);
    	} else {
    		int xBall = ballLocation.x;
        	int yBall = ballLocation.y;
        	PlayerPercepts agentPercepts = this.players.get(agent);
            Location agentLocation = agentPercepts.getLocation();
        	
            if (agentLocation.x < xBall)
                agentLocation.x++;
            else if (agentLocation.x > xBall)
                agentLocation.x--;
            if (agentLocation.y < yBall)
                agentLocation.y++;
            else if (agentLocation.y > yBall)
                agentLocation.y--;
            
            this.soccerArtifact.getView().update();
    	}
    }
    
    /**
     * Action Move - Random
     * @param agent
     */
    public void moveRandom(String agent){
    	Random random = new Random();
        int move = random.nextInt(4);
        if(move == 0){
        	this.move(Direction.UP, agent);
        } else if(move==1){
        	this.move(Direction.DOWN, agent);
        } else if(move==2){
        	this.move(Direction.RIGHT, agent);
        } else this.move(Direction.LEFT, agent);
    }

    /**
     * Action Move
     * @param Direction dir
     * @param agent
     */
    public void move(Direction dir, String agent) {
    	this.sleep();
    	Location l = this.getAgentLocation(agent);
    	PlayerPercepts agentPercepts = this.players.get(agent);
		switch (dir) {
			case UP:
				if(l.y - 1 < 0){
					agentPercepts.setLocation(new Location(l.x, l.y + 1));
				} else agentPercepts.setLocation(new Location(l.x, l.y - 1)); 
		      	break;
		    case DOWN:
		    	if(l.y + 1 >= GRID_HEIGHT-1) {
		    		agentPercepts.setLocation(new Location(l.x, l.y - 1));
		    	} else agentPercepts.setLocation(new Location(l.x, l.y + 1)); 
		    	break;
		    case RIGHT:
		    	if(l.x + 1 >= GRID_WIDTH-1){
		    		agentPercepts.setLocation(new Location(l.x - 1, l.y));
		    	} else agentPercepts.setLocation(new Location(l.x + 1, l.y));
		    	break;
		    case LEFT:
		    	if(l.x - 1 < 0){
		    		agentPercepts.setLocation(new Location(l.x + 1, l.y));
		    	} else agentPercepts.setLocation(new Location(l.x - 1, l.y)); 
		    	break;
		}
		this.soccerArtifact.getView().update();
    }
    
    /**
     * KickBall - ToGoal
     * @param dir
     * @param agent
     */
    public void kickBallToGoal(Direction dir, String agent){
    	Location agentLocation = this.getAgentLocation(agent);
    	int agentY = agentLocation.y;
    	int yBall = -999;
    	
    	if(agentY>=8 && agentY<=15){
    		yBall = 0;
    	} else if(agentY < 7){
    		yBall = 1;
    	} else if(agentY > 16){
    		yBall = -1;
    	} else {
    		yBall = -999;
    	}
    	
    	kickBall(dir, agent, yBall);
    }

    /**
     * KickBall - Away
     * @param dir
     * @param agent
     */
    public void kickBallAway(Direction dir, String agent){
    	kickBall(dir, agent, -999);
    }
    
    /**
     * KickBall - Generic
     * @param dir
     * @param agent
     * @param y
     */
    private void kickBall(Direction dir, String agent, int y){
    	this.sleepBall();
    	
    	if(this.kickingBall){
    		moveRandom(agent);
    		return;
    	}
    	
    	this.kickingBall = true;    	
    	Random rand = new Random();
    	int power = rand.nextInt(10);
    	if(power < 3) power = 3;
    	
    	Location currentBallLocation = getBallLocation();
    	
    	if( dir == Direction.RIGHT && ((currentBallLocation.x + power) >= (GRID_WIDTH-2) ) ){
    		power = power + ((currentBallLocation.x + power) - (GRID_WIDTH-2) );
    	} else if( dir == Direction.LEFT && (currentBallLocation.x - power <= 0) ){
    		power = power + (currentBallLocation.x - power);
    	}
    	
    	int yDir = 0;
    	if(y == -999){
    		int randDir = rand.nextInt(2);
    		if(randDir == 0){
    			yDir = 0;
    		} else if(randDir == 1){
    			yDir = + 1;
    		} else yDir = - 1;
    	} else yDir = y;

		if(currentBallLocation.y >= GRID_HEIGHT-10) yDir = -1;
		else if(currentBallLocation.y == 4) yDir = +1;
		
    	while(power > 0){
    		currentBallLocation = getBallLocation();
    		Location newLocation = new Location(dir == Direction.RIGHT ? currentBallLocation.x+1 : currentBallLocation.x-1, (currentBallLocation.y+yDir < 0 || currentBallLocation.y+yDir > GRID_HEIGHT-1) ? currentBallLocation.y : currentBallLocation.y+yDir);
    		this.setBallLocation(newLocation);
    		power = power - 1;        		
    		this.sleepBall();
    		this.soccerArtifact.getView().update();
    		
    		if(isGoal()){
    			startTeamsMatch();
    			kickingBall = false;
    			return;
    		}
    	}
    	this.sleepBall();
    	this.kickingBall = false;
    	this.sleepBall();
    	moveRandom(agent);
    }
    
    boolean isGoal(){
    	int ballX = this.ballLocation.x;
    	int ballY = this.ballLocation.y;
    	
    	/*BLUE GOAL - LEFT*/
    	if( (ballX==49) && (ballY>7 && ballY<16)){
    		scoreRed++;
    		score = scoreRed + "x" + scoreBlue;
    		this.soccerArtifact.getView().out("REEEEDDDDS ;-D!");
    		this.soccerArtifact.getView().out("GOOOOOOOOOOOOOOOOOOOOAAALLLLLLLLLLLLLLLLL!");
    		this.soccerArtifact.getView().out("Score: " + score);
    		this.soccerArtifact.getView().out("");
    		startMatchRed = false;
    		return true;
    	
    	/*RED GOAL - RIGHT*/
    	} else if( (ballX==0) && (ballY>7 && ballY<16)){
    		scoreBlue++;
    		score = scoreRed + "x" + scoreBlue;
    		this.soccerArtifact.getView().out("BLUUUEEEES ;-D!");
    		this.soccerArtifact.getView().out("GOOOOOOOOOOOOOOOOOOOOAAALLLLLLLLLLLLLLLLL!");
    		this.soccerArtifact.getView().out("Score: " + score);
    		this.soccerArtifact.getView().out("");
    		startMatchRed = true;
    		return true;
    	}
    	return false;
    }

    public void sleepBall(){
    	try {
			Thread.sleep(SLEEP*2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    public void sleep(){
    	try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void breakTime(){
    	this.soccerArtifact.getView().out("Score: " + this.getScore());
    	this.soccerArtifact.getView().out("-> BREAK TIME! WAIT...");
    	if(this.scoreRed > this.scoreBlue){
    		this.defineFormation(Team.BLUE, Formation.attack1);
    		this.defineFormation(Team.RED, System.currentTimeMillis() % 5 == 0 ? Formation.defensive2 : (System.currentTimeMillis() % 3 == 0 ? Formation.defensive3 : Formation.defensive4));
    		this.soccerArtifact.getView().out("\n-> THE COACHS CHANGED THE FORMATION OF THE TEAMS:");
    		this.soccerArtifact.getView().out("\n -> REDS ARE NOW IN DEFENSIVE FORMATION!" + "\n\tFormation:5-4-1");
    		this.soccerArtifact.getView().out("\n -> BLUES ARE NOW IN OFFENSIVE FORMATION!" + "\n\tFormation:3-4-3");
    	} else if(this.scoreBlue > this.scoreRed){
    		this.defineFormation(Team.BLUE, System.currentTimeMillis() % 5 == 0 ? Formation.defensive2 : (System.currentTimeMillis() % 3 == 0 ? Formation.defensive3 : Formation.defensive4));
    		this.defineFormation(Team.RED, Formation.attack1);
    		this.soccerArtifact.getView().out("\n-> THE COACHS CHANGED THE FORMATION:");
    		this.soccerArtifact.getView().out("\n -> REDS ARE NOW IN OFFENSIVE FORMATION!" + "\n\tFormation:3-4-3");
    		this.soccerArtifact.getView().out("\n -> BLUES ARE NOW IN DEFENSIVE FORMATION!" + "\n\tFormation:5-4-1");
    	} else {
    		this.soccerArtifact.getView().out("-> THE COACHES DO NOT CHANGE THE FORMATION!");
    	}
    	this.startTeamsBreakTime();
    	this.soccerArtifact.getView().out("Score: " + this.getScore());
    	this.soccerArtifact.getView().out("");
    	try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void whoIsTheWinner(){
    	if(this.scoreRed == this.scoreBlue){
    		this.soccerArtifact.getView().out("\n-> DRAW GAME! :-| :-| :-|");
    		this.soccerArtifact.getView().out("\t" + this.getScore());
    	} else if(this.scoreRed > this.scoreBlue){
    		this.soccerArtifact.getView().out("\n-> REEDDDDDSSSSS WINNNNN ;-D <o/ \\o> \\o/");
    		this.soccerArtifact.getView().out("\t" + this.getScore());
    	} else {
    		this.soccerArtifact.getView().out("\n-> BLLLUUUUEEES WINNNNN ;-D <o/ \\o> \\o/");
    		this.soccerArtifact.getView().out("\t" + this.getScore());
    	}
    }
    
    public String getScore() {
		return score;
	}
    
    public String getTeamsFormation() {
		return teamsFormation;
	}
    
    public Location getAgentLocation(String agent){
    	PlayerPercepts percepts = this.players.get(agent);
        Location agentLocation = percepts.getLocation();
        return agentLocation;
    }
    
    public Map<String, PlayerPercepts> getPlayers() {
		return players;
	}
    
    public Map<String, CoachPercepts> getCoachs() {
		return coachs;
	}
    
    public void setBallLocation(Location ballLocation) {
		this.ballLocation = ballLocation;
	}
    
    public Location getBallLocation() {
		return ballLocation;
	}
}
