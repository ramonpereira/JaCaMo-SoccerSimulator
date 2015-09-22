{ include("common-agents.asl") }

/* Beliefs */ 

// ### Roles - Defender or Midfielder or Striker
//role("df"). || role("mf"). || role("sf").

// ### Team - Red or Blue
// team("red"). || team("blue").

// ### Strategies - Attacking || Defending
//strategy("attacking"). || strategy("defending").

/* ######################### */

/* Rules - Agent Zone - Defense */ 
agentInZone(X) :- role("df") & team("red") & X <= 11.
agentInZone(X) :- role("df") & team("blue") & X >= 39.

/* Rules - Agent Zone - Middle */
agentInZone(X) :- role("mf") & (X > 8 & X < 41).

/* Rules - Agent Zone - Attack */
agentInZone(X) :- role("sf") & team("blue") & X <= 7.
agentInZone(X) :- role("sf") & team("red") & X >= 42.

/* ######################### */

/* Rules - Ball in Agent Zone - Defense */
ballInAgentZone(XBall) :- role("df") & team("red") & XBall <= 11.
ballInAgentZone(XBall) :- role("df") & team("blue") & XBall >= 39.

/* Rules - Ball in Agent Zone - Midfielder */
ballInAgentZone(XBall) :- role("mf") & (XBall > 8 & XBall < 41).

/* Rules - Ball in Agent Zone - Attack */
ballInAgentZone(XBall) :- role("sf") & team("blue") & XBall <= 7.
ballInAgentZone(XBall) :- role("sf") & team("red") & XBall >= 42.

/* ######################### */

/* -------------------------- */

!start.

+!start
	:
		true
	<-
		.my_name(Me);
		!init(Me)
	.
	
+initPercepts(T,R,S)[source(A)]
	<-
		+team(T);
		+role(R);
		+strategy(S);
	.
	
+ballPosX(X)[source(A)]
	:
		A \== self
	<-
		-ballPosX(_);
		+ballPosX(X);
	.
	
+ballPosY(Y)[source(A)]
	:
		A \== self
	<-
		-ballPosY(_);
		+ballPosY(Y);
	.

+kickingBall(K)[source(A)]
	:
		A \== self
	<-
		-kickingBall(_);
		+kickingBall(K);
	.	

+myPos(X,Y)[source(A)]
	:
		agentInZone(X)
	<-
		getBallX[artifact_id(SoccerField)];
		getBallY[artifact_id(SoccerField)];
		?ballPosX(XBall);
		?ballPosY(YBall);
		
		if(ballInAgentZone(XBall)){
			if(XBall == X & YBall == Y){
				kickingBall[artifact_id(SoccerField)];
				?kickingBall(K);
				if(not K){
					!kickBall;	
				}
				-ballPosX(_);
				-ballPosY(_);
				-kickingBall(_);
			} else{
				!moveTo(ball);
			} 
		} else {
			!moveTo(random);
		}
		.

+myPos(X,Y)[source(A)]
	:
		 role("sf") & not agentInZone(X)
	<- 
		getBallX[artifact_id(SoccerField)];
		getBallY[artifact_id(SoccerField)];
		?ballPosX(XBall);
		?ballPosY(YBall);
		if(XBall == X & YBall == Y){
			kickingBall[artifact_id(SoccerField)];
			?kickingBall(K);
			if(not K){
				!kickBall;	
			}
			-ballPosX(_);
			-ballPosY(_);
			-kickingBall(_);
		} else {
			if(team("red")){
				!moveTo(right)	
			} else {
				!moveTo(left)
			}	
		}
		.

+myPos(X,Y)[source(A)]
	<- 
		!moveTo(random)
		.

+!kickBall
	:
		strategy("defending") & ( role("df") | role("mf") ) 
	<-
		!kickBallAway.
	
+!kickBall
	:
		role("sf") | role("mf") 
	<-
		!kickBallToGoal.	

+!kickBall
	<-
		!kickBallAway.
	
+!moveTo(D)
	<-
		moveTo(D) [artifact_id(SoccerField)].

+!kickBallAway
	: 
		team("blue")
	<-
		kickBall(away, left) [artifact_id(SoccerField)].

+!kickBallAway
	: 
		team("red")
	<-
		kickBall(away, right) [artifact_id(SoccerField)].
		
+!kickBallToGoal
	:
		team("red")
	<-
		kickBall(goal, right) [artifact_id(SoccerField)].
		
+!kickBallToGoal
	:
		team("blue")
	<-	
		kickBall(goal, left) [artifact_id(SoccerField)].	