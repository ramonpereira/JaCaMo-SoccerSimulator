/* auxiliary plans for Moise agents */

@l_focus_on_my_scheme[atomic]
+schemes(L)[workspace(_,_,W)]
	: true 
<-
	cartago.set_current_wsp(W);
	for ( .member(S,L) ) {
		lookupArtifact(S,ArtId);
		focus(ArtId)
	}
	.

+!jcm__initial_roles([],_).
+!jcm__initial_roles(L,0) <- .print("Error with inicial roles ",L).

@l_focus_org_art[atomic]
+!jcm__initial_roles([role(O,H,G,R)|T],Try)
	: true
   <- !join_workspace(O,H);
   	 .print("Lookup for art ", G);
      lookupArtifact(G,GId);
      +jcm__art(G,GId);
      focus(GId);
      adoptRole(R)[artifact_id(GId)];
      !jcm__initial_roles(T,Try);
      .
      
-!jcm__initial_roles(L,Try)
	: true
<-
	.wait(100);
	!jcm__initial_roles(L,Try-1);
	.

/* Organisational Plans Required by all agents */

// plans to handle obligations
// obligation to commit to a mission
+obligation(Ag,Norm,committed(Ag,Mission,Scheme),Deadline)[artifact_id(ArtId)]
	: .my_name(Ag)
<- 
	commitMission(Mission)[artifact_id(ArtId)];
	. 

// obligation to achieve a goal      
+obligation(Ag,Norm,achieved(Scheme,Goal,Ag),Deadline)[artifact_id(ArtId)]
	: .my_name(Ag)
<-
	!Goal[scheme(Scheme)];
	goalAchieved(Goal)[artifact_id(ArtId)];
	.

// an unknown type of obligation was received
+obligation(Ag,Norm,What,DeadLine)  
	: .my_name(Ag)
<-
	.print("I am obliged to ",What,", but I don't know what to do!");
	.