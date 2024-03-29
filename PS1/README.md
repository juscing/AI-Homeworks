# AI-PS1
The first homework of CS 4710 SP 15 Artificial Intelligence

## Collaborators
* Justin Ingram (jci5kb)
* Megan Bishop (mgb5db)

## Compiling
The code is written in Java on a Java 8 compliant machine, although there may not be any Java 8 specific features in the code.
```bash
mkdir bin
javac -d ./bin src/*
cd bin
java Main
```

## Notes on our Implementation
* The LEARN command adds things to the list of facts that are known, but any call to TEACH after calling LEARN will invalidate any knowledge gained by calling LEARN.
* If a variable has been taught as a result of a rule, then trying to use TEACH to set the truth value of the variable will be ignored. If there was previously a truth value associated with a variable when it is taught as a rule, this will be deleted.
* Because of these properties, the WHY command will always backwards chain, as will the QUERY command. Facts acquired by using LEARN will be ignored when backwards chaining, so all rules will always be fully evaluated or explained. So, if you had previously called LEARN, and then set something to FALSE, but did not call LEARN again, QUERY would evaluate the system in its current state and report FALSE (This makes us disagree with a line 3 in mid2.out).
* If there are any variables in a rule that have not been previously defind, the rule will be ignored.

## Sample I/O
```
TEACH A = "IT IS A VERY HOT DAY"
TEACH B = "I LIKE ICE CREAM ON HOT DAYS"
TEACH C = "I EAT ICE CREAM"
TEACH D = "I DRINK COFFEE"
TEACH E = "E"
TEACH F = "F"
TEACH G = "G"
TEACH A = TRUE
TEACH B = TRUE
LIST
Variables:
	A = "IT IS A VERY HOT DAY"
	B = "I LIKE ICE CREAM ON HOT DAYS"
	C = "I EAT ICE CREAM"
	D = "I DRINK COFFEE"
	E = "E"
	F = "F"
	G = "G"

Facts:
	A
	B

Rules:

TEACH A -> B
TEACH B -> C
TEACH E = TRUE
TEACH G = TRUE
LIST
Variables:
	A = "IT IS A VERY HOT DAY"
	B = "I LIKE ICE CREAM ON HOT DAYS"
	C = "I EAT ICE CREAM"
	D = "I DRINK COFFEE"
	E = "E"
	F = "F"
	G = "G"

Facts:
	A
	E
	G

Rules:
	A -> B
	B -> C

QUERY C
true
WHY C
true
I KNOW IT IS TRUE THAT "IT IS A VERY HOT DAY"
BECAUSE "IT IS A VERY HOT DAY" I KNOW THAT "I LIKE ICE CREAM ON HOT DAYS"
BECAUSE "I LIKE ICE CREAM ON HOT DAYS" I KNOW THAT "I EAT ICE CREAM"
THUS I CAN PROVE "I EAT ICE CREAM"

LEARN
LIST
Variables:
	A = "IT IS A VERY HOT DAY"
	B = "I LIKE ICE CREAM ON HOT DAYS"
	C = "I EAT ICE CREAM"
	D = "I DRINK COFFEE"
	E = "E"
	F = "F"
	G = "G"

Facts:
	A
	B
	C
	E
	G

Rules:
	A -> B
	B -> C

QUERY E&(!F|!G)
true
WHY E&(!F|!G)
true
I KNOW IT IS TRUE THAT "E"
I KNOW IT IS TRUE THAT NOT "F"
THUS I CAN PROVE (  NOT "F" OR  NOT "G" )
THUS I CAN PROVE "E" AND  (  NOT "F" OR  NOT "G" )

exit
```
