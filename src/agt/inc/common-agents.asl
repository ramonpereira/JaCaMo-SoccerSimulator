{ include("common-cartago.asl") }

+!init(Name):
		true
	<-
		lookupArtifact("soccerField", SoccerField);
		focus(SoccerField);
		start(Name)[artifact_id(SoccerField)];
	.
	
{ include("common-moise.asl") }	