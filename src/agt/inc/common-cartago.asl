// Set of common plans for cartago

// the goal !focus_env_art is produced by the JaCaMo launcher for the agent to focus on initial artifacts
+!jcm__focus_env_art([],_).
+!jcm__focus_env_art(L,0)   <- .print("Error focusing on environment artifacts ",L).

@lf_env_art[atomic]
+!jcm__focus_env_art([art_env(W,H,A)|T],Try)
	: true
<-
	!join_workspace(W,H); 
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
      
+!join_workspace(W,_) : jcm__ws(W,I) <- cartago.set_current_wsp(I).      
+!join_workspace(W,"localhost") <- joinWorkspace(W,I); +jcm__ws(W,I).
+!join_workspace(W,localhost)   <- joinWorkspace(W,I); +jcm__ws(W,I).
+!join_workspace(W,H)           <- joinRemoteWorkspace(W,H,I); +jcm__ws(W,I).