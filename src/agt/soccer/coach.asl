{ include("common-agents.asl") }

!start.

+!start
	:
		true
	<-
	.my_name(Me);
	!init(Me).
	
+initPercepts(T,S,F)[source(A)]
	:
		true
	<-
		+team(T);
		+strategy(S);
		+formation(F).
		
+!changeStrategy
	:
		breakTime
	<-
		score(R,B)[artifact_id(SoccerField)];
		if(team("red") & B > R){
			changeStrategyOffensive[artifact_id(SoccerField)];
		} else{
			if(R<B){
				changeStrategyDefensive[artifact_id(SoccerField)];	
			}
		}
.
		
		
	