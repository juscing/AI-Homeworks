# AI-PS2
The second homework CS 4710 AI Spring 2015.

## Collaborators
* Justin Ingram (jci5kb)
* Megan Bishop (mgb5db)

## Information
See REPORT.md for the required report.

## Ideas for handling uncertainty we have considered:
* Jitter detection for when to stop planning and start moving
* Doing lots of pings at the beginning to determine the uncertainty function
* Building a map as we go using weighted averages of the points for the score/likelihood of being able to move there
* Calculating the function by pinging a lot at the beginning
* Starting with a function and tweaking it as you go
* Goodness of a point is the average of the n points around it


## Robot functionality
* HAL9000 - Strictly planning
* Rosie
* C3PO
* WallE - planning repeatedly
* Sonny - constant moving and edge hugging
