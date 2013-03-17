default:
		javac src/view/*.java src/model/*.java src/*.java
		java src/Animator
run:
		javac src/view/*.java src/model/*.java src/*.java
		java src/Animator

clean:
		$(RM) src/view/*.class
		$(RM) src/model/*.class
		$(RM) src/*.class