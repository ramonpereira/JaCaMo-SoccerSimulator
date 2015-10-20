// Set of common plans for cartago

// the goal !focus_env_art is produced by the JaCaMo launcher for the agent to focus on initial artifacts
+!jcm__focus_env_art([],_).
+!jcm__focus_env_art(L,0)   <- .print("Error focusing on environment artifacts ",L).

@lf_env_art[atomic]
+!jcm__focus_env_art([art_env(W,H,A)|T],Try)
	: true
<-
	!in_ora4mas; 
	if (A \== "") {
		lookupArtifact(A,AId);
		+jcm__art(A,AId);
		focus(AId);
	}
	!jcm__focus_env_art(T,Try);
	.
	
-!jcm__focus_env_art(L,Try)
	: true
<-
	.wait(100);
	!jcm__focus_env_art(L,Try-1);
	.

+!in_ora4mas : in_ora4mas.
+!in_ora4mas : .intend(in_ora4mas)
   <- .wait({+in_ora4mas},100,_); 
      !in_ora4mas.
@lin[atomic]    
+!in_ora4mas
   <- joinWorkspace("soccerWorkspace",_);
	  +in_ora4mas.